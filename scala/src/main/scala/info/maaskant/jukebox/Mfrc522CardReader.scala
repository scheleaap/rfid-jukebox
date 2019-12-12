package info.maaskant.jukebox

import cats.effect.{Resource, Sync}
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class Mfrc522CardReader private(reader: MFRC522) extends CardReader with StrictLogging {
  def read(): Option[Card] = {
    logger.trace("Reading from RFID")
    Try {
      if (reader.isNewCardPresent) {
        logger.trace("Card present")
        val uid: MFRC522.UID = reader.readCardSerial()
        reader.haltA()
        reader.stopCrypto1()
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

  def close(): Unit = reader.close()
}

object Mfrc522CardReader extends StrictLogging {
  def apply(controller: Int, chipSelect: Int, resetGpio: Int): Mfrc522CardReader =
    new Mfrc522CardReader(new MFRC522(controller, chipSelect, resetGpio))

  def resource[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): Resource[F, Mfrc522CardReader] =
    Resource.make(F.delay {
      logger.debug("Opening RFID reader")
      Mfrc522CardReader(controller, chipSelect, resetGpio)
    })(rfid => F.delay {
      logger.debug("Closing RFID reader")
      rfid.close()
    })
}
