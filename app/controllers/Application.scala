package controllers

import play.api.mvc.{Action, _}
import play.api.libs.json._
import selects.{SelectDataFromUsers, Test}

object Application extends Controller {

  def index () = Action {

    Ok(views.html.index("Welcome to stackoverflow analitics"))
  }

  def users () = Action {
    val registerUsersByDate = SelectDataFromUsers.getRegisterUsersByDate

   val countUsersByAge = SelectDataFromUsers.getCountUsersByAge

    val userNamesWithMaxReputation = SelectDataFromUsers.getUserNamesWithMaxReputation

    val userNamesWithMaxPositiveVotes = SelectDataFromUsers.getUserNamesWithMaxPositiveVotes

    val userNamesWithMaxNegativeVotes = SelectDataFromUsers.getUserNamesWithMaxNegativeVotes

    Ok(views.html.users("Welcome to stackoverflow analitics", registerUsersByDate, countUsersByAge,userNamesWithMaxReputation, userNamesWithMaxPositiveVotes, userNamesWithMaxNegativeVotes))
  }

  def posts () = Action {
    Ok(views.html.posts("Welcome to stackoverflow analitics"))
  }

}
