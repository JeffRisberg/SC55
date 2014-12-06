package com.incra.init

import com.escalatesoft.subcut.inject.NewBindingModule
import com.incra.app.MainServlet
import com.incra.services.{LeaderboardService, ChallengeService, ActivityService}

import scala.slick.driver.MySQLDriver.simple._

/**
 * Dependency injection configuration for services.
 */
object ServiceConfiguration extends NewBindingModule(mutableBindingModule => {
  import mutableBindingModule._

  val url = "jdbc:mysql://localhost:3306/sc55"
  val driver = "com.mysql.jdbc.Driver"
  val user = "developer"
  val password = "123456"

  bind[Database] toProvider {
    implicit bindingModule => Database.forURL(url, user = user, password = password, driver = driver)
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
