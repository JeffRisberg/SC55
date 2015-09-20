package com.incra.init

import javax.sql.DataSource

import com.escalatesoft.subcut.inject._
import com.incra.infrastructure.BindingIds.MySQL
import com.incra.infrastructure.ConfigSupport._
import com.incra.infrastructure.{DBConnectionFactory, DBOperation, MySQLConnectionFactory}
import com.incra.services.{ActivityService, ChallengeService, LeaderboardService}
import com.typesafe.config.{Config, ConfigFactory}

import scala.slick.driver.MySQLDriver.simple._

/**
 * Dependency injection configuration for services.
 */
object ServiceConfiguration extends NewBindingModule(mutableBindingModule => {
  import mutableBindingModule._

  bind[Config] toSingle ConfigFactory.load("config.json")

  bind[DataSource] idBy MySQL toModuleSingle {
    bindingModule =>
      val config = bindingModule.inject[Config](None)
      MySQLConnectionFactory.createDataSource(config.getConfigOrEmpty("mysql"))
  }

  /** This is used by Slick-based services */
  bind[Database] toProvider {
    bindingModule =>
      val dataSource = bindingModule.inject[DataSource](Some(MySQL))
      Database.forDataSource(dataSource)
  }

  bind[DBConnectionFactory] idBy MySQL toModuleSingle {
    bindingModule =>
      val dataSource = bindingModule.inject[DataSource](Some(MySQL))
      new MySQLConnectionFactory(dataSource)
  }

  /** This is used by non-Slick-based services */
  bind[DBOperation] idBy MySQL toProvider {
    bindingModule =>
      val dbConnectionFactory = bindingModule.inject[DBConnectionFactory](Some(MySQL))
      new DBOperation(dbConnectionFactory)
  }

  bind[ActivityService] toProvider {
    implicit bindingModule => new ActivityService
  }

  bind[ChallengeService] toProvider {
    implicit bindingModule => new ChallengeService
  }

  bind[LeaderboardService] toProvider {
    implicit bindingModule => new LeaderboardService
  }
})
