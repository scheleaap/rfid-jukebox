package info.maaskant.jukebox

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS

import info.maaskant.jukebox.rfid.Card

sealed trait State {
  def apply(input: Option[Card]): (State, Option[Action])
}

object State {

  case object Starting extends State {
    override def apply(input: Option[Card]): (State, Option[Action]) =
      Running(LocalDateTime.now()) -> None
  }

  case class Running(start: LocalDateTime) extends State {
    override def apply(input: Option[Card]): (State, Option[Action]) = input match {
      case Some(_) => this -> None
      case None => Finished(start, LocalDateTime.now()) -> None
    }
  }

  case class Finished(start: LocalDateTime, finish: LocalDateTime) extends State {
    val duration: Long = SECONDS.between(start, finish)
    override def apply(input: Option[Card]): (State, Option[Action]) = input match {
      case _ => this -> None
    }
  }

}
