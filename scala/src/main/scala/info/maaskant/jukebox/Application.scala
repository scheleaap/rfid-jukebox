package info.maaskant.jukebox

import cats.effect.{ExitCode, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Card.{Album, Stop}
import info.maaskant.jukebox.State.Stopped
import info.maaskant.jukebox.mopidy.MopidyClient
import info.maaskant.jukebox.rfid.{FakeCardReader, FixedUidReader, Uid}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

import scala.concurrent.duration._
import scala.util.control.NonFatal

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

  override def run(args: List[String]): Task[ExitCode] = {

    //    val cardReader = Mfrc522CardReader.resource(controller, chipSelect, resetGpio)
    //    val cardReader = FakeCardReader.resource(new TimeBasedReader())
    val cardReader = FakeCardReader.resource(new FixedUidReader(IndexedSeq(
      //      None,
      Some(Uid("ebd1a421")),
      Some(Uid("ebd1a421")),
      Some(Uid("ebd1a421")),
      Some(Uid("album2")),
      Some(Uid("album2")),
      Some(Uid("TODO-STOP")),
    )))

    Observable.fromResource(cardReader)
      .flatMap { rfid =>
        Observable
          .repeatEvalF(rfid.read())
          .delayOnNext(500.milliseconds)
      }
      .distinctUntilChanged
      //.dump("physical")
      .map(_.map(pc =>
        cardMapping.getOrElse(pc.uid, Card.Unknown))
        .getOrElse(Card.None))
      .distinctUntilChanged
      .dump("logical")
      .scanEval[State](Task.pure(Stopped)) { (s0, cr) =>
        val (s1, action) = s0(cr)
        action match {
          case None => Task.pure(s1)
          case Some(action) =>
            executeAction[Task](action, ???)
              .map(success => if (success) s1 else s0)
        }
      }
      //.dump("state")
      .foldWhileLeftL()((_, state) => state match {
        case Stopped => Right()
        case _ => Left()
      })
      .map(_ => ExitCode.Success)
  }

  // TODO Refactor with type classes
  private def executeAction[F[_] : Sync](action: Action, mopidyClient: MopidyClient[F]): F[Boolean] =
    (action match {
      case Action.Stop => mopidyClient.stopPlayback()
      case Action.Play(uri) =>
        mopidyClient.clearTracklist() >>
          mopidyClient.addToTracklist(Seq(uri.value)) >>
          mopidyClient.startPlayback()
      case Action.Pause => mopidyClient.pausePlayback()
      case Action.Resume => mopidyClient.resumePlayback()
    }).map(_ => true).recoverWith {
      case NonFatal(t) => Sync[F].delay {
        logger.warn("Could not execute action", t)
        false
      }
    }
}