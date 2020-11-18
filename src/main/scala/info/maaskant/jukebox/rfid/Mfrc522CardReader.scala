package info.maaskant.jukebox.rfid

import java.io.IOException

import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.MFRC522
import info.maaskant.jukebox.MFRC522.{PcdRegister, StatusCode}
import info.maaskant.jukebox.rfid.Mfrc522CardReader.ReadError
import info.maaskant.jukebox.rfid.Mfrc522CardReader.ReadError.{PermanentError, TemporaryError, UnknownError}
import monix.eval.Task
import monix.execution.Scheduler
import monix.reactive.Observable

import scala.util.Try

case class Mfrc522CardReader private (controller: Int, chipSelect: Int, resetGpio: Int)
    extends CardReader
    with StrictLogging {

  private val scheduler = Scheduler.singleThread("mfrc522")

  def read(): Observable[Option[Card]] = {
    Observable
      .resource {
        Task(logger.debug("Opening RFID reader")) >>
          Task(new MFRC522(controller, chipSelect, resetGpio))
      } { reader =>
        Task(logger.debug("Closing RFID reader")) >>
          Task(reader.close())
      }
      .flatMap { reader => Observable.repeatEval(read(reader)) }
      .flatMap {
        case Left(PermanentError) =>
          logger.trace("Raising error")
          Observable.raiseError(new IOException("Permanent card reader error"))
        case Left(_) => Observable.empty
        case Right(i) => Observable.pure(i)
      }
      .onErrorRestart(2)
      .subscribeOn(scheduler)
      .executeOn(scheduler, forceAsync = false)
  }

  private def read(reader: MFRC522): Either[ReadError, Option[Card]] = {
    Try {
      logger.trace("Checking if a card is present")
      isNewCardPresent(reader) match {
        case Right(true) =>
          logger.trace("Card present, reading serial")
          val nullableUid: MFRC522.UID = reader.readCardSerial()
          logger.trace("Halting card")
          reader.haltA()
          if (nullableUid != null) {
            Right(Some(Card(nullableUid.getUidBytes, nullableUid.getType.getName)))
          } else {
            logger.trace("Could not read card UID")
            Left(TemporaryError)
          }
        case Right(false) =>
          logger.trace("No card present")
          Right(None)
        case Left(statusCode) =>
          logger.warn(s"$statusCode while checking if card is present")
          statusCode match {
            case StatusCode.ERROR => Left(PermanentError)
            case _ => Left(UnknownError)
          }
      }
    }.recover { t =>
      logger.warn("Error while communicating with RFID reader", t)
      reader.haltA()
      Left(UnknownError)
    }.get
  }

  /** Returns true if a PICC responds to PICC_CMD_REQA.
    * Only "new" cards in state IDLE are invited. Sleeping cards in state HALT are ignored.
    *
    * (Modified from [[MFRC522.isNewCardPresent]] to return errors.)
    */
  private def isNewCardPresent(reader: MFRC522): Either[StatusCode, Boolean] = {
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

object Mfrc522CardReader {
  sealed trait ReadError

  object ReadError {
    case object TemporaryError extends ReadError
    case object PermanentError extends ReadError
    case object UnknownError extends ReadError
  }
}
