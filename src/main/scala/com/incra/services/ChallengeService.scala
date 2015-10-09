package com.incra.services

import java.sql.Date

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

    Await.result(setupFuture flatMap { _ => populateFuture }, Duration.Inf)
  }
  println("EndInitChallengeService")

  def filterQuery(id: Long): Query[ChallengeTable, Challenge, Seq] =
    challenges.filter(_.id === id)

  /**
   *
   */
  def getEntityList(): Seq[Challenge] = {
    val challenges = TableQuery[ChallengeTable]

    Await.result(database.run(challenges.result), Duration.Inf)
  }

  /**
   * @param id
   */
  def findById(id: Long): Option[Challenge] = {
    val records = Await.result(database.run(filterQuery(id).result), Duration.Inf)

    if (records.isEmpty) None else Some(records.head)
  }

  /**
   * @param challenge
   * @return
   */
  def insert(challenge: Challenge): Future[Int] = {
    database.run(challenges += challenge)
  }

  /**
   * @param id
   * @param challenge
   */
  def update(id: Long, challenge: Challenge): Future[Int] = {
    database.run(filterQuery(id).update(challenge))
  }

  /**
   *
   */
  def delete(challenge: Challenge) = {
    database.run(filterQuery(challenge.id.get).delete)
  }
}
