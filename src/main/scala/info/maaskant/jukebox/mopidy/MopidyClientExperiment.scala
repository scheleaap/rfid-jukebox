package info.maaskant.jukebox.mopidy

import cats.effect.IO
import info.maaskant.jukebox.Card
import info.maaskant.jukebox.mopidy.JsonRpcWrites._
import sttp.client3.{basicRequest, _}

import scala.annotation.nowarn

@nowarn
object MopidyClientExperiment {
  val b = AddToTracklist(Seq("spotify:track:4jNQkWhuzqrbqQuqanFFJ6"))
  val s = addToTracklistWrites.writes(b).toString()
  println(s)

  private def callMopidy(card: Card): IO[Unit] = IO {
    println(card)
    val request = basicRequest
      .post(uri"http://localhost:6680/mopidy/rpc")
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.clear"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.add", "params": {"uris":["spotify:track:4jNQkWhuzqrbqQuqanFFJ6"]}}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.playback.play"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
  }

}
