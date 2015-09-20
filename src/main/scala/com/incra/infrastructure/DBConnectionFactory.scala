package com.incra.infrastructure

import java.sql.Connection

/**
 * Connection Factory to a SQL DB.
 */
trait DBConnectionFactory {
  /**
   * Get a DB Connection.
   * @return a DB Connection.
   */
  def getConnection: Connection

  /**
   * Shutdown the connection factory.
   */
  def shutdown()
}