package info.maaskant.jukebox

import cats.effect.IO

trait ActionExecutor {
  def executeAction(action: Action): IO[Boolean]
}
