package info.maaskant.jukebox

import cats.effect.{Resource, Sync}
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class Mfrc522CardReader[F[_]] private(reader: MFRC522)(implicit F: Sync[F]) extends CardReader[F] with StrictLogging {
  def read(): F[Option[Card]] = F.delay {
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

  def close(): F[Unit] = F.delay(reader.close())
}

object Mfrc522CardReader extends StrictLogging {
  def apply[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): F[Mfrc522CardReader[F]] =
    F.delay(new Mfrc522CardReader(new MFRC522(controller, chipSelect, resetGpio)))

  def resource[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): Resource[F, Mfrc522CardReader[F]] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._
    Resource.make(for {
      _ <- F.delay(logger.debug("Opening RFID reader"))
      reader <- Mfrc522CardReader(controller, chipSelect, resetGpio)
    } yield reader)(rfid => for {
      _ <- F.delay(logger.debug("Closing RFID reader"))
      _ <- rfid.close()
    } yield ())
  }
}
