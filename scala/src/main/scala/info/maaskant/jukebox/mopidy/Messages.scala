package info.maaskant.jukebox.mopidy

sealed trait Message

case class TracklistAdd(uris: Seq[String]) extends Message
