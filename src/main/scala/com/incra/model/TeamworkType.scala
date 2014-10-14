package com.incra.model

import scala.slick.lifted.MappedTo

/**
 * TeamworkType for a Challenge.
 *
 * @author Jeff Risberg
 * @since 10/04/14
 */
object TeamworkType extends Enumeration[Long, TeamworkType] {

  object Individual extends TeamworkType(1L, "Individual")

  object Team extends TeamworkType(2L, "Team")

  val list = List(Individual, Team)
}

case class TeamworkType(value: Long, name: String) extends MappedTo[Long]
