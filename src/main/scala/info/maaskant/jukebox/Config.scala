package info.maaskant.jukebox

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import info.maaskant.jukebox.Config.{Album, Command, EventHooks, Mopidy, Spi}
import info.maaskant.jukebox.mopidy.MopidyUri
import info.maaskant.jukebox.rfid.Uid
import pureconfig.ConfigReader.Result
import pureconfig.ConvertHelpers._
import pureconfig.configurable._
import pureconfig.error.ConfigReaderException
import pureconfig.generic.auto._
import pureconfig.generic.semiauto._
import pureconfig.{ConfigCursor, ConfigReader, ConfigSource}

import java.net.URI
import scala.concurrent.duration.FiniteDuration

case class Config(
    mopidy: Mopidy,
    spi: Spi,
    readInterval: FiniteDuration,
    albums: Map[Uid, Either[MopidyUri, Album]],
    commands: Map[Uid, Command],
    hooks: Option[EventHooks]
)

object Config {

  case class Mopidy(
      baseUrl: URI
  )

  case class Spi(
      controller: Int,
      chipSelect: Int,
      resetGpio: Int
  )

  case class Album(uri: MopidyUri, repeat: Boolean, shuffle: Boolean)

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

  private implicit val commandConvert: ConfigReader[Command] = deriveEnumerationReader[Command]

  private implicit val albumMapReader: ConfigReader[Map[Uid, Either[MopidyUri, Album]]] =
    genericMapReader[Uid, Either[MopidyUri, Album]](catchReadError(Uid.apply))

  // Source: https://stackoverflow.com/a/61060575
  private implicit def albumEitherReader: ConfigReader[Either[MopidyUri, Album]] =
    new ConfigReader[Either[MopidyUri, Album]] {
      override def from(cur: ConfigCursor): Result[Either[MopidyUri, Album]] =
        ConfigReader[MopidyUri].from(cur).map(Left(_)) orElse ConfigReader[Album].from(cur).map(Right(_))
    }

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
