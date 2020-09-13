package info.maaskant.jukebox

import cats.Parallel
import cats.effect.Sync
import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.{MopidyClient, MopidyUri}

import scala.sys.process._
import scala.util.control.NonFatal

object Actions extends StrictLogging {
  def executeAction[F[_]: Sync: Parallel](action: Action)(implicit mopidyClient: MopidyClient[F]): F[Boolean] =
    (action match {
      case Action.Pause => executePause
      case Action.Play(uri, shuffle, repeat) => executePlay(uri, shuffle, repeat)
      case Action.Resume => executeResume
      case Action.Shutdown => executeShutdown
      case Action.Stop => executeStop
    }).map(_ => true).recoverWith {
      case NonFatal(t) =>
        Sync[F].delay {
          logger.warn(s"Could not execute action $action: ${t.getLocalizedMessage}")
          false
        }
    }

  private def executePause[F[_]: Sync](implicit mopidyClient: MopidyClient[F]): F[Unit] =
    mopidyClient.pausePlayback()

  private def executePlay[F[_]: Sync: Parallel](uri: MopidyUri, shuffle: Boolean, repeat: Boolean)(
      implicit mopidyClient: MopidyClient[F]
  ): F[Unit] = {
    List(
      List(
        mopidyClient.clearTracklist() >> mopidyClient.addToTracklist(Seq(uri.value)),
        mopidyClient.setShuffle(shuffle)
      ).parSequence >> mopidyClient.startPlayback(),
      mopidyClient.setRepeat(repeat)
    ).parSequence
      .map(_ => ())
  }

  private def executeResume[F[_]: Sync](implicit mopidyClient: MopidyClient[F]) =
    mopidyClient.resumePlayback()

  private def executeShutdown[F[_]](implicit F: Sync[F]): F[Unit] = F.delay {
    val command = "sudo shutdown now"
    logger.debug(s"Executing local command $command")

    val exitCode = command.!
    if (exitCode != 0) {
      F.raiseError(new RuntimeException(s"$command returned exit code $exitCode"))
    }
  }

  private def executeStop[F[_]: Sync](implicit mopidyClient: MopidyClient[F]): F[Unit] =
    mopidyClient.stopPlayback() >>
      mopidyClient.clearTracklist()
}
