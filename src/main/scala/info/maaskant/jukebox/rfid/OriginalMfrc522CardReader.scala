//package info.maaskant.jukebox.rfid
//
//import com.diozero.devices.MFRC522
//import com.typesafe.scalalogging.StrictLogging
//import monix.eval.Task
//import monix.execution.Scheduler
//import monix.reactive.Observable
//
//import scala.util.Try
//
//case class OriginalMfrc522CardReader private (controller: Int, chipSelect: Int, resetGpio: Int)
//    extends CardReader
//    with StrictLogging {
//
//  private val scheduler = Scheduler.singleThread("mfrc522")
//
//  def read(): Observable[Option[Card]] = {
//    Observable
//      .resource {
//        Task(logger.debug("Opening RFID reader")) >>
//          Task(new MFRC522(controller, chipSelect, resetGpio))
//      } { reader =>
//        Task(logger.debug("Closing RFID reader")) >>
//          Task(reader.close())
//      }
//      .flatMap { reader => Observable.repeatEval(read(reader)) }
//      .subscribeOn(scheduler)
//      .executeOn(scheduler, forceAsync = false)
//  }
//
//  def read(reader: MFRC522): Option[Card] = {
//    Try {
//      logger.trace("Checking if a card is present")
//      if (reader.isNewCardPresent) {
//        logger.trace("Card present, reading serial")
//        val nullableUid: MFRC522.UID = reader.readCardSerial()
//        logger.trace("Halting card")
//        reader.haltA()
//        reader.stopCrypto1()
//        if (nullableUid != null) {
//          Some(Card(nullableUid.getUidBytes, nullableUid.getType.getName))
//        } else {
//          logger.trace("Could not read card UID")
//          None
//        }
//      } else {
//        logger.trace("No card present")
//        None
//      }
//    }.recover { t =>
//      logger.debug("Error while reading card", t)
//      None
//    }.get
//  }
//}
