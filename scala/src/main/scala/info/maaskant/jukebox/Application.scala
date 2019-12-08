package info.maaskant.jukebox

import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

object Application extends App with StrictLogging {
  val spiController = 0
  val chipSelect = 0
  val resetGpio = 25
  val rfid = new MFRC522(spiController, chipSelect, resetGpio)
  while (true) {
    if (rfid.isNewCardPresent) {
      val uid: MFRC522.UID = rfid.readCardSerial()
      logger.info(s"UID read: ${uid.toString}")
    }
  }

}
