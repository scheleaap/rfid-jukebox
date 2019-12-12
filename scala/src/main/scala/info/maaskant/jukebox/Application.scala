package info.maaskant.jukebox

import cats.effect.ExitCode
import com.typesafe.scalalogging.StrictLogging
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

import scala.concurrent.duration._

object Application extends TaskApp with StrictLogging {
  logger.debug("DEBUG")
  logger.info("INFO")
  logger.warn("WARN")

  val controller = 0
  val chipSelect = 0
  val resetGpio = 25

  override def run(args: List[String]): Task[ExitCode] = {
    import Card.cardUidEq

    //    val cardReader = Mfrc522CardReader.resource(controller, chipSelect, resetGpio)
    val cardReader = FakeCardReader.resource()

    Observable.fromResource(cardReader)
      .flatMap { rfid =>
        Observable
          .repeatEvalF(Task(rfid.read()))
          .delayOnNext(500.milliseconds)
      }
      .filter(_.isDefined)
      .map(_.get) // Never fails
      // .dump("card")
      .distinctUntilChanged
      .dump("distinct")
      .countL
      .map(_ => ExitCode.Success)
  }
}
