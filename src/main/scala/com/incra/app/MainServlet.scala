package com.incra.app

import com.incra.services.{ActivityService, ChallengeService}

/**
 * @author Jeff Risberg
 * @since late August 2014
 */
class MainServlet extends SC55Stack {

  get("/") {
    contentType = "text/html"

    val data1 = List("title" -> "SC55 Example")
    val data2 = data1 ++ List("city" -> "Palo Alto", "state" -> "California", "population" -> 66363)

    ssp("/index", data2.toSeq: _*)
  }

  get("/activity") {
    contentType = "text/html"

    val activities = ActivityService.getEntityList()

    val data1 = List("title" -> "SC55 Activities")
    val data2 = data1 ++ List("name" -> "George Washington", "activities" -> activities)

    ssp("/activity/index", data2.toSeq: _*)
  }

  get("/activity/:id") {
    contentType = "text/html"

    val activityOpt = ActivityService.findById(params("id").toInt)

    if (activityOpt.isDefined) {
      val activity = activityOpt.get

      val data1 = List("title" -> "SC55 Activity")
      val data2 = data1 ++ List("activity" -> activity)

      ssp("/activity/show", data2.toSeq: _*)
    }
    else {
      redirect("/activity")
    }
  }

  get("/challenge") {
    contentType = "text/html"

    val challenges = ChallengeService.getEntityList()

    val data1 = List("title" -> "SC55 Challenges")
    val data2 = data1 ++ List("name" -> "Brocade-San Jose", "challenges" -> challenges)

    ssp("/challenge/index", data2.toSeq: _*)
  }

  get("/challenge/:id") {
    contentType = "text/html"

    val challengeOpt = ChallengeService.findById(params("id").toInt)
    if (challengeOpt.isDefined) {
      val challenge = challengeOpt.get

      val data1 = List("title" -> "SC55 Challenge")
      val data2 = data1 ++ List("challenge" -> challenge)

      ssp("/challenge/show", data2.toSeq: _*)
    }
    else {
      redirect("/challenge")
    }
  }
}

object MainServlet {
  val url = "jdbc:mysql://localhost:3306/sc55"
  val driver = "com.mysql.jdbc.Driver"
  val user = "developer"
  val password = "123456"
}