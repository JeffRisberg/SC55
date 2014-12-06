package com.incra.init

import com.escalatesoft.subcut.inject.NewBindingModule
import com.incra.services.{LeaderboardService, ChallengeService, ActivityService}

/**
 * Dependency injection configuration for services.
 */
object ServiceConfiguration extends NewBindingModule(mutableBindingModule => {
  import mutableBindingModule._

  //bind[AnalyticService] toProvider {
  //  implicit bindingModule => new RdsAnalyticService
  //}
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
