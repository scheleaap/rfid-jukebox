package info.maaskant.jukebox.mopidy

trait MopidyClient[F[_]] {
  def addToTracklist(uris: Seq[String]): F[Unit]

  def clearTracklist(): F[Unit]

  def pausePlayback(): F[Unit]

  def resumePlayback(): F[Unit]

  def setRepeat(enable: Boolean): F[Unit]

  def setShuffle(enable: Boolean): F[Unit]

  def startPlayback(): F[Unit]

  def stopPlayback(): F[Unit]
}
