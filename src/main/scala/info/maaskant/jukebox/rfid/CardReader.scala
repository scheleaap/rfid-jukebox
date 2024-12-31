package info.maaskant.jukebox.rfid

import cats.effect.{IO, Resource}

trait CardReader[R] {
  def resource(): Resource[IO, R]

  def read(resources: R): IO[Option[Card]]
}
