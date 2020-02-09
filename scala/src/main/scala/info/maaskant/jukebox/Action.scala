package info.maaskant.jukebox

import info.maaskant.jukebox.mopidy.MopidyUri

sealed trait Action

object Action {

  case object Pause extends Action

  case class Play(uri: MopidyUri) extends Action

  case object Resume extends Action

  case object Stop extends Action

}