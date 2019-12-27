package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.State.{Paused, Playing, Stopped}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StateMachineTest extends AnyFlatSpec with Matchers {
  private val card1 = Album(SpotifyUri("card1"))
  private val card2 = Album(SpotifyUri("card2"))

  "Stopped, nothing" should "Stopped, None" in {
    Stopped(Card.None) should be(Stopped, None)
  }

  "Stopped, album" should "Playing, Play" in {
    Stopped(card1) should be(playing(card1), play(card1))
  }

  "Stopped, unknown card" should "Stopped, None" in {
    Stopped(Card.Unknown) should be(Stopped, None)
  }

  "Stopped, stop" should "Stopped, None" in {
    Stopped(Card.Stop) should be(Stopped, None)
  }

  "Playing, nothing" should "Paused, Pause" in {
    playing(card1)(Card.None) should be(paused(card1), pause)
  }

  "Playing, same card" should "Playing, None" in {
    playing(card1)(card1) should be(playing(card1), None)
  }

  "Playing, different card" should "Playing, Play" in {
    playing(card1)(card2) should be(playing(card2), play(card2))
  }

  "Playing, unknown card" should "Playing, None" in {
    playing(card1)(Card.Unknown) should be(playing(card1), None)
  }

  "Playing, stop" should "Stopped, Stop" in {
    playing(card1)(Card.Stop) should be(Stopped, stop)
  }

  "Paused, nothing" should "Paused, None" in {
    paused(card1)(Card.None) should be(paused(card1), None)
  }

  "Paused, same card" should "Playing, Resume" in {
    paused(card1)(card1) should be(playing(card1), resume)
  }

  "Paused, different card" should "Playing, Play" in {
    paused(card1)(card2) should be(playing(card2), play(card2))
  }

  "Paused, unknown card" should "Paused, None" in {
    paused(card1)(Card.Unknown) should be(paused(card1), None)
  }

  "Paused, stop" should "Stopped, Stop" in {
    paused(card1)(Card.Stop) should be(Stopped, stop)
  }

  private def pause: Some[Action.Pause.type] = Some(Pause)

  private def paused(album: Album): Paused = Paused(album.spotifyUri)

  private def play(album: Album): Option[Play] = Some(Play(album.spotifyUri))

  private def playing(album: Album): Playing = Playing(album.spotifyUri)

  private def resume: Some[Action.Resume.type] = Some(Resume)

  private def stop: Some[Action.Stop.type] = Some(Action.Stop)
}
