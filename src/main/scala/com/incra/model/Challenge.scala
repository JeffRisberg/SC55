package com.incra.model

import java.sql.Date

import scala.slick.driver.H2Driver.simple._

/**
 * Definition of the Challenge entity
 *
 * @author Jeff Risberg
 * @since 09/10/2014
 */
case class Challenge(id: Int, name: String, startDate: Date, endDate: Date, active: Boolean)

class ChallengeTable(tag: Tag) extends Table[Challenge](tag, "CHALLENGE") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[String]("NAME")

  def startDate = column[Date]("START_DATE")

  def endDate = column[Date]("END_DATE")

  def active = column[Boolean]("ACTIVE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, startDate, endDate, active) <> (Challenge.tupled, Challenge.unapply _)
}
