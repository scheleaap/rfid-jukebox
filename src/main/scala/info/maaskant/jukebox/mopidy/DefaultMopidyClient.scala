package info.maaskant.jukebox.mopidy

import cats.effect.IO
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.JsonRpcWrites._
import sttp.client3.playJson._
import sttp.client3.{SttpBackend, _}
import sttp.model.Uri

class DefaultMopidyClient private (rpcEndpoint: Uri)(implicit sttpBackend: SttpBackend[IO, _])
    extends MopidyClient
    with StrictLogging {

  private def send[T: BodySerializer](body: T): IO[Unit] =
    basicRequest
      .post(rpcEndpoint)
      .body(body)
      .contentType("application/json")
      .send(sttpBackend)
      .map(response => {
        if (!response.isSuccess) {
          logger.warn(
            s"Call to $rpcEndpoint with $body failed: " +
              s"${response.code} ${response.body}"
          )
        }
        ()
      })

  override def addToTracklist(uris: Seq[String]): IO[Unit] =
    IO(logger.debug(s"Adding to tracklist: $uris")) >>
      send(AddToTracklist(uris))

  override def clearTracklist(): IO[Unit] =
    IO(logger.debug("Clearing tracklist")) >>
      send(ClearTracklist)

  override def pausePlayback(): IO[Unit] =
    IO(logger.debug("Pausing playback")) >>
      send(PausePlayback)

  override def resumePlayback(): IO[Unit] =
    IO(logger.debug("Resuming playback")) >>
      send(ResumePlayback)

  override def startPlayback(): IO[Unit] =
    IO(logger.debug("Starting playback")) >>
      send(StartPlayback)

  override def stopPlayback(): IO[Unit] =
    IO(logger.debug("Stopping playback")) >>
      send(StopPlayback)

}

object DefaultMopidyClient extends StrictLogging {
  def apply(rpcEndpoint: Uri)(implicit sttpBackend: SttpBackend[IO, _]): DefaultMopidyClient =
    new DefaultMopidyClient(rpcEndpoint)(sttpBackend)
}
