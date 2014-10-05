package com.incra.model

// Use H2Driver to connect to an H2 database

import scala.slick.driver.H2Driver.simple._

/**
 * @author Jeffrey Risberg
 * @since 9/10/2014
 */

object FirstExample extends App {

  // Definition of the SUPPLIERS table
  class Activity(tag: Tag) extends Table[(Int, String, String, String)](tag, "ACTIVITY") {
    def id = column[Int]("ID", O.PrimaryKey)

    // This is the primary key column
    def name = column[String]("NAME")

    def description = column[String]("DESCRIPTION")

    def uom = column[String]("UOM")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, description, uom)
  }

  val activities = TableQuery[Activity]
}