package info.maaskant.jukebox

import cats.effect.{Resource, Sync}
import com.diozero.devices.MFRC522

object Rfid {
  def resource[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): Resource[F, MFRC522] =
    Resource.fromAutoCloseable(F.delay {
      new MFRC522(controller, chipSelect, resetGpio)
    })
}
