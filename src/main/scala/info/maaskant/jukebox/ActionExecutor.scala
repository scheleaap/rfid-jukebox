package info.maaskant.jukebox

import cats.Parallel
import cats.effect.Sync
import cats.implicits.catsStdInstancesForList
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.parallel._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.{MopidyClient, MopidyUri}
import info.maaskant.jukebox.Process.runCommand
import scala.util.control.NonFatal

class ActionExecutor[F[_]: Sync: Parallel](eventHooks: Option[EventHooks], mopidyClient: MopidyClient[F])
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

  private def executePlay(uri: MopidyUri): F[Unit] = {
    List(
      eventHooks.flatMap(_.onPlay).fold(Sync[F].unit)(runCommand[F](raiseError = false)),
      mopidyClient.clearTracklist() >>
        mopidyClient.addToTracklist(Seq(uri.value)) >>
        mopidyClient.startPlayback()
    ).parSequence
      .map(_ => ())
  }

  private def executeResume: F[Unit] =
    mopidyClient.resumePlayback()

  private def executeShutdown: F[Unit] =
    runCommand(raiseError = true)("sudo shutdown now")

  private def executeInitialize: F[Unit] =
    eventHooks.flatMap(_.onInitialize).fold(Sync[F].unit)(runCommand[F](raiseError = false))

  private def executeStop: F[Unit] =
    mopidyClient.stopPlayback() >>
      mopidyClient.clearTracklist()
}
