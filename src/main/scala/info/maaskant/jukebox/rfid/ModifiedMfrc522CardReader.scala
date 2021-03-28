package info.maaskant.jukebox.rfid

import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.MFRC522
import info.maaskant.jukebox.MFRC522.StatusCode
import info.maaskant.jukebox.rfid.ModifiedMfrc522CardReader.ReadError
import info.maaskant.jukebox.rfid.ModifiedMfrc522CardReader.ReadError.{PermanentError, TemporaryError, UnknownError}
import monix.eval.Task
import monix.execution.Scheduler
import monix.reactive.Observable

import java.io.IOException
import scala.util.Try

case class ModifiedMfrc522CardReader private (controller: Int, chipSelect: Int, resetGpio: Int)
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
          val message = "Permanent card reader error"
          logger.warn(message)
          Observable.raiseError(new IOException(message))
        case Left(_) => Observable.pure(None)
        case Right(i) => Observable.pure(i)
      }
      .onErrorRestart(2)
      .subscribeOn(scheduler)
      .executeOn(scheduler, forceAsync = false)
  }

  private def read(reader: MFRC522): Either[ReadError, Option[Card]] = {
    Try {
      logger.trace("Checking if a card is present")
      reader.isNewCardPresent2 match {
        case Right(java.lang.Boolean.TRUE) =>
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
        case Right(_ /* matches TRUE, null */ ) =>
          logger.trace("No card present")
          Right(None)
        case Left(statusCode) =>
          logger.trace(s"Received status code '$statusCode' while checking if a card is present")
          statusCode match {
            case StatusCode.TIMEOUT_WAITING_FOR_INTERRUPT => Left(PermanentError)
            case _ => Left(UnknownError)
          }
      }
    }.recover { t =>
      logger.warn("Error while communicating with RFID reader", t)
      reader.haltA()
      Left(UnknownError)
    }.get
  }
}

object ModifiedMfrc522CardReader {
  sealed trait ReadError

  object ReadError {
    case object TemporaryError extends ReadError
    case object PermanentError extends ReadError
    case object UnknownError extends ReadError
  }
}
