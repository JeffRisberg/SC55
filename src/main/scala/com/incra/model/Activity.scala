package com.incra.model

import scala.slick.driver.H2Driver.simple._

/**
 * Definition of the Activity entity
 *
 * @author Jeffrey Risberg
 * @since 9/10/2014
 */
case class Activity(id: Option[Int], name: String, description: String, uom: String)

class ActivityTable(tag: Tag) extends Table[Activity](tag, "ACTIVITY") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def description = column[String]("DESCRIPTION")

  def uom = column[String]("UOM")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, description, uom) <>(Activity.tupled, Activity.unapply _)
}
