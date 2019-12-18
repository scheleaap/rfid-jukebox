package info.maaskant.jukebox.rfid

trait CardReader[F[_]] {
  def read(): F[Option[Card]]
}
