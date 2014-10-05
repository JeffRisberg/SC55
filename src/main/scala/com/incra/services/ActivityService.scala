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
  Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
    implicit session =>
      // <- write queries here
      System.out.println("have session")
  }
  System.out.println("EndInitActivityService")

  def getEntityList(): List[Activity] = {
    System.out.println("getEntityList")

    Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
      implicit session =>
        // <- write queries here
        System.out.println("have session")

        val activities = TableQuery[Activity]

        // Create the tables, including primary and foreign keys
        (activities.ddl).create

        activities +=(101, "Hiking", "", "miles")
        activities +=(102, "Walking", "", "steps")
        activities +=(103, "Spins", "", "minutes")
        activities +=(104, "Exercise", "", "minutes")

        println("Activities:")
        activities foreach { case (id, name, description, uom) =>
          println("  " + name + "\t" + description + "\t" + uom)
        }

        System.out.println("end session")
    }

    var activities = scala.collection.mutable.ListBuffer.empty[Activity]

    activities.toList
  }
}
