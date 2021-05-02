package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Initialize, Pause, Play, Resume}
import info.maaskant.jukebox.Card.Album
import info.maaskant.jukebox.mopidy.MopidyUri
import info.maaskant.jukebox.rfid.Uid
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._
import java.time.{Clock, Instant, ZoneId, ZonedDateTime}

class StateMachineTest extends AnyFlatSpec with Matchers {
  private val album1 = Album(MopidyUri("album1"))
  private val album2 = Album(MopidyUri("album2"))
  private val httpAlbum = Album(MopidyUri("http://foo"))
  private val unknown = Card.Unknown(Uid("1"))
  private val zoneId: ZoneId = ZoneId.of("Europe/Amsterdam")
  private val instant1 = ZonedDateTime
    .of(2021, 5, 2, 14, 36, 0, 0, zoneId)
    .toInstant
  private val instant2 = instant1.plusSeconds(60)

  private val stateMachine = StateMachine(60.seconds)
  import stateMachine.{Paused, Playing, Stopped, Uninitialized}

  "Uninitialized, any" should "Stopped, SignalReady" in {
    Uninitialized(Card.None) should be(Stopped, Some(Initialize))
  }

  "Stopped, nothing" should "Stopped, None" in {
    val originalState = Stopped
    originalState(Card.None) should be(originalState, None)
  }

  "Stopped, album" should "Playing, Play" in {
    Stopped(album1) should be(playing(album1), play(album1))
  }

  "Stopped, unknown card" should "Stopped, None" in {
    val originalState = Stopped
    originalState(unknown) should be(originalState, None)
  }

  "Stopped, shutdown" should "Stopped, Shutdown" in {
    Stopped(Card.Shutdown) should be(Stopped, shutdown)
  }

  "Stopped, stop" should "Stopped, None" in {
    val originalState = Stopped
    originalState(Card.Stop) should be(originalState, None)
  }

  "Playing, nothing" should "Paused, Pause" in {
    playing(album1)(Card.None, clock(instant1)) should be(paused(album1, instant1), pause)
  }

  "Playing, same card" should "Playing, None" in {
    val originalState = playing(album1)
    originalState(album1) should be(originalState, None)
  }

  "Playing, different card" should "Playing, Play" in {
    playing(album1)(album2) should be(playing(album2), play(album2))
  }

  "Playing, unknown card" should "Playing, None" in {
    val originalState = playing(album1)
    originalState(unknown) should be(originalState, None)
  }

  "Playing, shutdown" should "Playing, Shutdown" in {
    val originalState = playing(album1)
    originalState(Card.Shutdown) should be(originalState, shutdown)
  }

  "Playing, stop" should "Stopped, Stop" in {
    playing(album1)(Card.Stop) should be(Stopped, stop)
  }

  "Paused, nothing" should "Paused, None" in {
    val originalState = paused(album1, instant1)
    originalState(Card.None, clock(instant2)) should be(originalState, None)
  }

  "Paused (HTTP URI), same card after short delay" should "Playing, Resume" in {
    paused(httpAlbum, instant1)(httpAlbum, clock(instant1.plusSeconds(59))) should be(playing(httpAlbum), resume)
  }

  "Paused (HTTP URI), same card after long delay" should "Playing, Play" in {
    paused(httpAlbum, instant1)(httpAlbum, clock(instant1.plusSeconds(60))) should be(
      playing(httpAlbum),
      play(httpAlbum)
    )
  }

  "Paused (other), same card after long delay" should "Playing, Resume" in {
    paused(album1, instant1)(album1, clock(instant1.plusSeconds(60))) should be(playing(album1), resume)
  }

  "Paused, different card" should "Playing, Play" in {
    paused(album1)(album2) should be(playing(album2), play(album2))
  }

  "Paused, unknown card" should "Paused, None" in {
    val originalState = paused(album1, instant1)
    originalState(unknown, clock(instant2)) should be(originalState, None)
  }

  "Paused, shutdown" should "Paused, Shutdown" in {
    val originalState = paused(album1, instant1)
    originalState(Card.Shutdown) should be(originalState, shutdown)
  }

  "Paused, stop" should "Stopped, Stop" in {
    paused(album1)(Card.Stop) should be(Stopped, stop)
  }

  private def clock(instant: Instant) = Clock.fixed(instant, zoneId)

  private def pause: Some[Action.Pause.type] = Some(Pause)

  private def paused(album: Album, since: Instant = Instant.now()): Paused = Paused(album.spotifyUri, since)

  private def play(album: Album): Option[Play] = Some(Play(album.spotifyUri))

  private def playing(album: Album): Playing = Playing(album.spotifyUri)

  private def resume: Some[Action.Resume.type] = Some(Resume)

  private def shutdown: Some[Action.Shutdown.type] = Some(Action.Shutdown)

  private def stop: Some[Action.Stop.type] = Some(Action.Stop)
}
