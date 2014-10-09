package com.incra.services

// Use H2Driver to connect to an H2 database

import com.incra.model._

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 09/10/2014
 */
object ActivityService {

  println("InitActivityService")
  Database.forURL("jdbc:h2:file:test1", driver = "org.h2.Driver") withSession {
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
    Database.forURL("jdbc:h2:file:test1", driver = "org.h2.Driver") withSession {
      implicit session =>

        TableQuery[ActivityTable].list
    }
  }
}
