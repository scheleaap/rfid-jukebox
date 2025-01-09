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

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Application extends IOApp with StrictLogging {
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
    case "fixed-uid" => Resource.pure(new FixedUidReader(Some(Uid("ebd1a421"))))
    case _ => OriginalMfrc522CardReader.resource(config.controller, config.chipSelect, config.resetGpio)
  }

  override def run(args: List[String]): IO[ExitCode] =
    for {
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
            readIntervalState = ReadIntervalState(noCardReadCount = 0, readInterval = ActiveReadInterval),
            stateMachineState = stateMachine.Uninitialized
          )

          infinitelyIterate(
            initialState,
            pipeline(
              cardReader.read _,
              cardMapping,
              actionExecutor,
              onCardChangeEventHook
            )
          ).map(_ => ExitCode.Success)
        }
        .onError(t => IO(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

  private val ActiveReadInterval = 1.second // TODO Make configurable or constant
  private val MaxReadInterval = 30.seconds // TODO Make configurable or constant
  private val NoCardReadCountBeforeBackoff = 10 // TODO Make configurable or constant
  private val BackoffFactor :Long = 2 // TODO Make configurable or constant

  private case class ReadIntervalState(noCardReadCount: Int, readInterval: FiniteDuration)

  private def calculateReadInterval(oldState: ReadIntervalState, card: Card): (ReadIntervalState, FiniteDuration) = {
    val newState = card match {
      case Card.None =>
        // TODO Make MaxReadInterval dependent on hour of day
        if (oldState.noCardReadCount < NoCardReadCountBeforeBackoff) {
          ReadIntervalState(oldState.noCardReadCount + 1, oldState.readInterval)
        } else {
          ReadIntervalState(0, (oldState.readInterval * BackoffFactor).min(MaxReadInterval))
        }
      case _ => ReadIntervalState(0, ActiveReadInterval)
    }
    (newState, newState.readInterval)
  }

  private case class IterationState(
      previousCard: Card,
      readIntervalState: ReadIntervalState,
      stateMachineState: StateMachine#State
  )

  private def infinitelyIterate[S](initialState: S, iteration: S => IO[S]): IO[S] = {
    def loop(oldState: S): IO[S] = {
      iteration(oldState).flatMap(newState => loop(newState))
    }
    loop(initialState)
  }

  private def pipeline(
      readPhysicalCard: () => IO[Option[PhysicalCard]],
      cardMapping: Map[Uid, Card],
      actionExecutor: ActionExecutor,
      onCardChangeEventHook: IO[Unit]
  )(oldState: IterationState): IO[IterationState] = {
    for {
      physicalCardOption <- readPhysicalCard()
      logicalCard = physicalCardToLogicalCard(physicalCardOption, cardMapping)
      hasLogicalCardChanged = oldState.previousCard != logicalCard
      _ <-
        if (hasLogicalCardChanged) {
          IO(logger.info(s"Logical card: $logicalCard")) >>
            onCardChangeEventHook
        } else { IO.unit }
      (newReadIntervalState, sleepDuration) = calculateReadInterval(oldState.readIntervalState, logicalCard)
      newStateMachineState <- updateStateAndExecuteAction(actionExecutor)(oldState.stateMachineState, logicalCard)
      _ <- IO.sleep(sleepDuration)
    } yield IterationState(logicalCard, newReadIntervalState, newStateMachineState)
  }

  private def physicalCardToLogicalCard(pco: Option[PhysicalCard], cardMapping: Map[Uid, Card]): Card =
    pco
      .map(pc => cardMapping.getOrElse(pc.uid, Card.Unknown(pc.uid)))
      .getOrElse(Card.None)

  private def resources(config: Config): Resource[IO, (CardReader, DefaultMopidyClient)] =
    for {
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
