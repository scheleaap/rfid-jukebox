package info.maaskant.jukebox

import java.nio.file.{Files, Paths}

import cats.effect.ExitCode
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Actions.executeAction
import info.maaskant.jukebox.State._
import info.maaskant.jukebox.rfid.{Card, Mfrc522CardReader}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

import scala.concurrent.duration.DurationInt

object Application extends TaskApp with StrictLogging {
  private def createCardReader(config: Spi) = {
    Mfrc522CardReader(config.controller, config.chipSelect, config.resetGpio)
//    FakeCardReader.resource(new TimeBasedReader())
//    FakeCardReader.resource(
//      new FixedUidReader(
//        IndexedSeq(
//          None,
//          Some(Uid("ebd1a421")),
//          Some(Uid("ebd1a421")),
//          Some(Uid("ebd1a421")),
//          Some(Uid("042abc4a325e81")),
//          Some(Uid("042ebc4a325e81")),
//          Some(Uid("TODO"))
//        )
//      )
//    )
  }

  override def run(args: List[String]): Task[ExitCode] =
    for {
      config <- Config.loadF()
      _ <- Task(logger.info(s"Configuration: $config"))
      exitCode <- pipeline(config).map(_ => ExitCode.Success)
        .onErrorHandleWith(t => Task(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

  private def pipeline(
      config: Config,
  ): Task[_] = {
    val cardReader = createCardReader(config.spi)
    cardReader.read()
      .delayOnNext(1.second)
//      .distinctUntilChanged
      .flatMap {
        case Left(_) => Observable.empty
        case Right(i) => Observable.pure(i)
      }
      .doOnNext(i => Task(logger.debug(s"Physical card: $i")))
      .scanEval[State](Task.pure(Starting)) { (s0, card) =>
        updateStateAndExecuteAction(s0, card)
      }
      .doOnNext(i => Task(logger.debug(s"State: $i")))
      .foldWhileLeftL(())((_, state) =>
        state match {
          case i: Finished =>
            val dir =
//              Paths.get(f"/tmp/rfid-jukebox/")
              Paths.get(f"/home/pi/rfid-jukebox-tmp")
            Files.createDirectories(dir)
            Files.write(
              dir.resolve(f"timestamps-${i.start}.log"),
              f"${i.start},${i.finish},${i.duration}\n".getBytes()
            )
            Right(())
          case _ => Left(())
        }
      )
  }

  private def updateStateAndExecuteAction(s0: State, card: Option[Card]): Task[State] = {
    val (s1, action0) = s0(card)
    action0 match {
      case None => Task.pure(s1)
      case Some(action) =>
        executeAction(action).flatMap { success =>
          if (success) {
            Task(logger.debug(s"New state: $s1")) >> Task.pure(s1)
          } else {
            Task(logger.warn(s"Failed to execute action $action to go from $s0 to $s1")) >>
              Task.pure(s0)
          }
        }
    }
  }
}
