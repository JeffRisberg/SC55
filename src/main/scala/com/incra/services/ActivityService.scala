package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.app.MainServlet
import com.incra.model.{ActivityTable, Activity}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class ActivityService(implicit val bindingModule: BindingModule) extends Injectable {
  //private def dbOperation = inject[DBOperation](RDS)
  
  println("InitActivityService")
  Database.forURL(MainServlet.url,
    user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
    implicit session =>
      val activities = TableQuery[ActivityTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("activity").list().isEmpty) {
        (activities.ddl).create

        activities += Activity(None, "Hiking", "Climb the mountain", "miles")
        activities += Activity(Some(102), "Walking", "Go out and walk", "steps")
        activities += Activity(None, "Spins", "Go to class at the gym", "minutes")
        activities += Activity(Some(104), "Exercise", "Do whatever you want", "minutes")
      }
  }
  println("EndInitActivityService")

  /**
   *
   */
  def getEntityList(): List[Activity] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[ActivityTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Activity] = {
    Database.forURL(MainServlet.url,
      user = MainServlet.user, password = MainServlet.password, driver = MainServlet.driver) withSession {
      implicit session =>

        TableQuery[ActivityTable].where(_.id === id).firstOption
    }
  }
}
