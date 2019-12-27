package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.State.{Paused, Playing, Stopped}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StateMachineTest extends AnyFlatSpec with Matchers {
  private val album1 = Album(SpotifyUri("album1"))
  private val album2 = Album(SpotifyUri("album2"))

  "Stopped, nothing" should "Stopped, None" in {
    Stopped(Card.None) should be(Stopped, None)
  }

  "Stopped, album" should "Playing, Play" in {
    Stopped(album1) should be(playing(album1), play(album1))
  }

  "Stopped, unknown card" should "Stopped, None" in {
    Stopped(Card.Unknown) should be(Stopped, None)
  }

  "Stopped, stop" should "Stopped, None" in {
    Stopped(Card.Stop) should be(Stopped, None)
  }

  "Playing, nothing" should "Paused, Pause" in {
    playing(album1)(Card.None) should be(paused(album1), pause)
  }

  "Playing, same card" should "Playing, None" in {
    playing(album1)(album1) should be(playing(album1), None)
  }

  "Playing, different card" should "Playing, Play" in {
    playing(album1)(album2) should be(playing(album2), play(album2))
  }

  "Playing, unknown card" should "Playing, None" in {
    playing(album1)(Card.Unknown) should be(playing(album1), None)
  }

  "Playing, stop" should "Stopped, Stop" in {
    playing(album1)(Card.Stop) should be(Stopped, stop)
  }

  "Paused, nothing" should "Paused, None" in {
    paused(album1)(Card.None) should be(paused(album1), None)
  }

  "Paused, same card" should "Playing, Resume" in {
    paused(album1)(album1) should be(playing(album1), resume)
  }

  "Paused, different card" should "Playing, Play" in {
    paused(album1)(album2) should be(playing(album2), play(album2))
  }

  "Paused, unknown card" should "Paused, None" in {
    paused(album1)(Card.Unknown) should be(paused(album1), None)
  }

  "Paused, stop" should "Stopped, Stop" in {
    paused(album1)(Card.Stop) should be(Stopped, stop)
  }

  private def pause: Some[Action.Pause.type] = Some(Pause)

  private def paused(album: Album): Paused = Paused(album.spotifyUri)

  private def play(album: Album): Option[Play] = Some(Play(album.spotifyUri))

  private def playing(album: Album): Playing = Playing(album.spotifyUri)

  private def resume: Some[Action.Resume.type] = Some(Resume)

  private def stop: Some[Action.Stop.type] = Some(Action.Stop)
}
