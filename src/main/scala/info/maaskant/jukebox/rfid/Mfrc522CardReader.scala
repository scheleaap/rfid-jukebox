package info.maaskant.jukebox.rfid

import cats.effect.{Resource, Sync}
import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import com.wiozero.devices.MFRC522
import com.wiozero.devices.MFRC522.{PcdRegister, StatusCode}

import scala.util.Try

class Mfrc522CardReader[F[_]] private (reader: MFRC522)(implicit F: Sync[F]) extends CardReader[F] with StrictLogging {
  def read(): F[Either[ReadError.type, Option[Card]]] = F.delay {
    Try {
      logger.trace("Checking if a card is present")
      isNewCardPresent match {
        case Right(true) =>
          logger.trace("Card present, reading serial")
          val nullableUid: MFRC522.UID = reader.readCardSerial()
          logger.trace("Halting card")
          reader.haltA()
          if (nullableUid != null) {
            Right(Some(Card(nullableUid.getUidBytes, nullableUid.getType.getName)))
          } else {
            logger.trace("Could not read card UID")
            Left(ReadError)
          }
        case Right(false) =>
          logger.trace("No card present")
          Right(None)
        case Left(statusCode) =>
          logger.warn(s"$statusCode while checking if card is present")
          Left(ReadError)
      }
    }.recover { t =>
      logger.warn("Error while communicating with RFID reader", t)
      reader.haltA()
      Left(ReadError)
    }.get
  }

  def close(): F[Unit] = F.delay(reader.close())

  /**
    * Returns true if a PICC responds to PICC_CMD_REQA.
    * Only "new" cards in state IDLE are invited. Sleeping cards in state HALT are ignored.
    *
    * (Modified from [[reader.isNewCardPresent]] to return errors.)
    */
  private def isNewCardPresent: Either[StatusCode, Boolean] = {
    // Reset baud rates
    reader.writeRegister(PcdRegister.TX_MODE_REG, 0x00.toByte)
    reader.writeRegister(PcdRegister.RX_MODE_REG, 0x00.toByte)
    // Reset ModWidthReg
    reader.writeRegister(PcdRegister.MOD_WIDTH_REG, 0x26.toByte)
    val bufferATQA = new Array[Byte](2)
    val result = reader.requestA(bufferATQA)
    result match {
      case StatusCode.OK | StatusCode.COLLISION => Right(true)
      case StatusCode.TIMEOUT => Right(false)
      case i => Left(i)
    }
  }
}

object Mfrc522CardReader extends StrictLogging {
  def apply[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): F[Mfrc522CardReader[F]] =
    F.delay({
        val mfrc = new MFRC522(controller, chipSelect, resetGpio)
        // Both reset() and performSelfTest() (which calls reset()) break the reader.
        mfrc.reset()
        mfrc -> true //mfrc.performSelfTest()
      })
      .flatMap {
        case (mfrc, selfTestSuccessful) =>
          if (selfTestSuccessful) {
            F.pure(new Mfrc522CardReader(mfrc))
          } else {
            F.delay(mfrc.close())
              .flatMap(_ => F.raiseError(new RuntimeException("Self test failed")))
          }

      }

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
