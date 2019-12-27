package info.maaskant.jukebox

import info.maaskant.jukebox.Event.{Pause, Play, Resume}
import info.maaskant.jukebox.rfid.{Card, Uid}

//sealed trait Card
//
//object Card {
//
//  case class Album(spotifyUri: SpotifyUri) extends Card
//
//  case object Stop extends Card
//
//}

sealed trait State {
  def apply(input: Option[Card])(implicit cardMapping: Map[Uid, SpotifyUri]): (State, Option[Event])
}

object State {

  case object Stopped extends State {
    def apply(input: Option[Card])(implicit cardMapping: Map[Uid, SpotifyUri]): (State, Option[Event]) = input match {
      case None => (Stopped, None)
      case Some(card) => {
        cardMapping.get(card.uid) match {
          case None => (Stopped, None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), Some(Play(cardSpotifyUri)))
        }
      }
    }
  }

  case class Playing(currentUri: SpotifyUri) extends State {
    override def apply(input: Option[Card])(implicit cardMapping: Map[Uid, SpotifyUri]): (State, Option[Event]) = input match {
      case None => (Paused(currentUri), Some(Pause))
      case Some(card) => {
        cardMapping.get(card.uid) match {
          case None => (Playing(currentUri), None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), if (currentUri != cardSpotifyUri) Some(Play(cardSpotifyUri)) else None)
        }
      }

    }
  }

  case class Paused(lastUri: SpotifyUri) extends State {
    override def apply(input: Option[Card])(implicit cardMapping: Map[Uid, SpotifyUri]): (State, Option[Event]) = input match {
      case None => (Paused(lastUri), None)
      case Some(card) => {
        cardMapping.get(card.uid) match {
          case None => (Paused(lastUri), None)
          case Some(cardSpotifyUri) =>
            (Playing(cardSpotifyUri), if (lastUri != cardSpotifyUri) Some(Play(cardSpotifyUri)) else Some(Resume))
        }
      }
    }
  }

}

sealed trait Event

object Event {

  case class Play(uri: SpotifyUri) extends Event

  case object Pause extends Event

  case object Resume extends Event

}
