package com.incra.infrastructure

import com.typesafe.config.{Config, ConfigException, ConfigFactory}

import scala.util.control.Exception._

/**
 * Configuration support functionality.
 *
 * @author various
 * @since 09/26/12
 */
object ConfigSupport {

  implicit class DecoratedConfigWithGetConfigOrEmpty(config: Config) {
    def getOptionConfig(path: String) = catching(classOf[ConfigException.Missing]).opt(config.getConfig(path))

    def getConfigOrEmpty(path: String) = getOptionConfig(path).getOrElse(ConfigFactory.empty)
  }

}
