package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Actions.executeAction
import info.maaskant.jukebox.State._
import info.maaskant.jukebox.rfid.{Card, CardReader, Mfrc522CardReader}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

object Application extends TaskApp with StrictLogging {
  override def run(args: List[String]): Task[ExitCode] =
    for {
      config <- Config.loadF()
      _ <- Task(logger.info(s"Configuration: $config"))
      exitCode <- resources(config)
        .use(cardReader => pipeline(config, cardReader).map(_ => ExitCode.Success))
        .onErrorHandleWith(t => Task(logger.error("Fatal error", t)).map(_ => ExitCode.Error))
    } yield exitCode

  private def pipeline(
      config: Config,
      cardReader: CardReader[Task]
  ): Task[Long] = {
    Observable
      .repeatEvalF(cardReader.read())
      .delayOnNext(config.readInterval)
      .distinctUntilChanged
      .doOnNext(i => Task(logger.info(s"Physical card: $i")))
      .scanEval[State](Task.pure(Starting)) { (s0, card) =>
        updateStateAndExecuteAction(s0, card)
      }
      //.dump("state")
      //.foldWhileLeftL(())((_, state) => state match {
      //  case Stopped => Right(())
      //  case _ => Left(())
      //})
      .countL
  }

  private def resources(config: Config): Resource[Task, CardReader[Task]] =
    for {
      cardReader <- Mfrc522CardReader.resource(config.spi.controller, config.spi.chipSelect, config.spi.resetGpio)
    } yield cardReader

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
