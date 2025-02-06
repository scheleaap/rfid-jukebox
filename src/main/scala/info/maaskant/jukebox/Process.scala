package info.maaskant.jukebox

import cats.effect.IO
import com.typesafe.scalalogging.StrictLogging

import scala.sys.process._

object Process extends StrictLogging {
  def runCommand(raiseError: Boolean)(command: String): IO[Unit] = IO.blocking {
    logger.debug(s"Executing local command $command")
    command.!
  }.flatMap { exitCode =>
    if (exitCode == 0) {
      IO(logger.debug(s"Successfully executed local command $command"))
    } else {
      val message = s"Local command $command returned exit code $exitCode"
      if (raiseError) {
        IO.raiseError(new RuntimeException(message))
      } else {
        IO(logger.warn(message))
      }
    }
  }
}
