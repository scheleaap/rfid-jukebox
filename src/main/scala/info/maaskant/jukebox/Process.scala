package info.maaskant.jukebox

import cats.effect.Sync
import cats.syntax.flatMap._
import com.typesafe.scalalogging.StrictLogging

import scala.sys.process._

object Process extends StrictLogging {
  def runCommand[F[_]: Sync](raiseError: Boolean)(command: String): F[Unit] = Sync[F]
    .delay {
      logger.debug(s"Executing local command $command")
      command.!
    }
    .flatMap { exitCode =>
      if (exitCode == 0) {
        Sync[F].delay(logger.debug(s"Successfully executed local command $command"))
      } else {
        val message = s"Local command $command returned exit code $exitCode"
        if (raiseError) {
          Sync[F].raiseError(new RuntimeException(message))
        } else {
          Sync[F].delay(logger.warn(message))
        }
      }
    }
}
