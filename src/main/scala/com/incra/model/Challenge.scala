package com.incra.model

import java.sql.Date

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Challenge entity
 *
 * @author Jeff Risberg
 * @since 09/11/2014
 */
case class Challenge(id: Option[Int], name: String, teamworkType: TeamworkType,
                     startDate: Date, endDate: Date, active: Boolean)

class ChallengeTable(tag: Tag) extends Table[Challenge](tag, "CHALLENGE") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def teamworkType = column[TeamworkType]("TEAMWORK_TYPE")

  def startDate = column[Date]("START_DATE")

  def endDate = column[Date]("END_DATE")

  def active = column[Boolean]("ACTIVE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, teamworkType, startDate, endDate, active) <>(Challenge.tupled, Challenge.unapply _)
}
