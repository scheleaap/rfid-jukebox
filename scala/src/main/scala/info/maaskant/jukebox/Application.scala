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
import sttp.client._
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend

import scala.concurrent.duration._

object Application extends TaskApp with StrictLogging {
  private val controller = 0
  private val chipSelect = 0
  private val resetGpio = 25

  private val cardMapping: Map[Uid, Card] = Map(
    Uid("ebd1a421") -> Stop,
    Uid("042abc4a325e81") -> Album(SpotifyUri("spotify:album:2WT1pbYjLJciAR26yMebkH")), // The Dark Side Of The Moon
    Uid("0426bc4a325e81") -> Album(SpotifyUri("spotify:album:02Ast9sM8awNiA2ViVjO4Q")), // Nightfall in Middle Earth
    Uid("042ebc4a325e81") -> Album(SpotifyUri("spotify:album:1YaUAkNsLKXtJfb0FVZcyu")), // New York - Addis - London
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

  override def run(args: List[String]): Task[ExitCode] = {
    val resources: Resource[Task, (DefaultMopidyClient[Task], CardReader[Task])] = for {
      sttpBackend <- AsyncHttpClientMonixBackend.resource()
      mopidyClient = DefaultMopidyClient(uri"http://framboos:6680/mopidy/rpc")(F = implicitly[Sync[Task]], sttpBackend = sttpBackend)
      cardReader <- createCardReaderResource
    } yield (mopidyClient, cardReader)

    resources.use { case (mopidyClient, cardReader) =>
      Observable
        .repeatEvalF(cardReader.read())
        .delayOnNext(500.milliseconds)
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
        .map(_ => ExitCode.Success)
    }.onErrorHandleWith(t => Task {
      logger.error("Fatal error", t)
      ExitCode.Error
    })
  }

  private def physicalCardToLogicalCard(pco: Option[rfid.Card]): Card = pco.map(pc =>
    cardMapping.getOrElse(pc.uid, Card.Unknown))
    .getOrElse(Card.None)

  private def updateStateAndExecuteAction(s0: State, card: Card)(implicit mopidyClient: MopidyClient[Task]): Task[State] = {
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