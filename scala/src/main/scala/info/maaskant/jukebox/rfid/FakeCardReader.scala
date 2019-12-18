package info.maaskant.jukebox.rfid

import cats.effect.{Resource, Sync}
import com.typesafe.scalalogging.StrictLogging

class FakeCardReader[F[_]](implicit F: Sync[F]) extends CardReader[F] with StrictLogging {
  def read(): F[Option[Card]] = F.delay {
    val uid = (System.currentTimeMillis / 1000 / 5)
    Some(Card(Uid(s"fake-$uid"), "Constant Fake Card"))
  }
}

object FakeCardReader {
  def resource[F[_]]()(implicit F: Sync[F]): Resource[F, FakeCardReader[F]] =
    Resource.pure(new FakeCardReader())
}
