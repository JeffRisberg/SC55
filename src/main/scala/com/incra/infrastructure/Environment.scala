package com.incra.infrastructure

/**
 * Environment.
 *
 * @param isProduction true if the current environment is production; false otherwise.
 * @param isStaging true if the current environment is staging; false otherwise.
 * @param isTest true if the current environment is test; false otherwise.
 * @param isDev true if the current environment is dev; false otherwise.
 *
 * @author various
 * @since 02/12/14
 */
case class Environment(isProduction: Boolean, isStaging: Boolean, isTest: Boolean, isDev: Boolean)

object Environment {
  val Production = "production"
  val Staging = "staging"
  val Test = "test"
  val Dev = "dev"

  def apply(environment: String): Environment = Environment(
    environment == Environment.Production,
    environment == Environment.Staging,
    environment == Environment.Test,
    environment == Environment.Dev)
}