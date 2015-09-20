package com.incra.infrastructure

import grizzled.slf4j.Logger

/**
 * A logging trait that uses the name of the underlying class as the name of the log.
 *
 * @author various
 * @since 06/16/2013
 */
trait Logging {
  lazy val log = Logger(getClass)
  lazy val sqlLog = Logger("sql." + getClass.getName)

  def logSQL(sql: String) {
    sqlLog.info(s"-----------------------------------------------------------------------------------------\n $sql")
  }
}
