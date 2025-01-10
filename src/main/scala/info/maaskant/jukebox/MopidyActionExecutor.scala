package info.maaskant.jukebox

import cats.effect.IO
import cats.implicits.catsStdInstancesForList
import cats.syntax.parallel._
import com.typesafe.scalalogging.StrictLogging
import info.maaskant.jukebox.Process.runCommand
import info.maaskant.jukebox.mopidy.{MopidyClient, MopidyUri}

import scala.util.control.NonFatal

class MopidyActionExecutor(eventHooks: Option[EventHooks], mopidyClient: MopidyClient)
    extends ActionExecutor
    with StrictLogging {

  def executeAction(action: Action): IO[Boolean] =
    (action match {
      case Action.Initialize => executeInitialize
      case Action.Pause => executePause
      case Action.Play(uri) => executePlay(uri)
      case Action.Resume => executeResume
      case Action.Shutdown => executeShutdown
      case Action.Stop => executeStop
    }).map(_ => true).recoverWith { case NonFatal(t) =>
      IO {
        logger.warn(s"Could not execute action $action: ${t.getLocalizedMessage}")
        false
      }
    }

  private def executePause: IO[Unit] =
    List(
      mopidyClient.pausePlayback(),
      eventHooks.flatMap(_.onPause).fold(IO.unit)(runCommand(raiseError = false))
    ).parSequence
      .as(())

  private def executePlay(uri: MopidyUri): IO[Unit] =
    List(
      mopidyClient.clearTracklist() >>
        mopidyClient.addToTracklist(Seq(uri.value)) >>
        mopidyClient.startPlayback(),
      eventHooks.flatMap(_.onPlay).fold(IO.unit)(runCommand(raiseError = false))
    ).parSequence
      .as(())

  private def executeResume: IO[Unit] =
    List(
      mopidyClient.resumePlayback(),
      eventHooks.flatMap(_.onResume).fold(IO.unit)(runCommand(raiseError = false))
    ).parSequence
      .as(())

  private def executeShutdown: IO[Unit] =
    eventHooks.flatMap(_.onShutdown).fold(IO.unit)(runCommand(raiseError = false)) >>
      runCommand(raiseError = true)("sudo shutdown now")

  private def executeInitialize: IO[Unit] =
    eventHooks.flatMap(_.onInitialize).fold(IO.unit)(runCommand(raiseError = false))

  private def executeStop: IO[Unit] =
    List(
      mopidyClient.stopPlayback() >>
        mopidyClient.clearTracklist(),
      eventHooks.flatMap(_.onStop).fold(IO.unit)(runCommand(raiseError = false))
    ).parSequence
      .as(())
}
