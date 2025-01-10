package info.maaskant.jukebox

import cats.effect._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.Process.runCommand
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyUri}
import info.maaskant.jukebox.rfid.{
  CardReader,
  FixedUidReader,
  ModifiedMfrc522CardReader,
  OriginalMfrc522CardReader,
  TimeBasedReader,
  Uid,
  Card => PhysicalCard
}
import sttp.client3.httpclient.cats.HttpClientCatsBackend
import sttp.model.Uri

import java.time.LocalDateTime
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Application extends IOApp with StrictLogging {
  private val NoCardReadCountBeforeBackoff = 10
  private val BackoffFactor: Long = 2

  private case class ReadIntervalState(noCardReadCount: Int, readInterval: FiniteDuration)

  private case class IterationState(
      previousCard: Card,
      readIntervalState: ReadIntervalState,
      stateMachineState: StateMachine#State
  )

  private def calculateMaxReadInterval(
      maxReadIntervalActivePeriods: FiniteDuration,
      maxReadIntervalQuietPeriods: FiniteDuration
  )(now: LocalDateTime): FiniteDuration =
    now.getHour match {
      case 0 => maxReadIntervalQuietPeriods
      case 1 => maxReadIntervalQuietPeriods
      case 2 => maxReadIntervalQuietPeriods
      case 3 => maxReadIntervalQuietPeriods
      case 4 => maxReadIntervalQuietPeriods
      case 5 => maxReadIntervalQuietPeriods
      case 6 => maxReadIntervalActivePeriods
      case 7 => maxReadIntervalActivePeriods
      case 8 => maxReadIntervalActivePeriods
      case 9 => maxReadIntervalQuietPeriods
      case 10 => maxReadIntervalQuietPeriods
      case 11 => maxReadIntervalQuietPeriods
      case 12 => maxReadIntervalQuietPeriods
      case 13 => maxReadIntervalQuietPeriods
      case 14 => maxReadIntervalQuietPeriods
      case 15 => maxReadIntervalQuietPeriods
      case 16 => maxReadIntervalQuietPeriods
      case 17 => maxReadIntervalActivePeriods
      case 18 => maxReadIntervalActivePeriods
      case 19 => maxReadIntervalActivePeriods
      case 20 => maxReadIntervalActivePeriods
      case 21 => maxReadIntervalActivePeriods
      case 22 => maxReadIntervalQuietPeriods
      case 23 => maxReadIntervalQuietPeriods
      case _ => maxReadIntervalActivePeriods
    }

  private def calculateReadInterval(
      minReadInterval: FiniteDuration
  )(oldState: ReadIntervalState, maxReadInterval: FiniteDuration, card: Card): (ReadIntervalState, FiniteDuration) = {
    val newState = card match {
      case Card.None =>
        if (oldState.noCardReadCount < NoCardReadCountBeforeBackoff) {
          ReadIntervalState(oldState.noCardReadCount + 1, oldState.readInterval)
        } else {
          ReadIntervalState(0, (oldState.readInterval * BackoffFactor).min(maxReadInterval))
        }
      case _ => ReadIntervalState(0, minReadInterval)
    }
    (newState, newState.readInterval)
  }

  private def createCardMapping(albums: Map[Uid, MopidyUri], commands: Map[Uid, Command]): Map[Uid, Card] =
    albums.map { case (uid, uri) => (uid, Album(uri)) } ++
      commands.map { case (uid, command) =>
        (
          uid,
          command match {
            case Command.Shutdown => Card.Shutdown
            case Command.Stop => Card.Stop
          }
        )
      }

  private def createCardReaderResource(reader: String, config: Spi): Resource[IO, CardReader] = reader match {
    case "modified" => ModifiedMfrc522CardReader.resource(config.controller, config.chipSelect, config.resetGpio)
    case "time-based" => Resource.pure(new TimeBasedReader())
    case "fixed-uid" => Resource.pure(new FixedUidReader(None))
    case _ => OriginalMfrc522CardReader.resource(config.controller, config.chipSelect, config.resetGpio)
  }

  private def infinitelyIterate[S](initialState: S, iteration: S => IO[S]): IO[S] = {
    def loop(oldState: S): IO[S] = {
      iteration(oldState).flatMap(newState => loop(newState))
    }
    loop(initialState)
  }

  override def run(args: List[String]): IO[ExitCode] = for {
    config <- Config.load()
    _ <- IO(logger.info(s"Configuration: $config"))
    exitCode <- resources(config)
      .use { case (cardReader, mopidyClient) =>
        val cardMapping: Map[Uid, Card] = createCardMapping(config.albums, config.commands)
        val stateMachine = StateMachine(config.streamPauseTimeout)
        val actionExecutor = new MopidyActionExecutor(config.hooks, mopidyClient)
        val onCardChangeEventHook = config.hooks
          .flatMap(_.onCardChange)
          .fold(IO.unit)(runCommand(raiseError = false))
        val initialState: IterationState = IterationState(
          previousCard = Card.None,
          readIntervalState = ReadIntervalState(noCardReadCount = 0, readInterval = config.minReadInterval),
          stateMachineState = stateMachine.Uninitialized
        )

        infinitelyIterate(
          initialState,
          pipeline(
            cardReader.read _,
            cardMapping,
            actionExecutor,
            onCardChangeEventHook,
            calculateMaxReadInterval(config.maxReadIntervalActivePeriods, config.maxReadIntervalQuietPeriods),
            calculateReadInterval(config.minReadInterval)
          )
        ).map(_ => ExitCode.Success)
      }
      .onError(t => IO(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
  } yield exitCode

  private def pipeline(
      readPhysicalCard: () => IO[Option[PhysicalCard]],
      cardMapping: Map[Uid, Card],
      actionExecutor: ActionExecutor,
      onCardChangeEventHook: IO[Unit],
      calculateMaxReadInterval: LocalDateTime => FiniteDuration,
      calculateReadInterval: (ReadIntervalState, FiniteDuration, Card) => (ReadIntervalState, FiniteDuration)
  )(oldState: IterationState): IO[IterationState] = for {
    _ <- IO(logger.debug("Begin pipeline"))
    physicalCardOption <- readPhysicalCard()
    logicalCard = physicalCardToLogicalCard(physicalCardOption, cardMapping)
    hasLogicalCardChanged = oldState.previousCard != logicalCard
    _ <-
      if (hasLogicalCardChanged) {
        IO(logger.info(s"Logical card: $logicalCard")) >>
          onCardChangeEventHook
      } else { IO.unit }
    now <- IO(LocalDateTime.now())
    maxReadInterval = calculateMaxReadInterval(now)
    _ <- IO(logger.debug(s"New max read interval: $maxReadInterval"))
    (newReadIntervalState, readInterval) = calculateReadInterval(
      oldState.readIntervalState,
      maxReadInterval,
      logicalCard
    )
    _ <- IO(logger.debug(s"New read interval: $readInterval"))
    newStateMachineState <- updateStateAndExecuteAction(actionExecutor)(oldState.stateMachineState, logicalCard)
    _ <- IO.sleep(readInterval)
  } yield IterationState(logicalCard, newReadIntervalState, newStateMachineState)

  private def physicalCardToLogicalCard(pco: Option[PhysicalCard], cardMapping: Map[Uid, Card]): Card =
    pco
      .map(pc => cardMapping.getOrElse(pc.uid, Card.Unknown(pc.uid)))
      .getOrElse(Card.None)

  private def resources(config: Config): Resource[IO, (CardReader, DefaultMopidyClient)] = for {
    sttpBackend <- HttpClientCatsBackend.resource[IO]()
    mopidyClient = DefaultMopidyClient(Uri.apply(config.mopidy.baseUrl))(sttpBackend)
    cardReader <- createCardReaderResource(config.reader, config.spi)
  } yield (cardReader, mopidyClient)

  private def updateStateAndExecuteAction(
      actionExecutor: ActionExecutor
  )(s0: StateMachine#State, card: Card): IO[StateMachine#State] = {
    val (s1, action0) = s0(card)
    action0 match {
      case None => IO.pure(s1)
      case Some(action) =>
        actionExecutor.executeAction(action).flatMap { success =>
          if (success) {
            IO(logger.debug(s"New state: $s1")) >> IO.pure(s1)
          } else {
            IO(logger.warn(s"Failed to execute action $action to go from $s0 to $s1")) >>
              IO.pure(s0)
          }
        }
    }
  }
}
