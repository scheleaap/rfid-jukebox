package org.pmw.tinylog

import java.lang.{String => JString}

import com.typesafe.scalalogging.StrictLogging

object Logger extends StrictLogging {
  def debug(message: JString): Unit = logger.debug(message)

  def debug(message: JString, data: Array[Object]): Unit = logger.debug(message, data)

  def error(message: JString): Unit = logger.error(message)

  def error(message: JString, data: Array[Object]): Unit = logger.error(message, data)

  def info(message: JString): Unit = logger.warn(message)

  def info(message: JString, data: Array[Object]): Unit = logger.warn(message, data)

  def warn(message: JString): Unit = logger.warn(message)

  def warn(message: JString, data: Array[Object]): Unit = logger.warn(message, data)
}
