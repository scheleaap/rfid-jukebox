package info.maaskant.jukebox.rfid

import info.maaskant.jukebox.rfid.CardReader.ReadError
import monix.reactive.Observable

trait CardReader {
  def read(): Observable[Either[ReadError, Option[Card]]]
}

object CardReader {

  sealed trait ReadError

  object ReadError {
    case object TemporaryError extends ReadError
    case object PermanentError extends ReadError
    case object UnknownError extends ReadError
  }
}
