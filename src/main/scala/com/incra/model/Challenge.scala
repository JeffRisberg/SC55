package com.incra.model

import java.sql.Date

import com.incra.model.TeamworkType.TeamworkType

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Challenge entity
 *
 * @author Jeff Risberg
 * @since 06/11/2014
 */
case class Challenge(id: Option[Long], name: String, teamworkType: TeamworkType,
                     startDate: Date, endDate: Date, active: Boolean) extends Entity[Long]

class ChallengeTable(tag: Tag) extends Table[Challenge](tag, "CHALLENGE") {

  implicit val teamworkTypeMapper = MappedColumnType.base[TeamworkType, Long](_.value, TeamworkType.withKey(_))

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def teamworkType = column[TeamworkType]("TEAMWORK_TYPE")

  def startDate = column[Date]("START_DATE")

  def endDate = column[Date]("END_DATE")

  def active = column[Boolean]("ACTIVE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, teamworkType, startDate, endDate, active) <>(Challenge.tupled, Challenge.unapply _)
}
