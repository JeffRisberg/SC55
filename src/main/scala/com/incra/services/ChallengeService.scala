package com.incra.services

import java.sql.Date

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model._
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import slick.driver.MySQLDriver.api._

/**
 * This is a Slick-based service
 *
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class ChallengeService(implicit val bindingModule: BindingModule) extends Injectable {
  private def database = inject[Database]

  println("InitChallengeService")
  val challenges = TableQuery[ChallengeTable]

  val tables = Await.result(database.run(MTable.getTables), Duration.Inf).toList

  val thisTableExists = tables.exists { table => table.name.name == "CHALLENGE"}

  if (thisTableExists == false) {
    val setupAction: DBIO[Unit] = DBIO.seq(
      challenges.schema.create
    )
    val setupFuture: Future[Unit] = database.run(setupAction)

    val populateAction: DBIO[Option[Int]] = challenges ++= Seq(
      Challenge(None, "Fall Hiking", TeamworkType.Team, new Date(113, 8, 1), new Date(113, 10, 20), false),
      Challenge(Some(101), "Walk to the Moon", TeamworkType.Individual, new Date(113, 5, 6), new Date(113, 6, 8), true),
      Challenge(None, "Holiday Ship-Shape", TeamworkType.Team, new Date(113, 10, 1), new Date(114, 1, 3), false)
    )

    val populateFuture: Future[Option[Int]] = database.run(populateAction)
  }
  println("EndInitChallengeService")

  def filterQuery(id: Long): Query[ChallengeTable, Challenge, Seq] =
    challenges.filter(_.id === id)

  /**
   *
   */
  def getEntityList(): Seq[Challenge] = {
    val activities = TableQuery[ChallengeTable]

    Await.result(database.run(challenges.result), Duration.Inf)
  }

  /**
   * @param id
   */
  def findById(id: Long): Option[Challenge] = {
    Some(Await.result(database.run(filterQuery(id).result.head), Duration.Inf))
  }
}
