package info.maaskant.jukebox.rfid

import cats.effect.IO
import com.typesafe.scalalogging.StrictLogging

class FixedUidReader(uidOption: Option[Uid]) extends CardReader with StrictLogging {
  def read(): IO[Option[Card]] =
    IO.pure(uidOption.map(Card(_, "Fake Card")))

  override def close(): IO[Unit] =
    IO.unit
}

class TimeBasedReader extends CardReader with StrictLogging {
  def read(): IO[Option[Card]] =
    IO(System.currentTimeMillis).map(i => {
      val uid = i / 1000 / 5
      Some(Card(Uid(s"fake-$uid"), "Fake Card"))
    })

  override def close(): IO[Unit] =
    IO.unit
}
