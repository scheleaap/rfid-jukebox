package info.maaskant.jukebox

import java.util.concurrent.{Executors, SynchronousQueue, ThreadPoolExecutor, TimeUnit}

import cats.effect.{Resource, Sync}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Threads {
  def blockingThreadPool[F[_]](implicit F: Sync[F]): Resource[F, ExecutionContext] =
    Resource(F.delay {
      val executor = new ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        1L,
        TimeUnit.HOURS,
        new SynchronousQueue[Runnable]()
      )
      val executionContext = ExecutionContext.fromExecutor(executor)
      (executionContext, F.delay(executor.shutdown()))
    })
}
