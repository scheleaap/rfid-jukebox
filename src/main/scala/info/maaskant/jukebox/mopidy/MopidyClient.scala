package info.maaskant.jukebox.mopidy

import cats.effect.IO

trait MopidyClient {
  def addToTracklist(uris: Seq[String]): IO[Unit]

  def clearTracklist(): IO[Unit]

  def pausePlayback(): IO[Unit]

  def resumePlayback(): IO[Unit]

  def startPlayback(): IO[Unit]

  def stopPlayback(): IO[Unit]
}
