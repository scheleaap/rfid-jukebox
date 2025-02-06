package info.maaskant.jukebox.rfid

import cats.effect.IO

trait CardReader {
  def read(): IO[Option[Card]]

  def close(): IO[Unit]
}
