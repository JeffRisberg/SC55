package com.incra.services

import com.incra.model.Activity

import scala.collection.mutable.ListBuffer

/**
 * @author Jeff Risberg
 * @since 09/10/2014
 */
object ActivityService {

  def getEntityList(): List[Activity] = {
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
