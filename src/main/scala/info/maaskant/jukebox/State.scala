package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.mopidy.MopidyUri

sealed trait State {
  def apply(input: Card): (State, Option[Action])
}

object State {

  case object Stopped extends State {
    override def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => this -> None
      case Card.Unknown => this -> None
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => this -> None
      case Album(mopidyUri, shuffle, repeat) => Playing(mopidyUri) -> Some(Play(mopidyUri, shuffle, repeat))
    }
  }

  case class Playing(currentUri: MopidyUri) extends State {
    override def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => Paused(currentUri) -> Some(Pause)
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case Card.Unknown => this -> None
      case Album(newUri, shuffle, repeat) =>
        if (currentUri == newUri) {
          this -> None
        } else {
          Playing(newUri) -> Some(Play(newUri, shuffle, repeat))
        }
    }
  }

  case class Paused(lastUri: MopidyUri) extends State {
    override def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => (Paused(lastUri), None)
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case Card.Unknown => this -> None
      case Album(newUri, shuffle, repeat) =>
        if (lastUri == newUri) {
          Playing(newUri) -> Some(Resume)
        } else {
          Playing(newUri) -> Some(Play(newUri, shuffle, repeat))
        }
    }
  }

}
