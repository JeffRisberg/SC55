package com.incra.services

import com.incra.model._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/28/2014
 */
object ActivityService {

  println("InitActivityService")
  Database.forURL("jdbc:mysql://localhost:3306/sc55",
    user = "developer", password = "123456", driver = "com.mysql.jdbc.Driver") withSession {
    implicit session =>
      val activities = TableQuery[ActivityTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("activity").list().isEmpty) {
        (activities.ddl).create

        activities += Activity(Some(101), "Hiking", "", "miles")
        activities += Activity(Some(102), "Walking", "", "steps")
        activities += Activity(Some(103), "Spins", "", "minutes")
        activities += Activity(Some(104), "Exercise", "", "minutes")
      }
  }
  println("EndInitActivityService")

  /**
   *
   */
  def getEntityList(): List[Activity] = {
    Database.forURL("jdbc:mysql://localhost:3306/sc55",
      user = "developer", password = "123456", driver = "com.mysql.jdbc.Driver") withSession {
      implicit session =>

        TableQuery[ActivityTable].list
    }
  }
}
