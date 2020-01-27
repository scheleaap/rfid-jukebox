package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource, Sync}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Actions.executeAction
import info.maaskant.jukebox.Card.{Album, Stop}
import info.maaskant.jukebox.State.Stopped
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyClient}
import info.maaskant.jukebox.rfid.{CardReader, Mfrc522CardReader, Uid}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend
import sttp.model.Uri

object Application extends TaskApp with StrictLogging {
  private val controller = 0
  private val chipSelect = 0
  private val resetGpio = 25

  private val cardMapping: Map[Uid, Card] = Map(
    Uid("ebd1a421") -> Stop,
    Uid("042abc4a325e81") -> Album(SpotifyUri("spotify:album:2WT1pbYjLJciAR26yMebkH")), // The Dark Side Of The Moon
    Uid("0426bc4a325e81") -> Album(SpotifyUri("spotify:album:02Ast9sM8awNiA2ViVjO4Q")), // Nightfall in Middle Earth
    Uid("042ebc4a325e81") -> Album(SpotifyUri("spotify:album:1YaUAkNsLKXtJfb0FVZcyu")), // New York - Addis - London
    Uid("TODO") -> Album(SpotifyUri("spotify:playlist:7klfbs5nEXp6KiVHcHcZXz")) // Feestselectie
  )

  private def createCardReaderResource = {
    Mfrc522CardReader.resource(controller, chipSelect, resetGpio)
    //    FakeCardReader.resource(new TimeBasedReader())
    //    FakeCardReader.resource(new FixedUidReader(IndexedSeq(
    //      None,
    //      Some(Uid("ebd1a421")),
    //      Some(Uid("ebd1a421")),
    //      Some(Uid("ebd1a421")),
    //      Some(Uid("album2")),
    //      Some(Uid("album2")),
    //      Some(Uid("TODO-STOP")),
    //    )))
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
  ): Task[Long] =
    Observable
      .repeatEvalF(cardReader.read())
      .delayOnNext(config.readInterval)
      .distinctUntilChanged
      //.dump("physical")
      .map(physicalCardToLogicalCard)
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

  private def physicalCardToLogicalCard(pco: Option[rfid.Card]): Card =
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
      cardReader <- createCardReaderResource
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
