package com.incra.infrastructure

/**
 * Hikari hangs when a JVM application tries to open 2 connection pools at exactly the same time.
 * This lock is used by the DataSource factories (MySQLConnectionFactory & RedshiftConnectionFactory)
 * to make sure that nasty race condition does not happen.
 */
object DataSourceLock {
  val Lock = new Object
}
