package info.maaskant.jukebox.mopidy

import play.api.libs.json._

object JsonRpcWrites {
  private def jsonRpc(method: String, params: JsValue = Json.obj()): JsObject = Json.obj(
    "jsonrpc" -> "2.0",
    "id" -> 1,
    "method" -> method,
    "params" -> params
  )

  implicit val addToTracklistWrites: Writes[AddToTracklist] = (value: AddToTracklist) =>
    jsonRpc("core.tracklist.add", Json.obj("uris" -> value.uris))

  implicit val clearTracklistWrites: Writes[ClearTracklist.type] = (_: ClearTracklist.type) =>
    jsonRpc("core.tracklist.clear")

  implicit val pausePlaybackWrites: Writes[PausePlayback.type] = (_: PausePlayback.type) =>
    jsonRpc("core.playback.pause")

  implicit val resumePlaybackWrites: Writes[ResumePlayback.type] = (_: ResumePlayback.type) =>
    jsonRpc("core.playback.resume")

  implicit val startPlaybackWrites: Writes[StartPlayback.type] = (_: StartPlayback.type) =>
    jsonRpc("core.playback.play")

  implicit val stopPlaybackWrites: Writes[StopPlayback.type] = (_: StopPlayback.type) => jsonRpc("core.playback.stop")

}
