package info.maaskant.jukebox.mopidy

import JsonRpcWrites.tracklistAddWrites

object MopidyClient extends App {
  val b = TracklistAdd(Seq("spotify:track:4jNQkWhuzqrbqQuqanFFJ6"))
  val s = tracklistAddWrites.writes(b).toString()
  println(s)
}
