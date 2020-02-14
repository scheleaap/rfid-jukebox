package org.pmw.tinylog

import java.lang.{String => JString}

import com.typesafe.scalalogging.StrictLogging

/**
 * Tinylog adapter for the MFRC522 class.
 *
 * Because MFRC522 logs things on ERROR that are not really errors, they are mapped to DEBUG.
 */
object Logger extends StrictLogging {
  def debug(message: JString): Unit = logger.debug(message)

  def debug(message: JString, data: Array[Object]): Unit = logger.debug(message, data)

  def error(message: JString): Unit = logger.debug(message)

  def error(message: JString, data: Array[Object]): Unit = logger.debug(message, data)

  def info(message: JString): Unit = logger.warn(message)

  def info(message: JString, data: Array[Object]): Unit = logger.warn(message, data)

  def warn(message: JString): Unit = logger.warn(message)

  def warn(message: JString, data: Array[Object]): Unit = logger.warn(message, data)
}
