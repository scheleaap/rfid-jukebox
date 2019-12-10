package info.maaskant.jukebox

import cats.effect.ExitCode
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

object Application extends TaskApp with StrictLogging {
  logger.debug("DEBUG")
  logger.info("INFO")
  logger.warn("WARN")

  val controller = 0
  val chipSelect = 0
  val resetGpio = 25

  def loop(rfid: MFRC522): Option[Card] = {
    if (rfid.isNewCardPresent) {
      val uid: MFRC522.UID = rfid.readCardSerial()
      rfid.haltA()
      rfid.stopCrypto1()
      Some(Card(uid.getUidBytes, uid.getType.getName))
    } else {
      None
    }
  }

  override def run(args: List[String]): Task[ExitCode] =
    Observable.fromResource(Rfid.resource(controller, chipSelect, resetGpio))
      .flatMap { mfrc522 =>
        Observable.repeatEvalF(Task {
          loop(mfrc522)
        })
      }
      .filter(_.isDefined)
      .map(_.get) // Never fails
      .dump("card")
      .countL
      .map(_ => ExitCode.Success)
}

case class Card(uid: Array[Byte], `type`: String)
