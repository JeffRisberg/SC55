package com.incra.services

import java.sql.{Statement, Types}

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.infrastructure.DBOperation
import com.incra.model._

import com.incra.infrastructure.BindingIds._

import slick.driver.MySQLDriver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * This is a non-Slick-based service (except for the table initialization)
 *
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class LeaderboardService(implicit val bindingModule: BindingModule) extends Injectable {
  private def database = inject[Database]

  private def dbOperation = bindingModule.inject[DBOperation](Some(MySQL))

  println("InitLeaderboardService")
  val leaderboards = TableQuery[LeaderboardTable]

  val tables = Await.result(database.run(MTable.getTables), Duration.Inf).toList

  val thisTableExists = tables.exists { table => table.name.name == "LEADERBOARD"}

  if (thisTableExists == false) {
    val setupAction: DBIO[Unit] = DBIO.seq(
      leaderboards.schema.create
    )
    val setupFuture: Future[Unit] = database.run(setupAction)

    val populateAction: DBIO[Option[Int]] = leaderboards ++= Seq(
      Leaderboard(None, "Team Leaders", Direction.Descending),
      Leaderboard(Some(101), "All-Time Winners", Direction.Ascending),
      Leaderboard(None, "Recent Gainers", Direction.Descending))

    val populateFuture: Future[Option[Int]] = database.run(populateAction)

    Await.result(setupFuture flatMap { _ => populateFuture }, Duration.Inf)
  }
  println("EndInitLeaderboardService")

  private val leaderboardFields = Seq(
    "id", "name", "direction")

  /**
   *
   */
  def getEntityList(): List[Leaderboard] = {
    val result = dbOperation.executeQueryWithStatement("select * from leaderboard")

    result.map(LeaderboardMarshaller.unmarshall)
  }

  /**
   *
   */
  def findById(id: Long): Option[Leaderboard] = {
    val result = dbOperation.executeQueryWithStatement("select * from leaderboard where id = " + id)

    if (result.size > 0)
      Some(LeaderboardMarshaller.unmarshall(result(0)))
    else
      None
  }

  /**
   * @param leaderboard
   * @return
   */
  def insert(leaderboard: Leaderboard): Long = {
    val fieldStr = leaderboardFields.mkString(",")
    val paramStr = leaderboardFields.map(f => "?").mkString(",")

    val sql = s"insert into leaderboard ($fieldStr) values ($paramStr)"

    dbOperation.executeInsert(sql, ps => {
      val doc = LeaderboardMarshaller.marshall(leaderboard)
      leaderboardFields.zipWithIndex.foreach {
        case (key, i) =>
          doc(key) match {
            case None => ps.setNull(i + 1, Types.OTHER)
            case value: Object => ps.setObject(i + 1, value)
          }
      }
    }, Some(Statement.RETURN_GENERATED_KEYS))
  }

  /**
   * @param id
   * @param leaderboard
   */
  def update(id: Long, leaderboard: Leaderboard): Long = {
    val paramStr = leaderboardFields.map(f => s"$f = ?").mkString(",")
    val sql = s"update leaderboard set $paramStr where id = ?"
    dbOperation.executeUpdate(sql, ps => {
      val doc = LeaderboardMarshaller.marshall(leaderboard)
      leaderboardFields.zipWithIndex.foreach {
        case (key, i) => ps.setObject(i + 1, doc(key))
      }
      ps.setObject(leaderboardFields.size+1, id)
    })
    1L
  }

  /**
   *
   */
  def delete(leaderboard: Leaderboard) = {
    val sql = "delete from leaderboard where id = ?"
    dbOperation.executeUpdate(sql, ps => {
      ps.setLong(1, leaderboard.id.get)
    })
  }
}
