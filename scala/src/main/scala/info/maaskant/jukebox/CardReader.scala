package info.maaskant.jukebox

trait CardReader {
  def read(): Option[Card]
}
