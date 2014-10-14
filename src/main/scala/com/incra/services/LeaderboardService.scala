package com.incra.services

import java.sql.Date

import com.incra.app.MainServlet
import com.incra.model.{Direction, LeaderboardTable, Leaderboard, TeamworkType}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
object LeaderboardService {

  println("InitLeaderboardService")
  Database.forURL(MainServlet.url,
    user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
    implicit session =>
      val leaderboards = TableQuery[LeaderboardTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("leaderboard").list().isEmpty) {
        (leaderboards.ddl).create

        leaderboards += Leaderboard(None, "Fall Hiking", Direction.Descending)
        leaderboards += Leaderboard(Some(101), "Walk to the Moon", Direction.Ascending)
        leaderboards += Leaderboard(None, "Holiday Ship-Shape", Direction.Descending)
      }
  }
  println("EndInitLeaderboardService")

  /**
   *
   */
  def getEntityList(): List[Leaderboard] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[LeaderboardTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Leaderboard] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[LeaderboardTable].where(_.id === id).firstOption
    }
  }
}
