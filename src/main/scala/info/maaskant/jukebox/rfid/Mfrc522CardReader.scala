package info.maaskant.jukebox.rfid

import cats.effect.{Resource, Sync}
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class Mfrc522CardReader[F[_]] private (reader: MFRC522)(implicit F: Sync[F]) extends CardReader[F] with StrictLogging {
  def read(): F[Option[Card]] = F.delay {
    logger.trace("Reading from RFID")
    Try {
      if (reader.isNewCardPresent) {
        logger.trace("Card present")
        val nullableUid: MFRC522.UID = reader.readCardSerial()
        reader.haltA()
        reader.stopCrypto1()
        if (nullableUid != null) {
          Some(Card(nullableUid.getUidBytes, nullableUid.getType.getName))
        } else {
          logger.trace("Could not read card UID")
          None
        }
      } else {
        logger.trace("No card present")
        None
      }
    }.recover { t =>
      logger.debug("Error while reading card", t)
      None
    }.get
  }

  def close(): F[Unit] = F.delay(reader.close())
}

object Mfrc522CardReader extends StrictLogging {
  def apply[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): F[Mfrc522CardReader[F]] =
    F.delay(new Mfrc522CardReader(new MFRC522(controller, chipSelect, resetGpio)))

  def resource[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(
      implicit F: Sync[F]
  ): Resource[F, Mfrc522CardReader[F]] = {
    import cats.syntax.flatMap._
    Resource.make(
      F.delay(logger.debug("Opening RFID reader")) >>
        Mfrc522CardReader(controller, chipSelect, resetGpio)
    )(rfid =>
      F.delay(logger.debug("Closing RFID reader")) >>
        rfid.close()
    )
  }
}
