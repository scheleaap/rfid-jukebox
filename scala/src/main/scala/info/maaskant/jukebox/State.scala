package info.maaskant.jukebox

import cats.Eq
import info.maaskant.jukebox.Action.{Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album

sealed trait Card

object Card {
  implicit val cardEq: Eq[Card] = (x: Card, y: Card) => ??? // x == y

  case object None extends Card

  case object Unknown extends Card

  case class Album(spotifyUri: SpotifyUri) extends Card

  case object Stop extends Card

}

sealed trait State {
  def apply(input: Card): (State, Option[Action])
}

object State {

  case object Stopped extends State {
    def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => this -> None
      case Card.Unknown => this -> None
      case Card.Stop => this -> None
      case Album(spotifyUri) => Playing(spotifyUri) -> Some(Play(spotifyUri))
    }
  }

  case class Playing(currentUri: SpotifyUri) extends State {
    override def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => Paused(currentUri) -> Some(Pause)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case Card.Unknown => this -> None
      case Album(newUri) => if (currentUri == newUri) {
        this -> None
      } else {
        Playing(newUri) -> Some(Play(newUri))
      }
    }
  }

  case class Paused(lastUri: SpotifyUri) extends State {
    override def apply(input: Card): (State, Option[Action]) = input match {
      case Card.None => (Paused(lastUri), None)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case Card.Unknown => this -> None
      case Album(newUri) => if (lastUri == newUri) {
        Playing(newUri) -> Some(Resume)
      } else {
        Playing(newUri) -> Some(Play(newUri))
      }
    }
  }

}

sealed trait Action

object Action {

  case object Pause extends Action

  case class Play(uri: SpotifyUri) extends Action

  case object Resume extends Action

  case object Stop extends Action

}
