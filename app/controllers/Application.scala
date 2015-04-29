package controllers

import play.api.mvc.{Action, _}
import play.api.libs.json._
import selects.Test

object Application extends Controller {

   private val test = new Test()

  def index () = Action {

    val registerUsersByDate = test.getRegisterUsersByDate

    val json = Json.obj("labels" -> JsArray(registerUsersByDate.map(value => JsString(value._2))),
      "datacount" -> JsArray(registerUsersByDate.map(value => JsNumber(value._1)))).toString()
    println("json = " + json)

    Ok(views.html.index("Welcome to stackoverflow analitics", json))
  }


}
