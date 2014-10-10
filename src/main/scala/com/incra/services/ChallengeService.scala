package com.incra.services

import java.sql.Date

import com.incra.app.MainServlet
import com.incra.model.{ChallengeTable, Challenge}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
object ChallengeService {

  println("InitChallengeService")
  Database.forURL(MainServlet.url,
    user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
    implicit session =>
      val challenges = TableQuery[ChallengeTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("challenge").list().isEmpty) {
        (challenges.ddl).create

        challenges += Challenge(None, "Fall Hiking", new Date(113, 8, 1), new Date(113, 10, 20), false)
        challenges += Challenge(Some(101), "Walk to the Moon", new Date(113, 5, 6), new Date(113, 6, 8), true)
        challenges += Challenge(None, "Holiday Ship-Shape", new Date(113, 10, 1), new Date(114, 1, 3), false)
      }
  }
  println("EndInitChallengeService")

  /**
   *
   */
  def getEntityList(): List[Challenge] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[ChallengeTable].list
    }
  }

  /**
   *
   */
  def findById(id: Int): Option[Challenge] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[ChallengeTable].where(_.id === id).firstOption
    }
  }
}
