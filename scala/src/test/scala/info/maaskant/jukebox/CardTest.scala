package info.maaskant.jukebox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTest extends AnyFlatSpec with Matchers {
  private val album1 = Card.Album(SpotifyUri("a"))
  private val album2 = Card.Album(SpotifyUri("b"))

  "eq" should "work correctly" in {
    Card.eq.eqv(Card.None, Card.None) shouldBe true
    Card.eq.eqv(Card.None, Card.Stop) shouldBe false
    Card.eq.eqv(Card.None, Card.Unknown) shouldBe false
    Card.eq.eqv(Card.None, album1) shouldBe false
    Card.eq.eqv(album1, album1) shouldBe true
    Card.eq.eqv(album1, album2) shouldBe false
  }
}
