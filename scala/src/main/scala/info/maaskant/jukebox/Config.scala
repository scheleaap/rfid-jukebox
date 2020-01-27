package info.maaskant.jukebox
import java.io.IOException
import java.net.URI

import cats.effect.Sync
import cats.syntax.flatMap._
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._
import pureconfig.{ConfigReader, ConfigSource, _}

import scala.concurrent.duration.FiniteDuration

object Config {
  private def load(): ConfigReader.Result[Config] = {
    val devFile = ConfigSource.file("/home/wout/dev/music-album-loader/scala/etc.conf").optional
    val etcFile = ConfigSource.file("/etc/rfid-jukebox.conf").optional

    devFile
      .withFallback(etcFile)
      .withFallback(ConfigSource.default)
      .load[Config]
  }

  def loadF[F[_]]()(implicit F: Sync[F]): F[Config] = F.delay(load()).flatMap {
    case Left(ConfigReaderFailures(h, t)) => {
      val errorDescription: String = (h :: t)
        .map(f =>
          f.location match {
            case None => s"${f.description}"
            case Some(location) => s"${f.description} @ $location"
          }
        )
        .mkString("\n  ", "\n  ", "\n")
      val message = s"Error while reading configuration: [$errorDescription]"
      F.raiseError(new IOException(message))
    }
    case Right(config) => F.pure(config)
  }
}

case class Config(
    mopidy: Mopidy,
    spi: Spi,
    readInterval: FiniteDuration
)

case class Mopidy(
    baseUrl: URI
)

case class Spi(
    controller: Int,
    chipSelect: Int,
    resetGpio: Int
)
