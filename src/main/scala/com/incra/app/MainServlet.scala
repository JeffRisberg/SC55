package com.incra.app

import com.escalatesoft.subcut.inject.BindingModule
import com.incra.model.Direction.Ascending
import com.incra.model.{Direction, Leaderboard, Activity}
import com.incra.services.{LeaderboardService, ChallengeService, ActivityService}

/**
 * @author Jeff Risberg
 * @since late August 2014
 */
class MainServlet(implicit val bindingModule: BindingModule) extends SC55Stack {

  private def activityService = inject[ActivityService]

  private def challengeService = inject[ChallengeService]

  private def leaderboardService = inject[LeaderboardService]

  get("/") {
    contentType = "text/html"

    val data1 = List("title" -> "SC55 Example")
    val data2 = data1 ++ List("city" -> "Palo Alto", "state" -> "California", "population" -> 66363)

    ssp("/index", data2.toSeq: _*)
  }

  get("/activity") {
    contentType = "text/html"

    val activities = activityService.getEntityList()

    val data1 = List("title" -> "SC55 Activities")
    val data2 = data1 ++ List("name" -> "George Washington", "activities" -> activities)

    ssp("/activity/index", data2.toSeq: _*)
  }

  get("/activity.json") {
    contentType = formats("json")

    trapData {
      val activities = activityService.getEntityList()

      activities
    }
  }

  get("/activity/:id") {
    contentType = "text/html"

    val activityOpt = activityService.findById(params("id").toLong)

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

  get("/activity.json/:id") {
    contentType = "text/text"

    val activityOpt = activityService.findById(params("id").toLong)

    if (activityOpt.isDefined) {
      // use Json4s to convert
      val activity = activityOpt.get


      val data1 = List("title" -> "SC55 Activity")
      val data2 = data1 ++ List("activity" -> activity)

      ssp("/activity/show", data2.toSeq: _*)
    }
    else {
      "not found"
    }
  }

  get("/activity/edit/:id") {
    contentType = "text/html"

    val activityOpt = activityService.findById(params("id").toLong)

    if (activityOpt.isDefined) {
      val activity = activityOpt.get

      val data1 = List("title" -> "SC55 Activity")
      val data2 = data1 ++ List("activity" -> activity)

      ssp("/activity/edit", data2.toSeq: _*)
    }
    else {
      redirect("/activity")
    }
  }

  get("/activity/create") {
    val activity = Activity(None, "", "", "miles")

    val data1 = List("title" -> "SC55 Activity")
    val data2 = data1 ++ List("activity" -> activity)

    ssp("/activity/edit", data2.toSeq: _*)
  }

  get("/activity/save") {
    contentType = "text/html"

    if (params("id") != "") {
      val id = params("id").toLong
      val activityOpt = activityService.findById(id)

      if (activityOpt.isDefined) {
        val name = params("name")
        val description = params("description")
        val uom = params("uom")

        val activity = Activity(Some(id), name, description, uom)

        activityService.update(activity.id.get, activity)
      }
    }
    else {
      val name = params("name")
      val description = params("description")
      val uom = params("uom")

      val activity = Activity(None, name, description, uom)

      activityService.insert(activity)
    }
    redirect("/activity")
  }

  get("/activity/delete/:id") {
    contentType = "text/html"

    val activityOpt = activityService.findById(params("id").toLong)

    if (activityOpt.isDefined) {
      activityService.delete(activityOpt.get)
    }

    redirect("/activity")
  }

  get("/challenge") {
    contentType = "text/html"

    val challenges = challengeService.getEntityList()

    val data1 = List("title" -> "SC55 Challenges")
    val data2 = data1 ++ List("name" -> "Brocade-San Jose", "challenges" -> challenges)

    ssp("/challenge/index", data2.toSeq: _*)
  }

  get("/challenge.json") {
    contentType = formats("json")

    trapData {
      val challenges = challengeService.getEntityList()

      challenges
    }
  }

  get("/challenge/:id") {
    contentType = "text/html"

    val challengeOpt = challengeService.findById(params("id").toLong)
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

  get("/leaderboard") {
    contentType = "text/html"

    val leaderboards = leaderboardService.getEntityList()

    val data1 = List("title" -> "SC55 Leaderboards")
    val data2 = data1 ++ List("name" -> "Brocade-San Jose", "leaderboards" -> leaderboards)

    ssp("/leaderboard/index", data2.toSeq: _*)
  }

  get("/leaderboard.json") {
    contentType = formats("json")

    trapData {
      val leaderboards = leaderboardService.getEntityList()

      leaderboards
    }
  }

  get("/leaderboard/:id") {
    contentType = "text/html"

    val leaderboardOpt = leaderboardService.findById(params("id").toLong)
    if (leaderboardOpt.isDefined) {
      val leaderboard = leaderboardOpt.get

      val data1 = List("title" -> "SC55 Leaderboard")
      val data2 = data1 ++ List("leaderboard" -> leaderboard)

      ssp("/leaderboard/show", data2.toSeq: _*)
    }
    else {
      redirect("/leaderboard")
    }
  }

  get("/leaderboard/edit/:id") {
    contentType = "text/html"

    val leaderboardOpt = leaderboardService.findById(params("id").toLong)

    if (leaderboardOpt.isDefined) {
      val leaderboard = leaderboardOpt.get

      val data1 = List("title" -> "SC55 Leaderboard")
      val data2 = data1 ++ List("leaderboard" -> leaderboard)

      ssp("/leaderboard/edit", data2.toSeq: _*)
    }
    else {
      redirect("/leaderboard")
    }
  }

  get("/leaderboard/create") {
    val leaderboard = Leaderboard(None, "", Direction.Ascending)

    val data1 = List("title" -> "SC55 Leaderboard")
    val data2 = data1 ++ List("leaderboard" -> leaderboard)

    ssp("/leaderboard/edit", data2.toSeq: _*)
  }

  get("/leaderboard/save") {
    contentType = "text/html"

    if (params("id") != "") {
      val id = params("id").toLong
      val leaderboardOpt = leaderboardService.findById(id)

      if (leaderboardOpt.isDefined) {
        val name = params("name")
        val direction = Direction.Ascending

        val leaderboard = Leaderboard(Some(id), name, direction)

        leaderboardService.update(leaderboard.id.get, leaderboard)
      }
    }
    else {
      val name = params("name")
      val direction = Direction.Ascending

      val leaderboard = Leaderboard(None, name, direction)

      leaderboardService.insert(leaderboard)
    }
    redirect("/leaderboard")
  }

  get("/leaderboard/delete/:id") {
    contentType = "text/html"

    val leaderboardOpt = leaderboardService.findById(params("id").toLong)

    if (leaderboardOpt.isDefined) {
      leaderboardService.delete(leaderboardOpt.get)
    }

    redirect("/leaderboard")
  }


}
