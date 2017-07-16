package net.petitviolet.anost.adapter

import com.typesafe.config.{ Config, ConfigFactory }

case class HttpConfig(host: String, post: Int)

case class ControllerConfig(parallelism: Int)

trait UsesControllerConfig {
  def key: String
  lazy val controllerConfig: ControllerConfig = ControllerConfig.findByKey(s"controller.$key")
}

object ControllerConfig {
  def findByKey(key: String): ControllerConfig = {
    val conf = ConfigContainer.config
    pureconfig.loadConfig[ControllerConfig](conf.getConfig(key)) match {
      case Right(c) => c
      case Left(t) => throw new RuntimeException(s"failed to load $key. config: $conf")
    }
  }
}

private object ConfigContainer {
  val config: Config = ConfigFactory.load()
}
