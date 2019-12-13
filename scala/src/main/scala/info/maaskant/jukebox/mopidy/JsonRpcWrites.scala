package info.maaskant.jukebox.mopidy

import play.api.libs.json._

object JsonRpcWrites {
  implicit val tracklistAddWrites = new Writes[TracklistAdd] {
    def writes(value: TracklistAdd): JsObject = Json.obj(
      "jsonrpc" -> "2.0",
      "id" -> 1,
      "method" -> "core.tracklist.add",
      "params" -> Json.obj(
        "uris" -> value.uris
      )
    )
  }
}
