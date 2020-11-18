package info.maaskant.jukebox

import cats.effect.ExitCode
import com.typesafe.scalalogging.StrictLogging
import monix.eval.{Task, TaskApp}
import monix.execution.Scheduler
import monix.execution.schedulers.TestScheduler
import monix.reactive.Observable

import scala.concurrent.duration.DurationInt
import scala.util.Random

object Test extends TaskApp with StrictLogging {

  val s = Scheduler.singleThread("t")

  override def run(args: List[String]): Task[ExitCode] = {
    Observable
      .resource(Task({
        val i = new Random().nextInt(100)
        logger.info(s"Acquire $i")
        i
      }))(i =>
        Task({
          logger.info(s"Release $i")
          Thread.sleep(500)
        })
      )
      .flatMap(resource =>
        Observable
          .repeat(1)
      )
      .delayOnNext(1.second)
      .map { _ =>
        new Random().nextInt(5)
      }
      .dump("test")
//      .map { i =>
//        if (i == 0) throw new RuntimeException("E")
//        i
//      }
      .flatMap { i =>
        if (i ==0) Observable.raiseError(new RuntimeException("E"))
        else Observable.pure(i)
      }
      .onErrorRestart(2)
      .executeOn(s)
      //      .executeOn(Scheduler.global)
      .observeOn(Scheduler.global)
      .doOnNext(_ => Task(logger.info("test")))
      .countL
      .map(_ => ExitCode.Success)
  }
}
