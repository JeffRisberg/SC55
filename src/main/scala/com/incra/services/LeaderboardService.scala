package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.infrastructure.DBOperation
import com.incra.model._

import com.incra.infrastructure.BindingIds._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * This is a non-Slick-based service (except for the table initialization)
 *
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class LeaderboardService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  private def dbOperation = bindingModule.inject[DBOperation](Some(MySQL))

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
    //val dbOperation = bindingModule.inject[DBOperation](Some(MySQL))

    val result = dbOperation.executeQueryWithStatement("select * from leaderboard")

    result.map(LeaderboardMarshaller.unmarshall)
  }

  /**
   *
   */
  def findById(id: Long): Option[Leaderboard] = {
    //val dbOperation = bindingModule.inject[DBOperation](Some(MySQL))

    val result = dbOperation.executeQueryWithStatement("select * from leaderboard where id = " + id)

    if (result.size > 0)
      Some(LeaderboardMarshaller.unmarshall(result(0)))
    else
      None
  }
}
