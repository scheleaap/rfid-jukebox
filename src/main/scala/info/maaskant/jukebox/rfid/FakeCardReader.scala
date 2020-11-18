package info.maaskant.jukebox.rfid

import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.rfid.CardReader.ReadError
import monix.reactive.Observable

class FixedUidReader(items: IndexedSeq[Option[Uid]]) extends CardReader with StrictLogging {
  def read(): Observable[Either[ReadError, Option[Card]]] =
    Observable
      .from(items)
      .map { _.map(Card.apply(_, "Fake Card")) }
      .map { Right(_) }
}

class TimeBasedReader extends CardReader with StrictLogging {
  def read(): Observable[Either[ReadError, Option[Card]]] =
    Observable.repeatEval {
      val uid = System.currentTimeMillis / 1000 / 5
      Right(Some(Card(Uid(s"fake-$uid"), "Fake Card")))
    }
}
