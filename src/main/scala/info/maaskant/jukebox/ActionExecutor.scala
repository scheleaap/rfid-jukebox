package info.maaskant.jukebox

import cats.effect.Sync
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.{MopidyClient, MopidyUri}

import scala.sys.process._
import scala.util.control.NonFatal

class ActionExecutor[F[_]](eventHooks: Option[EventHooks], mopidyClient: MopidyClient[F])(implicit F: Sync[F])
    extends StrictLogging {

  def executeAction(action: Action): F[Boolean] =
    (action match {
      case Action.Initialize => executeInitialize
      case Action.Pause => executePause
      case Action.Play(uri) => executePlay(uri)
      case Action.Resume => executeResume
      case Action.Shutdown => executeShutdown
      case Action.Stop => executeStop
    }).map(_ => true).recoverWith { case NonFatal(t) =>
      Sync[F].delay {
        logger.warn(s"Could not execute action $action: ${t.getLocalizedMessage}")
        false
      }
    }

  private def executePause: F[Unit] =
    mopidyClient.pausePlayback()

  private def executePlay(uri: MopidyUri): F[Unit] =
    mopidyClient.clearTracklist() >>
      mopidyClient.addToTracklist(Seq(uri.value)) >>
      mopidyClient.startPlayback()

  private def executeResume: F[Unit] =
    mopidyClient.resumePlayback()

  private def executeShutdown: F[Unit] = F.delay {
    val command = "sudo shutdown now"
    logger.debug(s"Executing local command $command")

    val exitCode = command.!
    if (exitCode != 0) {
      F.raiseError(new RuntimeException(s"$command returned exit code $exitCode"))
    }
  }

  private def executeInitialize: F[Unit] = {
    eventHooks.flatMap(_.onStartup).fold(F.unit) { command =>
      F.delay {
        logger.debug(s"Executing local command $command")
        val exitCode = command.!
        if (exitCode != 0) {
          logger.warn(s"Local command $command returned exit code $exitCode")
        } else {
          logger.debug(s"Successfully executed local command $command")
        }
      }
    }
  }

  private def executeStop: F[Unit] =
    mopidyClient.stopPlayback() >>
      mopidyClient.clearTracklist()
}
