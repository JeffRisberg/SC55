package com.incra.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Leaderboard entity
 *
 * @author Jeff Risberg
 * @since 10/12/2014
 */
case class Leaderboard(id: Option[Long], name: String, direction: Direction) extends Entity[Long]

class LeaderboardTable(tag: Tag) extends Table[Leaderboard](tag, "LEADERBOARD") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def direction = column[Direction]("DIRECTION")

  implicit val directionMapper = MappedColumnType.base[Direction, String](_.value, Direction(_))

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, direction) <>(Leaderboard.tupled, Leaderboard.unapply _)
}
