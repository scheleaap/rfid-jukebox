package info.maaskant.jukebox.mopidy

trait MopidyClient[F[_]] {
  def addToTracklist(uris: Seq[String]): F[Boolean]
}
