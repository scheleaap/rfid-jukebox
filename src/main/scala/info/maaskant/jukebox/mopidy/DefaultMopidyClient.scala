package info.maaskant.jukebox.mopidy

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.JsonRpcWrites._
import sttp.client._
import sttp.client.playJson._
import sttp.model.Uri

class DefaultMopidyClient[F[_]] private (rpcEndpoint: Uri)(
    implicit F: Sync[F],
    sttpBackend: SttpBackend[F, Nothing, NothingT]
) extends MopidyClient[F]
    with StrictLogging {

  private def send[T: BodySerializer](body: T): F[Unit] =
    basicRequest
      .post(rpcEndpoint)
      .body(body)
      .send()
      .map(response => {
        if (!response.isSuccess) {
          logger.warn(
            s"Call to $rpcEndpoint with $body failed: " +
              s"${response.code} ${response.body}"
          )
        }
        ()
      })

  override def addToTracklist(uris: Seq[String]): F[Unit] =
    F.delay(logger.debug(s"Adding to tracklist: $uris")) >>
      send(AddToTracklist(uris))

  override def clearTracklist(): F[Unit] =
    F.delay(logger.debug("Clearing tracklist")) >>
      send(ClearTracklist)

  override def pausePlayback(): F[Unit] =
    F.delay(logger.debug("Pausing playback")) >>
      send(PausePlayback)

  override def resumePlayback(): F[Unit] =
    F.delay(logger.debug("Resuming playback")) >>
      send(ResumePlayback)

  override def startPlayback(): F[Unit] =
    F.delay(logger.debug("Starting playback")) >>
      send(StartPlayback)

  override def stopPlayback(): F[Unit] =
    F.delay(logger.debug("Stopping playback")) >>
      send(StopPlayback)

}

object DefaultMopidyClient extends StrictLogging {
  def apply[F[_]](
      rpcEndpoint: Uri
  )(implicit F: Sync[F], sttpBackend: SttpBackend[F, Nothing, NothingT]): DefaultMopidyClient[F] =
    new DefaultMopidyClient[F](rpcEndpoint)
}
