package info.maaskant.jukebox

import java.net.URI

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import info.maaskant.jukebox.mopidy.MopidyUri
import info.maaskant.jukebox.rfid.Uid
import pureconfig.ConvertHelpers._
import pureconfig.configurable._
import pureconfig.error.ConfigReaderException
import pureconfig.generic.auto._
import pureconfig.generic.semiauto._
import pureconfig.{ConfigReader, ConfigSource}

import scala.concurrent.duration.FiniteDuration

object Config {
  private implicit val commandConvert: ConfigReader[Command] = deriveEnumerationReader[Command]

  private implicit val albumMapReader: ConfigReader[Map[Uid, MopidyUri]] =
    genericMapReader[Uid, MopidyUri](catchReadError(Uid.apply))

  private implicit val commandMapReader: ConfigReader[Map[Uid, Command]] =
    genericMapReader[Uid, Command](catchReadError(Uid.apply))

  private def load(): ConfigReader.Result[Config] = {
    val devFile = ConfigSource.file("/home/wout/dev/music-album-loader/scala/etc.conf").optional
    val etcFile = ConfigSource.file("/etc/rfid-jukebox.conf").optional

    devFile
      .withFallback(etcFile)
      .withFallback(ConfigSource.default)
      .load[Config]
  }

  def loadF[F[_]]()(implicit F: Sync[F]): F[Config] =
    for {
      i <- F.delay(load().left.map(ConfigReaderException(_)))
      j <- F.fromEither(i)
    } yield j
}

case class Config(
    mopidy: Mopidy,
    reader: String,
    readInterval: FiniteDuration,
    spi: Spi,
    albums: Map[Uid, MopidyUri],
    commands: Map[Uid, Command],
    hooks: Option[EventHooks]
)

case class Mopidy(
    baseUrl: URI
)

case class Spi(
    controller: Int,
    chipSelect: Int,
    resetGpio: Int
)

sealed trait Command

object Command {
  case object Shutdown extends Command
  case object Stop extends Command
}

case class EventHooks(
    onCardChange: Option[String],
    onInitialize: Option[String],
    onPause: Option[String],
    onPlay: Option[String],
    onResume: Option[String],
    onShutdown: Option[String],
    onStop: Option[String]
)
