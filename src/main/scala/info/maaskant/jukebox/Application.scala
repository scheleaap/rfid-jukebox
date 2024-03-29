package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource, Sync}
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
  Uid
}
import monix.eval.{Task, TaskApp}
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend
import sttp.model.Uri

import scala.concurrent.duration.FiniteDuration

object Application extends TaskApp with StrictLogging {
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

  private def createCardReader(reader: String, config: Spi): CardReader = reader match {
    case "modified" => ModifiedMfrc522CardReader(config.controller, config.chipSelect, config.resetGpio)
    case "time-based" => new TimeBasedReader()
    case "fixed-uid" =>
      new FixedUidReader(
        IndexedSeq(
          None,
          Some(Uid("ebd1a421")),
          Some(Uid("ebd1a421")),
          Some(Uid("ebd1a421")),
          Some(Uid("042abc4a325e81")),
          Some(Uid("042ebc4a325e81")),
          Some(Uid("TODO"))
        )
      )
    case _ => OriginalMfrc522CardReader(config.controller, config.chipSelect, config.resetGpio)
  }

  override def run(args: List[String]): Task[ExitCode] =
    for {
      config <- Config.loadF()
      _ <- Task(logger.info(s"Configuration: $config"))
      exitCode <- resources(config)
        .use { mopidyClient =>
          val cardReader = createCardReader(config.reader, config.spi)
          val cardMapping: Map[Uid, Card] = createCardMapping(config.albums, config.commands)
          val stateMachine = StateMachine(config.streamPauseTimeout)
          val actionExecutor = new ActionExecutor(config.hooks, mopidyClient)
          val onCardChangeEventHook = config.hooks
            .flatMap(_.onCardChange)
            .fold(Task.unit)(runCommand[Task](raiseError = false))

          pipeline(
            cardReader,
            config.readInterval,
            cardMapping,
            stateMachine,
            actionExecutor,
            onCardChangeEventHook
          ).map(_ => ExitCode.Success)
        }
        .onErrorHandleWith(t => Task(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

  private def pipeline(
      cardReader: CardReader,
      readInterval: FiniteDuration,
      cardMapping: Map[Uid, Card],
      stateMachine: StateMachine,
      actionExecutor: ActionExecutor[Task],
      onCardChangeEventHook: Task[Unit]
  ): Task[Long] =
    cardReader
      .read()
      .delayOnNext(readInterval)
      .map(physicalCardToLogicalCard(_, cardMapping))
      .distinctUntilChanged
      .doOnNext(i =>
        Task(logger.info(s"Logical card: $i")) >>
          onCardChangeEventHook
      )
      .scanEval[StateMachine#State](Task.pure(stateMachine.Uninitialized))(
        updateStateAndExecuteAction(actionExecutor)
      )
      //.foldWhileLeftL(())((_, state) => state match {
      //  case Stopped => Right(())
      //  case _ => Left(())
      //})
      .countL

  private def physicalCardToLogicalCard(pco: Option[rfid.Card], cardMapping: Map[Uid, Card]): Card =
    pco
      .map(pc => cardMapping.getOrElse(pc.uid, Card.Unknown(pc.uid)))
      .getOrElse(Card.None)

  private def resources(config: Config): Resource[Task, DefaultMopidyClient[Task]] =
    for {
      sttpBackend <- AsyncHttpClientMonixBackend.resource()
      mopidyClient = DefaultMopidyClient(Uri.apply(config.mopidy.baseUrl))(
        F = implicitly[Sync[Task]],
        sttpBackend = sttpBackend
      )
    } yield mopidyClient

  private def updateStateAndExecuteAction(
      actionExecutor: ActionExecutor[Task]
  )(s0: StateMachine#State, card: Card): Task[StateMachine#State] = {
    val (s1, action0) = s0(card)
    action0 match {
      case None => Task.pure(s1)
      case Some(action) =>
        actionExecutor.executeAction(action).flatMap { success =>
          if (success) {
            Task(logger.debug(s"New state: $s1")) >> Task.pure(s1)
          } else {
            Task(logger.warn(s"Failed to execute action $action to go from $s0 to $s1")) >>
              Task.pure(s0)
          }
        }
    }
  }
}
