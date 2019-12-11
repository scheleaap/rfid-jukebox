package info.maaskant.jukebox

import cats.effect.ExitCode
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.ByteUtils.convertBytesToHex
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

import scala.concurrent.duration._
import scala.util.Try

object Application extends TaskApp with StrictLogging {
  logger.debug("DEBUG")
  logger.info("INFO")
  logger.warn("WARN")

  val controller = 0
  val chipSelect = 0
  val resetGpio = 25

  def loop(rfid: MFRC522): Option[Card] = {
    logger.trace("Reading from RFID")
    Try {
      if (rfid.isNewCardPresent) {
        logger.trace("Card present")
        val uid: MFRC522.UID = rfid.readCardSerial()
        rfid.haltA()
        rfid.stopCrypto1()
        Some(Card(uid.getUidBytes, uid.getType.getName))
      } else {
        logger.trace("No card present")
        None
      }
    }.recover { t =>
      logger.debug("Error while reading card", t)
      None
    }.get
  }

  override def run(args: List[String]): Task[ExitCode] =
    Observable.fromResource(Rfid.resource(controller, chipSelect, resetGpio))
      .flatMap { rfid =>
        Observable.repeatEvalF(Task {
          loop(rfid)
        }).delayOnNext(500.milliseconds)
      }
      .filter(_.isDefined)
      .map(_.get) // Never fails
      .dump("card")
      .countL
      .map(_ => ExitCode.Success)
}

case class Card(uid: String, `type`: String) {
  override def toString: String = s"Card($uid, ${`type`})"
}

object Card {
  def apply(uid: Array[Byte], `type`: String): Card =
    new Card(convertBytesToHex(uid), `type`)
}
