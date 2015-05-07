package controllers

import play.api.mvc.{Action, _}
import play.api.libs.json._
import selects.{SelectDataFromUsers, Test}

object Application extends Controller {

   private val test = new Test()

  def index () = Action {

    val registerUsersByDate = SelectDataFromUsers.getRegisterUsersByDate
    println("registerUsersByDate = " + registerUsersByDate)

    Ok(views.html.index("Welcome to stackoverflow analitics", registerUsersByDate))
  }

  def users () = Action {
    Ok(views.html.users("Welcome to stackoverflow analitics"))
  }

}
