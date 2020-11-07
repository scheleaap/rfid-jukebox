package info.maaskant.jukebox

import cats.effect.Sync
import com.typesafe.scalalogging.StrictLogging

object Actions extends StrictLogging {
  def executeAction[F[_]: Sync](action: Action): F[Boolean] =
//    (action match {
//      case Action.Pause => executePause
//      case Action.Play(uri) => executePlay(uri)
//      case Action.Resume => executeResume
//      case Action.Shutdown => executeShutdown
//      case Action.Stop => executeStop
//    }).map(_ => true).recoverWith {
//      case NonFatal(t) =>
//        Sync[F].delay {
//          logger.warn(s"Could not execute action $action: ${t.getLocalizedMessage}")
//          false
//        }
//    }
  Sync[F].pure(true)

//  private def executeShutdown[F[_]](implicit F: Sync[F]): F[Unit] = F.delay {
//    val command = "sudo shutdown now"
//    logger.debug(s"Executing local command $command")
//
//    val exitCode = command.!
//    if (exitCode != 0) {
//      F.raiseError(new RuntimeException(s"$command returned exit code $exitCode"))
//    }
//  }
}
