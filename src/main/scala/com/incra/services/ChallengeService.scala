package com.incra.services

import java.sql.Date

import com.incra.model.{Challenge, ChallengeTable}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/28/2014
 */
object ChallengeService {

  println("InitChallengeService")
  Database.forURL("jdbc:mysql://localhost:3306/sc55",
    user = "developer", password = "123456", driver = "com.mysql.jdbc.Driver") withSession {
    implicit session =>
      val challenges = TableQuery[ChallengeTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("challenge").list().isEmpty) {
        (challenges.ddl).create

        challenges += Challenge(Some(101), "Walk to the Moon", new Date(113, 5, 6), new Date(113, 6, 8), true)
        challenges += Challenge(None, "Fall Hiking", new Date(113, 8, 1), new Date(113, 10, 20), false)
        challenges += Challenge(None, "Holiday Ship-Shape", new Date(113, 10, 1), new Date(114, 1, 3), false)
      }
  }
  println("EndInitChallengeService")

  /**
   *
   */
  def getEntityList(): List[Challenge] = {
    Database.forURL("jdbc:mysql://localhost:3306/sc55",
      user = "developer", password = "123456", driver = "com.mysql.jdbc.Driver") withSession {

        TableQuery[ChallengeTable].list
    }
  }
}
