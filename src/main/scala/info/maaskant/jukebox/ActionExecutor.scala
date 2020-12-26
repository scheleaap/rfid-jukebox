package info.maaskant.jukebox

import cats.Parallel
import cats.effect.Sync
import cats.implicits.catsStdInstancesForList
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.parallel._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Process.runCommand
import info.maaskant.jukebox.mopidy.{MopidyClient, MopidyUri}

import scala.util.control.NonFatal

class ActionExecutor[F[_]: Sync: Parallel](eventHooks: Option[Config.EventHooks], mopidyClient: MopidyClient[F])
    extends StrictLogging {

  def executeAction(action: Action): F[Boolean] =
    (action match {
      case Action.Initialize => executeInitialize
      case Action.Pause => executePause
      case Action.Play(uri, shuffle, repeat) => executePlay(uri, shuffle, repeat)
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
    List(
      mopidyClient.pausePlayback(),
      eventHooks.flatMap(_.onPause).fold(Sync[F].unit)(runCommand[F](raiseError = false))
    ).parSequence
      .as(())

  private def executePlay(uri: MopidyUri, shuffle: Boolean, repeat: Boolean): F[Unit] = {
    List(
      List(
        mopidyClient.clearTracklist() >> mopidyClient.addToTracklist(Seq(uri.value)),
        mopidyClient.setShuffle(shuffle)
      ).parSequence >> mopidyClient.startPlayback(),
      mopidyClient.setRepeat(repeat),
      eventHooks.flatMap(_.onPlay).fold(Sync[F].unit)(runCommand[F](raiseError = false))
    ).parSequence
      .as(())
  }

  private def executeResume: F[Unit] =
    List(
      mopidyClient.resumePlayback(),
      eventHooks.flatMap(_.onResume).fold(Sync[F].unit)(runCommand[F](raiseError = false))
    ).parSequence
      .as(())

  private def executeShutdown: F[Unit] =
    eventHooks.flatMap(_.onShutdown).fold(Sync[F].unit)(runCommand[F](raiseError = false)) >>
      runCommand(raiseError = true)("sudo shutdown now")

  private def executeInitialize: F[Unit] =
    eventHooks.flatMap(_.onInitialize).fold(Sync[F].unit)(runCommand[F](raiseError = false))

  private def executeStop: F[Unit] =
    List(
      mopidyClient.stopPlayback() >>
        mopidyClient.clearTracklist(),
      eventHooks.flatMap(_.onStop).fold(Sync[F].unit)(runCommand[F](raiseError = false))
    ).parSequence
      .as(())
}
