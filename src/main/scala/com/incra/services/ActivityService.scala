package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model._
import slick.driver.MySQLDriver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * This is a Slick-based service
 *
 * @author Jeff Risberg
 * @since 10/08/2014 (updated 09/30/15)
 */
class ActivityService(implicit val bindingModule: BindingModule) extends Injectable {
  private def database = inject[Database]

  println("InitActivityService")
  val activities = TableQuery[ActivityTable]

  val tables = Await.result(database.run(MTable.getTables), Duration.Inf).toList

  val thisTableExists = tables.exists { table => table.name.name == "ACTIVITY"}

  if (thisTableExists == false) {
    val setupAction: DBIO[Unit] = DBIO.seq(
      activities.schema.create
    )

    val setupFuture: Future[Unit] = database.run(setupAction)

    val populateAction: DBIO[Option[Int]] = activities ++= Seq(
      Activity(None, "Hiking", "Climb any mountain", "miles"),
      Activity(Some(102), "Walking", "Go out and walk", "steps"),
      Activity(None, "Spins", "Go to class at the fitness center", "minutes"),
      Activity(Some(104), "Exercise", "Do whatever you want", "minutes"))

    val populateFuture: Future[Option[Int]] = database.run(populateAction)

    Await.result(setupFuture flatMap { _ => populateFuture }, Duration.Inf)
  }
  println("EndInitActivityService")

  def filterQuery(id: Long): Query[ActivityTable, Activity, Seq] =
    activities.filter(_.id === id)

  /**
   *
   */
  def getEntityList(): Seq[Activity] = {
    val activities = TableQuery[ActivityTable]

    Await.result(database.run(activities.result), Duration.Inf)
  }

  /**
   * @param id
   */
  def findById(id: Long): Option[Activity] = {
    val records = Await.result(database.run(filterQuery(id).result), Duration.Inf)

    if (records.isEmpty) None else Some(records.head)
  }

  /**
   * @param activity
   * @return
   */
  def insert(activity: Activity): Future[Int] = {
    database.run(activities += activity)
  }

  /**
   * @param id
   * @param activity
   */
  def update(id: Long, activity: Activity): Future[Int] = {
    database.run(filterQuery(id).update(activity))
  }

  /**
   *
   */
  def delete(activity: Activity) = {
    database.run(filterQuery(activity.id.get).delete)
  }
}
