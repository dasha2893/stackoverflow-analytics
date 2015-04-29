package selects

import anorm._
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json.{JsString, JsNumber}

/**
  * Created by user on 28.04.2015.
 */

class Test {
  var registerUsersByDate : List[(Int, String)] = List()

  def getRegisterUsersByDate = DB.withConnection { implicit c =>
    if(registerUsersByDate.size < 1) {
      println("Идём в базу")
      registerUsersByDate = SQL("SELECT  * FROM view_regisrer_users_by_date")()
        .map(row => (row[Int](1), convert(row[Double](2).toInt) + " " + row[Double](3).toInt)).toList
    }

    registerUsersByDate

  }
  def convert(month:Int) =  month match {
      case 1 => "January"
      case 2 => "February"
      case 3 => "March"
      case 4 => "April"
      case 5 => "May"
      case 6 => "June"
      case 7 => "July"
      case 8 => "August"
      case 9 => "September"
      case 10 => "October"
      case 11 => "November"
      case 12 => "December"
      case _ => "some other number"
    }


}
