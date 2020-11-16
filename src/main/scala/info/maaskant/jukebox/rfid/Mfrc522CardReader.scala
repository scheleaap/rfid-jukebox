package info.maaskant.jukebox.rfid

import cats.implicits._
import cats.effect.implicits._
import cats.effect.{Resource, Sync}
import com.wiozero.devices.MFRC522
import com.typesafe.scalalogging.StrictLogging
import com.wiozero.devices.MFRC522.StatusCode

import scala.util.Try

class Mfrc522CardReader[F[_]] private (reader: MFRC522)(implicit F: Sync[F]) extends CardReader[F] with StrictLogging {
  def read(): F[Option[Card]] = F.delay {
    logger.trace("1: Reading from RFID")
    Try {
      if (reader.isNewCardPresent) {
        logger.trace("2: Card present; reading serial")
        val nullableUid: MFRC522.UID = reader.readCardSerial()
        Thread.sleep(50)
        logger.trace("3: Halting")
        reader.haltA() match {
          case StatusCode.TIMEOUT => logger.trace("E1a: Timeout while halting")
          case StatusCode.OK =>
          case StatusCode.ERROR => logger.trace("E1b: Error while halting")
        }
        // TODO Changed:
        //logger.trace("4: Stopping crypto")
        //reader.stopCrypto1()
        if (nullableUid != null) {
          Some(Card(nullableUid.getUidBytes, nullableUid.getType.getName))
        } else {
          logger.trace("E2: Could not read card UID")
          None
        }
      } else {
        logger.trace("E3: No card present")
        None
      }
    }.recover { t =>
      logger.debug("E4: Error while reading card", t)
      reader.haltA() // TODO Changed
      None
    }.get
  }

  def close(): F[Unit] = F.delay(reader.close())
}

object Mfrc522CardReader extends StrictLogging {
  def apply[F[_]](controller: Int, chipSelect: Int, resetGpio: Int)(implicit F: Sync[F]): F[Mfrc522CardReader[F]] =
    F.delay({
        val mfrc = new MFRC522(controller, chipSelect, resetGpio)
        // Both reset() and performSelfTest() (which calls reset()) break the reader.
        //mfrc.reset()
        mfrc.dumpVersionToConsole()
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
