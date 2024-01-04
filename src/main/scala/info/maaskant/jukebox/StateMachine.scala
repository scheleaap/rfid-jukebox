package info.maaskant.jukebox

import info.maaskant.jukebox.Action.{Initialize, Pause, Play, Resume}
import info.maaskant.jukebox.mopidy.MopidyUri

import java.time.{Clock, Duration, Instant}
import scala.concurrent.duration.FiniteDuration

case class StateMachine(streamPauseTimeout: FiniteDuration) {

  sealed trait State {
    def apply(input: Card, clock: Clock = Clock.systemUTC()): (State, Option[Action])
  }

  case object Uninitialized extends State {
    override def apply(input: Card, clock: Clock): (State, Option[Action]) =
      Stopped -> Some(Initialize)
  }

  case object Stopped extends State {
    override def apply(input: Card, clock: Clock): (State, Option[Action]) = input match {
      case Card.None => this -> None
      case _: Card.Unknown => this -> None
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => this -> None
      case Card.Album(spotifyUri) => Playing(spotifyUri) -> Some(Play(spotifyUri))
    }
  }

  case class Playing(currentUri: MopidyUri) extends State {
    override def apply(input: Card, clock: Clock): (State, Option[Action]) = input match {
      case Card.None => Paused(currentUri, Instant.now(clock)) -> Some(Pause)
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case _: Card.Unknown => this -> None
      case Card.Album(newUri) =>
        if (currentUri == newUri) {
          this -> None
        } else {
          Playing(newUri) -> Some(Play(newUri))
        }
    }
  }

  case class Paused(lastUri: MopidyUri, since: Instant) extends State {
    override def apply(input: Card, clock: Clock): (State, Option[Action]) = input match {
      case Card.None => this -> None
      case Card.Shutdown => this -> Some(Action.Shutdown)
      case Card.Stop => Stopped -> Some(Action.Stop)
      case _: Card.Unknown => this -> None
      case Card.Album(newUri) =>
        if (
          lastUri == newUri && (if (lastUri.value.startsWith("http://"))
                                  Duration
                                    .between(since, Instant.now(clock))
                                    .abs()
                                    .toMillis < streamPauseTimeout.toMillis
                                else true)
        ) {
          Playing(newUri) -> Some(Resume)
        } else {
          Playing(newUri) -> Some(Play(newUri))
        }
    }
  }

}
