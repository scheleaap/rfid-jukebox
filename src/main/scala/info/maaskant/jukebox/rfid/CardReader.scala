package info.maaskant.jukebox.rfid

import monix.reactive.Observable

trait CardReader {
  def read(): Observable[Option[Card]]
}
