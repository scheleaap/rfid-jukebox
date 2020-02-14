package info.maaskant.jukebox.rfid

import cats.effect.{Resource, Sync}
import com.typesafe.scalalogging.StrictLogging

trait FakeCardReader[F[_]] extends CardReader[F]

class FixedUidReader[F[_]](items: IndexedSeq[Option[Uid]])(implicit F: Sync[F]) extends FakeCardReader[F] with StrictLogging {
  private var i = 0

  def read(): F[Option[Card]] = F.delay {
    if (i < items.size) {
      val result = items(i).map(Card.apply(_, "Fake Card"))
      i = (i + 1) //% items.size
      result
    } else {
      None
    }
  }
}

class TimeBasedReader[F[_]](implicit F: Sync[F]) extends FakeCardReader[F] with StrictLogging {
  def read(): F[Option[Card]] = F.delay {
    val uid = (System.currentTimeMillis / 1000 / 5)
    Some(Card(Uid(s"fake-$uid"), "Fake Card"))
  }
}

object FakeCardReader {
  def resource[F[_]](fakeCardReader: FakeCardReader[F])(implicit F: Sync[F]): Resource[F, FakeCardReader[F]] =
    Resource.pure(fakeCardReader)
}
