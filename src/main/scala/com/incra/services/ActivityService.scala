package com.incra.services

// Use H2Driver to connect to an H2 database

import com.incra.model._

import scala.slick.driver.H2Driver.simple._

/**
 * @author Jeff Risberg
 * @since 09/10/2014
 */
object ActivityService {

  System.out.println("InitActivityService")
  Database.forURL("jdbc:h2:file:test1", driver = "org.h2.Driver") withSession {
    implicit session =>
      val activities = TableQuery[Activity]

      // Create the tables, including primary and foreign keys
      (activities.ddl).create

      activities +=(101, "Hiking", "", "miles")
      activities +=(102, "Walking", "", "steps")
      activities +=(103, "Spins", "", "minutes")
      activities +=(104, "Exercise", "", "minutes")
  }
  System.out.println("EndInitActivityService")

  def getEntityList(): List[Activity] = {
    var result = scala.collection.mutable.ListBuffer.empty[Activity]

    Database.forURL("jdbc:h2:file:test1", driver = "org.h2.Driver") withSession {
      implicit session =>

        val activities = TableQuery[Activity]

        activities foreach { case (id, name, description, uom) =>
          println("  " + name + "\t" + description + "\t" + uom)
        }
        System.out.println(activities)
    }

    result.toList
  }
}
