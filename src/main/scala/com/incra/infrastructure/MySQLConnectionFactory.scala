package com.incra.infrastructure

import javax.sql.DataSource

import com.incra.infrastructure.DefaultOperator._
import com.typesafe.config.{Config, ConfigFactory}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

/**
 * A Connection Factory to MySQL - using HikariCP to manage the connection pool.
 *
 * @author various
 * @since 6/21/13
 */
class MySQLConnectionFactory(dataSourceOpt: Option[DataSource]) extends DBConnectionFactory
with Logging {
  private val dataSource: DataSource = dataSourceOpt match {
    case Some(ds) => ds
    case None => MySQLConnectionFactory.createDataSource(ConfigFactory.empty)
  }

  def this(dataSource: DataSource) = this(Some(dataSource))

  def this(config: Config) = this(Some(MySQLConnectionFactory.createDataSource(config)))

  /**
   * Get a DB Connection.
   * @return a DB Connection.
   */
  def getConnection = {
    println("getConnection " + dataSource)
    dataSource match {
      case ds: HikariDataSource =>
        log.info("getting MySQL data source: %s".format(ds.getJdbcUrl))
    }
    dataSource.getConnection
  }

  /**
   * Shutdown the connection factory.
   */
  def shutdown() {
    dataSource match {
      case ds: HikariDataSource =>
        log.info("shutting down MySQL data source: %s".format(ds.getJdbcUrl))
        ds.close()
      case _ => // do nothing because there's no close() method in DataSource interface
    }
  }
}

object MySQLConnectionFactory extends Logging {
  def createDataSource(config: Config) = {
    DataSourceLock.Lock.synchronized {
      val minConnection = config.getInt("minConnections") default 0
      val maxConnection = config.getInt("maxConnections") default 5

      val hcfg = new HikariConfig()
      hcfg.setDriverClassName(config.getString("driver.class") default "com.mysql.jdbc.Driver")

      val url = config.getString("url") default "jdbc:mysql://localhost:3306/sc55"
      hcfg.setJdbcUrl(url + "?allowMultiQueries=true")

      hcfg.setUsername(config.getString("username") default "developer")
      hcfg.setPassword(config.getString("password") default "123456")
      hcfg.setMinimumIdle(minConnection)
      hcfg.setMaximumPoolSize(maxConnection)
      hcfg.setConnectionTimeout(config.getLong("connection.timeout.ms") default 30000)
      hcfg.setIdleTimeout(config.getLong("connection.idle.timeout.ms") default 600000) // 10 mins
      hcfg.setMaxLifetime(config.getLong("connection.max.lifetime.ms") default 1800000) // 30 mins
      hcfg.setConnectionTestQuery("SELECT 1")

      // used to monitor the DataSource
      hcfg.setRegisterMbeans(true)

      new HikariDataSource(hcfg)
    }
  }
}