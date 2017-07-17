package net.petitviolet.anost.adapter.support

import com.typesafe.config.{ Config, ConfigFactory }
import pureconfig.ConfigReader

case class HttpConfig(host: String, port: Int)

case class ControllerConfig(parallelism: Int)

trait UsesControllerConfig extends UsesConfig {
  def configKey: String

  lazy val controllerConfig: ControllerConfig = config.load(s"controller.$configKey")(ConfigReader[ControllerConfig])
}

trait UsesHttpConfig extends UsesConfig {
  final private val key: String = "http"
  val httpConfig: HttpConfig = config.load(key)(ConfigReader[HttpConfig])
}

trait UsesConfig {
  private[support] val config = new PConfig(ConfigFactory.load())
}

object ConfigContainer {
  lazy val config: Config = ConfigFactory.load()
}
private[support] class PConfig(val c: Config) extends AnyVal {
  def load[C](key: String)(implicit reader: ConfigReader[C]): C =
    pureconfig.loadConfig[C](c.getConfig(key))(reader) match {
      case Right(conf) => conf
      case Left(t) => throw new RuntimeException(s"failed to load $key. config: $c. err: $t")
    }
}

