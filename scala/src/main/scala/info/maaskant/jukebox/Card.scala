package info.maaskant.jukebox

import cats.Eq
import info.maaskant.jukebox.ByteUtils.convertBytesToHex

case class Uid(value: String) extends AnyVal // Value class

case class Card(uid: Uid, `type`: String) {
  override def toString: String = s"Card($uid, ${`type`})"
}

object Card {
  implicit val cardEq: Eq[Card] = (x: Card, y: Card) => x.uid == y.uid
  implicit val cardOptionEq: Eq[Option[Card]] = {
    case (None, None) => true
    case (None, Some(_)) | (Some(_), None) => false
    case (Some(x), Some(y)) => cardEq.eqv(x, y)
  }

  def apply(uid: Array[Byte], `type`: String): Card =
    new Card(Uid(convertBytesToHex(uid)), `type`)
}