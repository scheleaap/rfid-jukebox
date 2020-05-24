package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource, Sync}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Actions.executeAction
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.State.Stopped
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyClient, MopidyUri}
import info.maaskant.jukebox.rfid.{CardReader, Mfrc522CardReader, Uid}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend
import sttp.model.Uri

object Application extends TaskApp with StrictLogging {
  private def createCardMapping(albums: Map[Uid, MopidyUri], commands: Map[Uid, Command]): Map[Uid, Card] =
    albums.map { case (uid, uri) => (uid, Album(uri)) } ++
      commands.map {
        case (uid, command) =>
          (uid, command match {
            case Command.Shutdown => Card.Shutdown
            case Command.Stop => Card.Stop
          })
      }

  private def createCardReaderResource(config: Spi) = {
    Mfrc522CardReader.resource(config.controller, config.chipSelect, config.resetGpio)
    //    FakeCardReader.resource(new TimeBasedReader())
//    FakeCardReader.resource(
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
//    )
  }

  override def run(args: List[String]): Task[ExitCode] =
    for {
      config <- Config.loadF()
      _ <- Task(logger.info(s"Configuration: $config"))
      exitCode <- resources(config)
        .use {
          case (mopidyClient, cardReader) =>
            pipeline(config, mopidyClient, cardReader).map(_ => ExitCode.Success)
        }
        .onErrorHandleWith(t => Task(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

  private def pipeline(
      config: Config,
      mopidyClient: MopidyClient[Task],
      cardReader: CardReader[Task]
  ): Task[Long] = {
    val cardMapping: Map[Uid, Card] = createCardMapping(config.albums, config.commands)

    Observable
      .repeatEvalF(cardReader.read())
      .delayOnNext(config.readInterval)
      .distinctUntilChanged
      //.dump("physical")
      .map(physicalCardToLogicalCard(_, cardMapping))
      .distinctUntilChanged
      .doOnNext(i => Task(logger.info(s"Logical card: $i")))
      //.dump("logical")
      .scanEval[State](Task.pure(Stopped)) { (s0, card) =>
        updateStateAndExecuteAction(s0, card)(mopidyClient)
      }
      //.dump("state")
      //.foldWhileLeftL(())((_, state) => state match {
      //  case Stopped => Right(())
      //  case _ => Left(())
      //})
      .countL
  }

  private def physicalCardToLogicalCard(pco: Option[rfid.Card], cardMapping: Map[Uid, Card]): Card =
    pco
      .map(pc => cardMapping.getOrElse(pc.uid, Card.Unknown))
      .getOrElse(Card.None)

  private def resources(config: Config): Resource[Task, (DefaultMopidyClient[Task], CardReader[Task])] =
    for {
      sttpBackend <- AsyncHttpClientMonixBackend.resource()
      mopidyClient = DefaultMopidyClient(Uri.apply(config.mopidy.baseUrl))(
        F = implicitly[Sync[Task]],
        sttpBackend = sttpBackend
      )
      cardReader <- createCardReaderResource(config.spi)
    } yield (mopidyClient, cardReader)

  private def updateStateAndExecuteAction(s0: State, card: Card)(
      implicit mopidyClient: MopidyClient[Task]
  ): Task[State] = {
    val (s1, action0) = s0(card)
    action0 match {
      case None => Task.pure(s1)
      case Some(action) =>
        executeAction(action).flatMap { success =>
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
