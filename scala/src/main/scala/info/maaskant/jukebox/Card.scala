package info.maaskant.jukebox

import cats.Eq
import info.maaskant.jukebox.ByteUtils.convertBytesToHex

case class Card(uid: String, `type`: String) {
  override def toString: String = s"Card($uid, ${`type`})"
}

object Card {
  implicit val cardUidEq: Eq[Card] = (x: Card, y: Card) => x.uid == y.uid

  def apply(uid: Array[Byte], `type`: String): Card =
    new Card(convertBytesToHex(uid), `type`)
}