package info.maaskant.jukebox.rfid

import cats.effect.{IO, Resource}
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

case class OriginalMfrc522CardReader private (reader: MFRC522)
    extends CardReader    with StrictLogging {

  def read(): IO[Option[Card]] =
    IO(unsafeRead(reader))

  def unsafeRead(reader: MFRC522): Option[Card] = {
    Try {
      logger.trace("Checking if a card is present")
      if (reader.isNewCardPresent) {
        logger.trace("Card present, reading serial")
        val nullableUid: MFRC522.UID = reader.readCardSerial()
        logger.trace("Halting card")
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

  override def close(): IO[Unit] = IO(reader.close())
}


object OriginalMfrc522CardReader extends StrictLogging  {
  def resource(controller: Int, chipSelect: Int, resetGpio: Int): Resource[IO, OriginalMfrc522CardReader] =
    Resource.make(
      IO(logger.debug("Opening RFID reader")) >>
        IO(OriginalMfrc522CardReader(new MFRC522(controller, chipSelect, resetGpio)))
    )(reader =>
      IO(logger.debug("Closing RFID reader")) >>
        IO(reader.close())
    )
}
