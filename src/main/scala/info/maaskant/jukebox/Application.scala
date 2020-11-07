package info.maaskant.jukebox

import cats.effect.{ExitCode, Resource}
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.rfid.{CardReader, Mfrc522CardReader}
import monix.eval.{Task, TaskApp}
import monix.reactive.Observable

object Application extends TaskApp with StrictLogging {
  override def run(args: List[String]): Task[ExitCode] =
    for {
      config <- Config.loadF()
      _ <- Task(logger.info(s"Configuration: $config"))
      exitCode <- resources(config)
        .use(cardReader => pipeline(config, cardReader).map(_ => ExitCode.Success))
        .onErrorHandleWith(t =>
          Task(logger.error("Fatal error", t))
            .map(_ => ExitCode.Error)
        )
    } yield exitCode

  private def pipeline(
      config: Config,
      cardReader: CardReader[Task]
  ): Task[Long] = {
    Observable
      .repeatEvalF(cardReader.read())
      .delayOnNext(config.readInterval)
      .distinctUntilChanged
      //.dump("physical")
      .countL
  }

  private def resources(config: Config): Resource[Task, CardReader[Task]] =
    for {
      cardReader <- Mfrc522CardReader.resource(config.spi.controller, config.spi.chipSelect, config.spi.resetGpio)
    } yield cardReader
}
