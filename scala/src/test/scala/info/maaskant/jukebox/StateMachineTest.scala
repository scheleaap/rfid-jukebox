package info.maaskant.jukebox

import info.maaskant.jukebox.Event.{Pause, Play, Resume}
import info.maaskant.jukebox.State.{Paused, Playing, Stopped}
import info.maaskant.jukebox.rfid.{Card, Uid}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StateMachineTest extends AnyFlatSpec with Matchers {
  private val unknownCard = Uid("unknown")
  private val card1 = Uid("card1")
  private val card2 = Uid("card2")
  private implicit val cardMapping: Map[Uid, SpotifyUri] = Map(
    card1 -> SpotifyUri(card1.value),
    card2 -> SpotifyUri(card2.value)
  )

  "Stopped, nothing" should "Stopped, None" in {
    Stopped(None) should be(Stopped, None)
  }

  "Stopped, known card" should "Playing, Play" in {
    Stopped(card(card1)) should be(Playing(card1), play(card1))
  }

  "Stopped, unknown card" should "Stopped, None" in {
    Stopped(card(unknownCard)) should be(Stopped, None)
  }

  "Playing, nothing" should "Paused, Pause" in {
    Playing(card1)(None) should be(Paused(card1), pause)
  }

  "Playing, same card" should "Playing, None" in {
    Playing(card1)(card(card1)) should be(Playing(card1), None)
  }

  "Playing, different card" should "Playing, Play" in {
    Playing(card1)(card(card2)) should be(Playing(card2), play(card2))
  }

  "Playing, unknown card" should "Playing, None" in {
    Playing(card1)(card(unknownCard)) should be(Playing(card1), None)
  }

  "Paused, nothing" should "Paused, None" in {
    Paused(card1)(None) should be(Paused(card1), None)
  }

  "Paused, same card" should "Playing, Resume" in {
    Paused(card1)(card(card1)) should be(Playing(card1), resume)
  }

  "Paused, different card" should "Playing, Play" in {
    Paused(card1)(card(card2)) should be(Playing(card2), play(card2))
  }

  "Paused, unknown card" should "Paused, None" in {
    Paused(card1)(card(unknownCard)) should be(Paused(card1), None)
  }

  private def card(uid: Uid): Option[Card] = Some(Card(uid, "test"))

  private def play(uid: Uid): Option[Play] = Some(Play(cardMapping(uid)))

  private def pause: Some[Event.Pause.type] = Some(Pause)

  private def resume: Some[Event.Resume.type] = Some(Resume)
}
