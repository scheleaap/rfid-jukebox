package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource, Sync}
import cats.syntax.flatMap._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Actions.executeAction
import info.maaskant.jukebox.Card.{Album, Stop}
import info.maaskant.jukebox.State.Stopped
import info.maaskant.jukebox.mopidy.{DefaultMopidyClient, MopidyClient}
import info.maaskant.jukebox.rfid.{CardReader, FakeCardReader, FixedUidReader, Uid}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable
import sttp.client._
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend

import scala.concurrent.duration._

object Application extends TaskApp with StrictLogging {
  logger.debug("DEBUG")
  logger.info("INFO")
  logger.warn("WARN")

  private val controller = 0
  private val chipSelect = 0
  private val resetGpio = 25

  private val cardMapping: Map[Uid, Card] = Map(
    Uid("ebd1a421") -> Album(SpotifyUri("spotify:album:7Eoz7hJvaX1eFkbpQxC5PA")),
    Uid("album2") -> Album(SpotifyUri("spotify:album:album2")),
    Uid("TODO-STOP") -> Stop,
  )

  private def createCardReaderResource = {
    //    Mfrc522CardReader.resource(controller, chipSelect, resetGpio)
    //    FakeCardReader.resource(new TimeBasedReader())
    FakeCardReader.resource(new FixedUidReader(IndexedSeq(
      //      None,
      Some(Uid("ebd1a421")),
      Some(Uid("ebd1a421")),
      Some(Uid("ebd1a421")),
      Some(Uid("album2")),
      Some(Uid("album2")),
      Some(Uid("TODO-STOP")),
    )))
  }

  override def run(args: List[String]): Task[ExitCode] = {
    val resources: Resource[Task, (DefaultMopidyClient[Task], CardReader[Task])] = for {
      sttpBackend <- AsyncHttpClientMonixBackend.resource()
      mopidyClient = DefaultMopidyClient(uri"http://localhost:6680/mopidy/rpc")(F = implicitly[Sync[Task]], sttpBackend = sttpBackend)
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
        .doOnNext(i => Task(logger.debug(s"Logical card: $i")))
        //.dump("logical")
        .scanEval[State](Task.pure(Stopped)) { (s0, card) =>
          updateStateAndExecuteAction(s0, card)(mopidyClient)
        }
        //.dump("state")
        .foldWhileLeftL(())((_, state) => state match {
          case Stopped => Right(())
          case _ => Left(())
        })
        .map(_ => ExitCode.Success)
    }
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