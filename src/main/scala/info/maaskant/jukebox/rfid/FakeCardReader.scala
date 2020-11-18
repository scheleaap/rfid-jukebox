package info.maaskant.jukebox.rfid

import com.typesafe.scalalogging.StrictLogging
import monix.reactive.Observable

class FixedUidReader(items: IndexedSeq[Option[Uid]]) extends CardReader with StrictLogging {
  def read(): Observable[Option[Card]] =
    Observable
      .from(items)
      .map { _.map(Card.apply(_, "Fake Card")) }
}

class TimeBasedReader extends CardReader with StrictLogging {
  def read(): Observable[Option[Card]] =
    Observable.repeatEval {
      val uid = System.currentTimeMillis / 1000 / 5
      Some(Card(Uid(s"fake-$uid"), "Fake Card"))
    }
}
