package info.maaskant.jukebox

import cats.effect.ExitCode
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.rfid.{Card, FakeCardReader, Uid}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable
import sttp.client._

import scala.concurrent.duration._

object Application extends TaskApp with StrictLogging {
  logger.debug("DEBUG")
  logger.info("INFO")
  logger.warn("WARN")

  private val controller = 0
  private val chipSelect = 0
  private val resetGpio = 25

  private val cardMapping: Map[Uid, SpotifyUri] = Map(
    Uid("ebd1a421") -> SpotifyUri("spotify:album:7Eoz7hJvaX1eFkbpQxC5PA")
  )

  override def run(args: List[String]): Task[ExitCode] = {

    //    val cardReader = Mfrc522CardReader.resource(controller, chipSelect, resetGpio)
    val cardReader = FakeCardReader.resource()

    Observable.fromResource(cardReader)
      .flatMap { rfid =>
        Observable
          .repeatEvalF(rfid.read())
          .delayOnNext(500.milliseconds)
      }
      .flatMap(_.map(Observable.pure).getOrElse(Observable.empty))
      // .dump("card")
      .distinctUntilChanged
      .dump("distinct")
      .doOnNext(callMopidy)
      .countL
      .map(_ => ExitCode.Success)
  }

  private def callMopidy(card: Card): Task[Unit] = Task {
    println(card)
    val request = basicRequest.post(uri"http://localhost:6680/mopidy/rpc")
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.clear"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.add", "params": {"uris":["spotify:track:4jNQkWhuzqrbqQuqanFFJ6"]}}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.playback.play"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
  }
}

/**
 * s0: stopped
 * s1: playing(current album)
 * s2: paused(last album)
 *
 * s0 -> s1 emits Play(album)
 * s1 -> s2 emits Pause
 * s2 -> s1 emits Resume
 * s1 -> s0 emits Stop
 * s2 -> s0 emits Stop
 */

//object StateMachine {
//
//}
//sealed trait PlayState {
//
//  case object Stopped extends State
//
//  case class Playing(currentUri: SpotifyUri) extends State
//
//}
//
//sealed trait Event {
//
//  case class Play(uri: SpotifyUri) extends Event
//
//}