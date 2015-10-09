package com.incra.model

import com.incra.model.Direction.Direction

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Leaderboard entity
 *
 * @author Jeff Risberg
 * @since 10/12/2014
 */
case class Leaderboard(id: Option[Long], name: String, direction: Direction) extends Entity[Long]

class LeaderboardTable(tag: Tag) extends Table[Leaderboard](tag, "LEADERBOARD") {

  implicit val directionMapper = MappedColumnType.base[Direction, String](_.value, Direction.withKey(_))

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def direction = column[Direction]("DIRECTION")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, direction) <>(Leaderboard.tupled, Leaderboard.unapply _)
}

object LeaderboardMarshaller {
  def unmarshall(document: Map[String, Any]) = {
    Leaderboard(
      Some(document.get("ID")).asInstanceOf[Option[Long]],
      document.get("NAME").get.asInstanceOf[String],
      Direction.withKey(document.get("DIRECTION").get.asInstanceOf[String])
    )
  }

  def marshall(leaderboard: Leaderboard) = {
    Map(
      "id" -> leaderboard.id.getOrElse(None),
      "name" -> leaderboard.name,
      "direction" -> leaderboard.direction.value
    )
  }
}