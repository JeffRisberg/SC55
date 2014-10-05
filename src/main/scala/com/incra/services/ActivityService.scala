package com.incra.services

// Use H2Driver to connect to an H2 database

import com.incra.model.FirstExample
import com.incra.model.FirstExample.Activity

import scala.slick.driver.H2Driver.simple._

/**
 * @author Jeff Risberg
 * @since 09/10/2014
 */
object ActivityService {

  def getEntityList(): List[Activity] = {
    Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
      implicit session =>
        // <- write queries here
        System.out.println("have session")
        (FirstExample.activities.ddl).create

        FirstExample.activities +=(101, "Hiking", "", "miles")

        FirstExample.activities +=(102, "Walking", "", "steps")
        System.out.println("end session")
    }

    var activities = scala.collection.mutable.ListBuffer.empty[Activity]

    // Create the tables, including primary and foreign keys


    // Insert some suppliers
    //activities += new Activity("Pilates", "", "times")
    //activities += new Activity("Biking", "", "miles")
    //activities += new Activity("Spins", "", "minutes")
    //activities += new Activity("Exercise", "", "minutes")
    activities.toList
  }
}
