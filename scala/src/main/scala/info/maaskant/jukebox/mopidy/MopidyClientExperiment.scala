package info.maaskant.jukebox.mopidy

import info.maaskant.jukebox.mopidy.JsonRpcWrites.tracklistAddWrites

object MopidyClientExperiment extends App {
  val b = TracklistAdd(Seq("spotify:track:4jNQkWhuzqrbqQuqanFFJ6"))
  val s = tracklistAddWrites.writes(b).toString()
  println(s)
}
