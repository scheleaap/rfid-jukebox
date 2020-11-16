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

  case class Running(start: LocalDateTime, numConsecutiveNones: Int = 0, finish: Option[LocalDateTime] = None)
      extends State {
    override def apply(input: Option[Card]): (State, Option[Action]) = input match {
      case Some(_) => this.copy(numConsecutiveNones = 0, finish = None) -> None
      case None =>
        if (numConsecutiveNones < 10) {
          val newFinish = finish match {
            case None => Some(LocalDateTime.now())
            case _ => finish
          }
          this.copy(numConsecutiveNones = this.numConsecutiveNones + 1, finish = newFinish) -> None
        } else {
          Finished(start, finish.getOrElse(LocalDateTime.now())) -> None
        }
    }
  }

  case class Finished(start: LocalDateTime, finish: LocalDateTime) extends State {
    val duration: Long = SECONDS.between(start, finish)
    override def apply(input: Option[Card]): (State, Option[Action]) = input match {
      case _ => this -> None
    }
  }

}
