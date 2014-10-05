package com.incra.services

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import com.incra.model.Activity

import scala.collection.mutable.ListBuffer

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
    }
    var activities = ListBuffer.empty[Activity]

    activities += new Activity("Hiking", "", "miles")
    activities += new Activity("Walking", "", "steps")
    activities += new Activity("Pilates", "", "times")
    activities += new Activity("Biking", "", "miles")
    activities += new Activity("Spins", "", "minutes")
    activities += new Activity("Exercise", "", "minutes")
    activities.toList
  }
}
