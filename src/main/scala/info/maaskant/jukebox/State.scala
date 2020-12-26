package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Pause, Play, Resume, Initialize}
import info.maaskant.jukebox.mopidy.MopidyUri

case class State(playback: PlaybackState) {
  def apply(input: Card): (State, Option[Action]) = {
    val (playbackNew, playbackAction): (PlaybackState, Option[Action]) = playback.apply(input)
    State(playbackNew) -> playbackAction
  }
}

sealed trait PlaybackState {
  def apply(input: Card): (PlaybackState, Option[Action])
}

object PlaybackState {

  case object Uninitialized extends PlaybackState {
    override def apply(input: Card): (PlaybackState, Option[Action]) = Stopped -> Some(Initialize)
  }

  case object Stopped extends PlaybackState {
    override def apply(input: Card): (PlaybackState, Option[Action]) = input match {
      case Card.None => this -> None
      case _: Card.Unknown => this -> None
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => this -> None
      case Card.Album(mopidyUri, shuffle, repeat) => Playing(mopidyUri) -> Some(Play(mopidyUri, shuffle, repeat))
    }
  }

  case class Playing(currentUri: MopidyUri) extends PlaybackState {
    override def apply(input: Card): (PlaybackState, Option[Action]) = input match {
      case Card.None => Paused(currentUri) -> Some(Pause)
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case _: Card.Unknown => this -> None
      case Card.Album(newUri, shuffle, repeat) =>
        if (currentUri == newUri) {
          this -> None
        } else {
          Playing(newUri) -> Some(Play(newUri, shuffle, repeat))
        }
    }
  }

  case class Paused(lastUri: MopidyUri) extends PlaybackState {
    override def apply(input: Card): (PlaybackState, Option[Action]) = input match {
      case Card.None => (Paused(lastUri), None)
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case _: Card.Unknown => this -> None
      case Card.Album(newUri, shuffle, repeat) =>
        if (lastUri == newUri) {
          Playing(newUri) -> Some(Resume)
        } else {
          Playing(newUri) -> Some(Play(newUri, shuffle, repeat))
        }
    }
  }

}
