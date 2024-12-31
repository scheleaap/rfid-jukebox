package info.maaskant.jukebox

import cats.data.State
import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.Process.runCommand
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyUri}
import info.maaskant.jukebox.rfid.{CardReader, FixedUidReader, ModifiedMfrc522CardReader, OriginalMfrc522CardReader, TimeBasedReader, Uid}
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
          pipeline()
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

  private def readPhysicalCard(): IO[Option[rfid.Card]] = ???
//  private def processRead(state: ProcessingState, in: Option[rfid.Card]): IO[ProcessingState] = ???

  case class CardReadingState(
      previousCard: Option[rfid.Card],
      noCardReadCount: Int,
      readInterval: FiniteDuration
  )
  val ActiveReadInterval = 1.second // TODO Make configurable or constant
  val MaxReadInterval = 30.seconds // TODO Make configurable or constant
  val NoCardReadCountBeforeBackoff = 10 // TODO Make configurable or constant
  val BackoffFactor = 2 // TODO Make configurable or constant

  private def singleIteration(state: CardReadingState): IO[CardReadingState] = {
    for {
      card <- readPhysicalCard()
      hasReadResultChanged = card != state.previousCard
      newState = card match {
        case Some(_) => CardReadingState(card, 0, ActiveReadInterval)
        case None =>
          if (state.noCardReadCount < NoCardReadCountBeforeBackoff) {
            CardReadingState(card, state.noCardReadCount + 1, state.readInterval)
          } else {
            CardReadingState(card, 0, (state.readInterval * BackoffFactor).min(MaxReadInterval))
          }
      }
      _ <- IO.sleep(newState.readInterval)
    } yield newState
  }

  private def infiniteIteration(oldState: CardReadingState): IO[CardReadingState] =
    singleIteration(oldState).flatMap(newState => infiniteIteration(newState))

  private def pipeline() = {
    val initialState :CardReadingState= ???
    infiniteIteration(initialState)
  }

  private def physicalCardToLogicalCard(pco: Option[rfid.Card], cardMapping: Map[Uid, Card]): Card =
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
