package net.petitviolet.anost.support

import org.slf4j.LoggerFactory

import enumeratum._

/**
 * Log base class
 * mix-in `UsesLogger` to use AnostLogger
 */
sealed trait AnostLogger {
  protected val loggerKey: String = getClass.getName
  private lazy val logger = LoggerFactory.getLogger(loggerKey)

  def isDebugEnabled: Boolean = logger.isDebugEnabled

  def debugStackTrace(t: => Throwable): Unit = {
    if (logger.isDebugEnabled) {
      logger.debug("---------------------")
      logger.debug(t.getStackTrace.mkString("\n"))
      logger.debug("---------------------")
    }
  }

  def debug(msg: => String)(implicit line: sourcecode.Line, enclosing: sourcecode.Enclosing): Unit =
    if (logger.isDebugEnabled) {
      // add logging tag
      val clazz = enclosing.value.split('.').last
      val tag = s"<$clazz:${line.value}>"
      logger.debug(s"$tag $msg")
    }

  def info(msg: => String): Unit =
    if (logger.isInfoEnabled) logger.info(msg)

  def warn(msg: => String): Unit =
    if (logger.isWarnEnabled) logger.warn(msg)

  def warn(msg: => String, e: => Throwable): Unit =
    if (logger.isWarnEnabled) logger.warn(msg, e)

  def error(msg: => String): Unit =
    if (logger.isErrorEnabled) logger.error(msg)

  def error(msg: => String, e: => Throwable): Unit =
    if (logger.isErrorEnabled) logger.error(msg, e)

  def withTimeTrackLog[T](msg: => String)(snippet: => T): T = {
    if (logger.isDebugEnabled) {
      logger.debug(s"[P][START] $msg")
      val start = System.nanoTime()
      val _result = snippet
      val end = System.nanoTime()
      logger.debug(s"[P][END](${(end - start) / 100000.0} ms) $msg")
      _result
    } else snippet
  }

  def log(logLevel: LogLevel): (=> String) => Unit = {
    import LogLevel._
    logLevel match {
      case DEBUG => debug _
      case INFO => info _
      case WARN => warn _
      case ERROR => error
    }
  }

  private def isLogEnable(logLevel: LogLevel): Boolean = {
    import LogLevel._
    logLevel match {
      case DEBUG => logger.isDebugEnabled
      case INFO => logger.isInfoEnabled()
      case WARN => logger.isWarnEnabled()
      case ERROR => logger.isErrorEnabled()
    }
  }

  def withTrackLog[T](msg: => String, logLevel: LogLevel)(snippet: => T): T = {
    if (!isLogEnable(logLevel)) {
      snippet
    } else {
      val _log = log(logLevel)
      val _msg = msg
      _log(s"[START] ${_msg}")
      val r = snippet
      _log(s"[END] ${_msg}")
      r
    }
  }
}

sealed abstract class LogLevel(val value: String) extends EnumEntry {
  val symbol = Symbol(value)
}

object LogLevel extends Enum[LogLevel] {
  case object DEBUG extends LogLevel("debug")
  case object INFO extends LogLevel("info")
  case object WARN extends LogLevel("warn")
  case object ERROR extends LogLevel("error")

  override val values = findValues
}

trait UsesLogger {
  val logger: AnostLogger
}

trait MixInLogger {
  val logger: AnostLogger = AnostLoggerImpl
}

private object AnostLoggerImpl extends AnostLogger {
  override protected val loggerKey: String = "RavenLogger"
}

