package com.incra.services

import java.util.Date

import com.incra.model.Challenge

import scala.collection.mutable.ListBuffer

/**
 * @author Jeff Risberg
 * @since 09/10/2014
 */
object ChallengeService {
  def getEntityList(): List[Challenge] = {
    var challenges = ListBuffer.empty[Challenge]

    challenges += new Challenge("Walk to the Moon", new Date(213, 5, 6), new Date(), true)
    challenges += new Challenge("Fall Hiking", new Date(213, 3, 4), new Date(), false)
    challenges += new Challenge("Holiday Ship-Shape", new Date(213, 11, 25), new Date(), false)

    challenges.toList
  }
}