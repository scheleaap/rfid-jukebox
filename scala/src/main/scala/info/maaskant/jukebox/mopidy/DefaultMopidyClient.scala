package info.maaskant.jukebox.mopidy

import cats.effect.Sync
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.mopidy.JsonRpcWrites.tracklistAddWrites
import play.api.libs.json.JsObject
import sttp.client._
import sttp.client.playJson._
import sttp.model.Uri

class DefaultMopidyClient[F[_]] private(rpcEndpoint: Uri)(implicit F: Sync[F]) extends MopidyClient[F] with StrictLogging {
  //  private implicit val backend = HttpURLConnectionBackend()
  private implicit val sttpBackend: SttpBackend[F, Nothing, NothingT] = ???

  override def addToTracklist(uris: Seq[String]): F[Boolean] = {
    import cats.syntax.functor._
    val json: JsObject = tracklistAddWrites.writes(TracklistAdd(uris))
    for {
      _ <- basicRequest
        .post(rpcEndpoint)
        .body(json)
        .send()
    } yield true
  }
}

object DefaultMopidyClient extends StrictLogging {
  def apply[F[_] : Sync](rpcEndpoint: Uri): DefaultMopidyClient[F] =
    new DefaultMopidyClient[F](rpcEndpoint)
}
