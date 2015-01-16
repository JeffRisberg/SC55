package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model.{Direction, LeaderboardTable, Leaderboard, TeamworkType}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class LeaderboardService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  println("InitLeaderboardService")
  mainDatabase withSession {
    implicit session =>
      val leaderboards = TableQuery[LeaderboardTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("leaderboard").list().isEmpty) {
        (leaderboards.ddl).create

        leaderboards += Leaderboard(None, "Team Leaders", Direction.Descending)
        leaderboards += Leaderboard(Some(101), "All-Time Winners", Direction.Ascending)
        leaderboards += Leaderboard(None, "Recent Gainers", Direction.Descending)
      }
  }
  println("EndInitLeaderboardService")

  /**
   *
   */
  def getEntityList(): List[Leaderboard] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[LeaderboardTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Leaderboard] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[LeaderboardTable].where(_.id === id).firstOption
    }
  }
}
