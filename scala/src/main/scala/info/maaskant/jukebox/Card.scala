package info.maaskant.jukebox

import cats.Eq
import info.maaskant.jukebox.mopidy.MopidyUri

sealed trait Card

object Card {
  implicit val eq: Eq[Card] = (x: Card, y: Card) => x == y

  case object None extends Card

  case object Unknown extends Card

  case class Album(spotifyUri: MopidyUri) extends Card

  case object Stop extends Card

}
