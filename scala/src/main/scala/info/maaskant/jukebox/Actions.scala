package info.maaskant.jukebox

import cats.effect.Sync
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.MopidyClient

import scala.util.control.NonFatal

object Actions extends StrictLogging {
  def executeAction[F[_]: Sync](action: Action)(implicit mopidyClient: MopidyClient[F]): F[Boolean] =
    (action match {
      case Action.Stop => executeStop
      case Action.Play(uri) => executePlay(uri)
      case Action.Pause => executePause
      case Action.Resume => executeResume
    }).map(_ => true).recoverWith {
      case NonFatal(t) =>
        Sync[F].delay {
          logger.warn(s"Could not execute action $action: ${t.getLocalizedMessage}")
          false
        }
    }

  private def executeResume[F[_]: Sync](implicit mopidyClient: MopidyClient[F]) =
    mopidyClient.resumePlayback()

  private def executePause[F[_]: Sync](implicit mopidyClient: MopidyClient[F]): F[Unit] =
    mopidyClient.pausePlayback()

  private def executePlay[F[_]: Sync](uri: SpotifyUri)(implicit mopidyClient: MopidyClient[F]): F[Unit] =
    mopidyClient.clearTracklist() >>
      mopidyClient.addToTracklist(Seq(uri.value)) >>
      mopidyClient.startPlayback()

  private def executeStop[F[_]: Sync](implicit mopidyClient: MopidyClient[F]): F[Unit] =
    mopidyClient.stopPlayback() >>
      mopidyClient.clearTracklist()
}
