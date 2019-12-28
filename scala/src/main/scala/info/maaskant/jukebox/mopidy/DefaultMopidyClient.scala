package info.maaskant.jukebox.mopidy

import cats.effect.Sync
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.JsonRpcWrites.tracklistAddWrites
import play.api.libs.json.JsObject
import sttp.client._
import sttp.client.playJson._
import sttp.model.Uri

class DefaultMopidyClient[F[_]] private(rpcEndpoint: Uri)(implicit F: Sync[F], sttpBackend: SttpBackend[F, Nothing, NothingT])
  extends MopidyClient[F] with StrictLogging {

  override def addToTracklist(uris: Seq[String]): F[Unit] = {
    val json: JsObject = tracklistAddWrites.writes(TracklistAdd(uris))

    import cats.syntax.functor._
    basicRequest
      .post(rpcEndpoint)
      .body(json)
      .send()
      .map(_ => ())
  }

  override def clearTracklist(): F[Unit] = ???

  override def pausePlayback(): F[Unit] = ???

  override def resumePlayback(): F[Unit] = ???

  override def startPlayback(): F[Unit] = ???

  override def stopPlayback(): F[Unit] = ???
}

object DefaultMopidyClient extends StrictLogging {
  def apply[F[_]](rpcEndpoint: Uri)(implicit F: Sync[F], sttpBackend: SttpBackend[F, Nothing, NothingT]): DefaultMopidyClient[F] =
    new DefaultMopidyClient[F](rpcEndpoint)
}
