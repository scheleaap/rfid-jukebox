package info.maaskant.jukebox.mopidy

import play.api.libs.json._

object JsonRpcWrites {
  private def rpcJs(method: String, params: JsValue = JsNull): JsObject = Json.obj(
    "jsonrpc" -> "2.0",
    "id" -> 1,
    "method" -> method,
    "params" -> params
  )

  implicit val addToTracklistWrites: Writes[AddToTracklist] = (value: AddToTracklist) =>
    rpcJs("core.tracklist.add", Json.obj("uris" -> value.uris))

  implicit val clearTracklistWrites: Writes[ClearTracklist.type] = (value: ClearTracklist.type) =>
    rpcJs("core.tracklist.clear")

  implicit val pausePlaybackWrites: Writes[PausePlayback.type] = (value: PausePlayback.type) =>
    rpcJs("core.playback.pause")

  implicit val resumePlaybackWrites: Writes[ResumePlayback.type] = (value: ResumePlayback.type) =>
    rpcJs("core.playback.resume")

  implicit val startPlaybackWrites: Writes[StartPlayback.type] = (value: StartPlayback.type) =>
    rpcJs("core.playback.play")

  implicit val stopPlaybackWrites: Writes[StopPlayback.type] = (value: StopPlayback.type) =>
    rpcJs("core.playback.stop")

}
