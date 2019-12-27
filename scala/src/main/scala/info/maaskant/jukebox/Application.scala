package info.maaskant.jukebox

import cats.effect.ExitCode
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Card.{Album, Stop}
import info.maaskant.jukebox.rfid.{FakeCardReader, Uid}
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

  private val cardMapping: Map[Uid, Card] = Map(
    Uid("ebd1a421") -> Album(SpotifyUri("spotify:album:7Eoz7hJvaX1eFkbpQxC5PA")),
    Uid("TODO") -> Stop,
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
      .distinctUntilChanged
      // .dump("physicalCard")
      .map(_.map(pc =>
        cardMapping.getOrElse(pc.uid, Card.Unknown))
        .getOrElse(Card.None))
      .distinctUntilChanged
      // .dump("logicalCard")
      //      .doOnNext(callMopidy)
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
