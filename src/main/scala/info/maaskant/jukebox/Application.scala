package info.maaskant.jukebox

import cats.data.State
import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.Process.runCommand
import info.maaskant.jukebox.StateMachine.Uninitialized
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyUri}
import info.maaskant.jukebox.rfid.{
  Card => PhysicalCard,
  CardReader,
  FixedUidReader,
  ModifiedMfrc522CardReader,
  OriginalMfrc522CardReader,
  TimeBasedReader,
  Uid
}
import sttp.model.Uri

import scala.annotation.tailrec
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

  private def createCardReader(reader: String, config: Spi): CardReader[_] = reader match {
    case "modified" => ModifiedMfrc522CardReader(config.controller, config.chipSelect, config.resetGpio)
//    case "time-based" => new TimeBasedReader()
//    case "fixed-uid" =>
//      new FixedUidReader(
//        IndexedSeq(
//          None,
//          Some(Uid("ebd1a421")),
//          Some(Uid("ebd1a421")),
//          Some(Uid("ebd1a421")),
//          Some(Uid("042abc4a325e81")),
//          Some(Uid("042ebc4a325e81")),
//          Some(Uid("TODO"))
//        )
//      )
//    case _ => OriginalMfrc522CardReader(config.controller, config.chipSelect, config.resetGpio)
  }

  override def run(args: List[String]): IO[ExitCode] =
    for {
      config <- Config.load()
      _ <- IO(logger.info(s"Configuration: $config"))
      exitCode <- resources(config)
        .use { mopidyClient =>
          val cardReader = createCardReader(config.reader, config.spi)
          val cardMapping: Map[Uid, Card] = createCardMapping(config.albums, config.commands)
          val stateMachine = StateMachine(config.streamPauseTimeout)
          val actionExecutor = new ActionExecutor(config.hooks, mopidyClient)
          val onCardChangeEventHook = config.hooks
            .flatMap(_.onCardChange)
            .fold(IO.unit)(runCommand[IO](raiseError = false))

//          pipeline(
//            cardReader,
//            config.readInterval,
//            cardMapping,
//            stateMachine,
//            actionExecutor,
//            onCardChangeEventHook
//          )
          pipeline(cardReader, ???, stateMachine)
            .map(_ => ExitCode.Success)
        }
        .onError(t => IO(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

//  private def pipeline(
//      cardReader: CardReader,
//      readInterval: FiniteDuration,
//      cardMapping: Map[Uid, Card],
//      stateMachine: StateMachine,
//      actionExecutor: ActionExecutor[IO],
//      onCardChangeEventHook: IO[Unit]
//  ): IO[Long] =
//    cardReader
//      .read()
//      .delayOnNext(readInterval)
//      .map(physicalCardToLogicalCard(_, cardMapping))
//      .distinctUntilChanged
//      .doOnNext(i =>
//        IO(logger.info(s"Logical card: $i")) >>
//          onCardChangeEventHook
//      )
//      .scanEval[StateMachine#State](IO.pure(stateMachine.Uninitialized))(
//        updateStateAndExecuteAction(actionExecutor)
//      )
//      //.foldWhileLeftL(())((_, state) => state match {
//      //  case Stopped => Right(())
//      //  case _ => Left(())
//      //})
//      .countL

  private val ActiveReadInterval = 1.second // TODO Make configurable or constant
  private val MaxReadInterval = 30.seconds // TODO Make configurable or constant
  private val NoCardReadCountBeforeBackoff = 10 // TODO Make configurable or constant
  private val BackoffFactor = 2 // TODO Make configurable or constant

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

  private def singleIteration(
      oldState: IterationState,
      readPhysicalCard: () => IO[Option[PhysicalCard]]
  ): IO[IterationState] = {
    for {
      physicalCardOption <- readPhysicalCard()
      logicalCard = physicalCardToLogicalCard(physicalCardOption, ???)
      (newReadIntervalState, sleepDuration) = calculateReadInterval(oldState.readIntervalState, logicalCard)
      passToStateMachine = oldState.previousCard != logicalCard
      newStateMachineState <-
        if (passToStateMachine) {
          updateStateAndExecuteAction(???)(oldState.stateMachineState, logicalCard)
        } else { IO.pure(oldState.stateMachineState) }
      _ <- IO.sleep(sleepDuration)
    } yield IterationState(logicalCard, newReadIntervalState, newStateMachineState)
  }

  private def infiniteIteration(
      oldState: IterationState,
      readPhysicalCard: () => IO[Option[PhysicalCard]]
  ): IO[IterationState] =
    singleIteration(oldState, readPhysicalCard).flatMap(newState => infiniteIteration(newState, readPhysicalCard))

  private def pipeline[R](
      cardReader: CardReader[R],
      cardReaderResource: R,
      stateMachine: StateMachine
  ): IO[IterationState] = {
    val initialState: IterationState = IterationState(
      previousCard = Card.None,
      readIntervalState = ReadIntervalState(noCardReadCount = 0, readInterval = ActiveReadInterval),
      stateMachineState = stateMachine.Uninitialized
    )
    infiniteIteration(oldState = initialState, readPhysicalCard = () => cardReader.read(cardReaderResource))
  }

  private def physicalCardToLogicalCard(pco: Option[PhysicalCard], cardMapping: Map[Uid, Card]): Card =
    pco
      .map(pc => cardMapping.getOrElse(pc.uid, Card.Unknown(pc.uid)))
      .getOrElse(Card.None)

  private def resources(config: Config): Resource[IO, DefaultMopidyClient[IO]] =
    ???
//    for {
//      sttpBackend <- AsyncHttpClientMonixBackend.resource()
//      mopidyClient = DefaultMopidyClient(Uri.apply(config.mopidy.baseUrl))(
//        F = implicitly[Sync[IO]],
//        sttpBackend = sttpBackend
//      )
//    } yield mopidyClient

  private def updateStateAndExecuteAction(
      actionExecutor: ActionExecutor[IO]
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
