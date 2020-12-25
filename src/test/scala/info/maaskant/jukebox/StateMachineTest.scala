package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Initialize, Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.State.{Paused, Playing, Stopped, Uninitialized}
import info.maaskant.jukebox.mopidy.MopidyUri
import info.maaskant.jukebox.rfid.Uid
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StateMachineTest extends AnyFlatSpec with Matchers {
  private val album1 = Album(MopidyUri("album1"))
  private val album2 = Album(MopidyUri("album2"))
  private val unknown = Card.Unknown(Uid("1"))

  "Uninitialized, any" should "Stopped, SignalReady" in {
    Uninitialized(Card.None) should be(Stopped, Some(Initialize))
  }

  "Stopped, nothing" should "Stopped, None" in {
    Stopped(Card.None) should be(Stopped, None)
  }

  "Stopped, album" should "Playing, Play" in {
    Stopped(album1) should be(playing(album1), play(album1))
  }

  "Stopped, unknown card" should "Stopped, None" in {
    Stopped(unknown) should be(Stopped, None)
  }

  "Stopped, shutdown" should "Stopped, Shutdown" in {
    Stopped(Card.Shutdown) should be(Stopped, shutdown)
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
    playing(album1)(unknown) should be(playing(album1), None)
  }

  "Playing, shutdown" should "Playing, Shutdown" in {
    playing(album1)(Card.Shutdown) should be(playing(album1), shutdown)
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
    paused(album1)(unknown) should be(paused(album1), None)
  }

  "Paused, shutdown" should "Paused, Shutdown" in {
    paused(album1)(Card.Shutdown) should be(paused(album1), shutdown)
  }

  "Paused, stop" should "Stopped, Stop" in {
    paused(album1)(Card.Stop) should be(Stopped, stop)
  }

  private def pause: Some[Action.Pause.type] = Some(Pause)

  private def paused(album: Album): Paused = Paused(album.spotifyUri)

  private def play(album: Album): Option[Play] = Some(Play(album.spotifyUri))

  private def playing(album: Album): Playing = Playing(album.spotifyUri)

  private def resume: Some[Action.Resume.type] = Some(Resume)

  private def shutdown: Some[Action.Shutdown.type] = Some(Action.Shutdown)

  private def stop: Some[Action.Stop.type] = Some(Action.Stop)
}
