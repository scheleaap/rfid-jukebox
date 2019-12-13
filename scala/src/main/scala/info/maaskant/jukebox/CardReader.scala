package info.maaskant.jukebox

trait CardReader[F[_]] {
  def read(): F[Option[Card]]
}
