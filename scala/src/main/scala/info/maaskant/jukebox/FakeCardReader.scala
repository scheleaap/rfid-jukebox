package info.maaskant.jukebox

import cats.effect.{Resource, Sync}
import com.typesafe.scalalogging.StrictLogging

class FakeCardReader extends CardReader with StrictLogging {
  def read(): Option[Card] = {
    val uid = (System.currentTimeMillis / 1000 / 5)
    Some(Card(s"fake-$uid", "Constant Fake Card"))
  }

}

object FakeCardReader {
  def resource[F[_]]()(implicit F: Sync[F]): Resource[F, FakeCardReader] =
    Resource.pure(new FakeCardReader())
}
