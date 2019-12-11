package info.maaskant.jukebox

import cats.effect.{Resource, Sync}
import com.diozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging

object Rfid extends StrictLogging {
  def resource[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): Resource[F, MFRC522] =
    Resource.make(F.delay {
      logger.debug("Opening RFID reader")
      new MFRC522(controller, chipSelect, resetGpio)
    })(rfid => F.delay {
      logger.debug("Closing RFID reader")
      rfid.close()
    })
}
