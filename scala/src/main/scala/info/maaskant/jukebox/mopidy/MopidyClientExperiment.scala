package info.maaskant.jukebox.mopidy

import info.maaskant.jukebox.Card
import info.maaskant.jukebox.mopidy.JsonRpcWrites.tracklistAddWrites
import monix.eval.Task
import sttp.client.{basicRequest, _}

object MopidyClientExperiment extends App {
  val b = TracklistAdd(Seq("spotify:track:4jNQkWhuzqrbqQuqanFFJ6"))
  val s = tracklistAddWrites.writes(b).toString()
  println(s)

  private def callMopidy(card: Card): Task[Unit] = Task {
    println(card)
    val request = basicRequest.post(uri"http://localhost:6680/mopidy/rpc")
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.clear"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.tracklist.add", "params": {"uris":["spotify:track:4jNQkWhuzqrbqQuqanFFJ6"]}}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
    // curl -d '{"jsonrpc": "2.0", "id": 1, "method": "core.playback.play"}' -H 'Content-Type: application/json' http://framboos:6680/mopidy/rpc
  }

}
