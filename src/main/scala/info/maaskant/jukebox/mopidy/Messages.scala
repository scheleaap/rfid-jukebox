package info.maaskant.jukebox.mopidy

sealed trait Message

case class AddToTracklist(uris: Seq[String]) extends Message

case object ClearTracklist extends Message

case object PausePlayback extends Message

case object ResumePlayback extends Message

case object StartPlayback extends Message

case object StopPlayback extends Message
