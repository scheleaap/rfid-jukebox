package info.maaskant.jukebox

import info.maaskant.jukebox.Event.{Pause, Play, Resume}
import info.maaskant.jukebox.State.{Paused, Playing, Stopped}
import info.maaskant.jukebox.rfid.{Card, Uid}

object StateMachine {

  def nextCard(state: State, maybeCard: Option[Card])(implicit cardMapping: Map[Uid, SpotifyUri]): (State, Option[Event]) = {
    (state, maybeCard) match {
      case (Stopped, None) => (Stopped, None)
      case (Stopped, Some(card)) => {
        cardMapping.get(card.uid) match {
          case None => (Stopped, None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), Some(Play(cardSpotifyUri)))
        }
      }
      case (Playing(currentUri), None) => (Paused(currentUri), Some(Pause))
      case (Playing(currentUri), Some(card)) => {
        cardMapping.get(card.uid) match {
          case None => (Playing(currentUri), None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), if (currentUri != cardSpotifyUri) Some(Play(cardSpotifyUri)) else None)
        }
      }
      case (Paused(lastUri), None) => (Paused(lastUri), None)
      case (Paused(lastUri), Some(card)) => {
        cardMapping.get(card.uid) match {
          case None => (Paused(lastUri), None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), if (lastUri != cardSpotifyUri) Some(Play(cardSpotifyUri)) else Some(Resume))
        }
      }
    }
  }
}


sealed trait State

object State {

  case object Stopped extends State

  case class Playing(currentUri: SpotifyUri) extends State

  case class Paused(lastUri: SpotifyUri) extends State

}

sealed trait Event

object Event {

  case class Play(uri: SpotifyUri) extends Event

  case object Pause extends Event

  case object Resume extends Event

}
