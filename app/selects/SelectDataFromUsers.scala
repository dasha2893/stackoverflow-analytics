package selects

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._

/**
 * Created by user on 07.05.2015.
 */
object SelectDataFromUsers {

  def getCountUsers = {

    var countUsers = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Id.Users-UserId].[All Users-UserIds]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[count_users]})} ON ROWS\n" +
      "FROM [data_users]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countUsers = cell.getDoubleValue.toString

      }
    }
    countUsers
  }


  def getRegisterUsersByDate = {

   val registerUsersByDate = collection.mutable.ArrayBuffer[(String, String)]()

   val olapStatement = new ConnectToMondrian().getStatement()
   val mdx: String = "SELECT\nNON EMPTY {Hierarchize({[Id.Users-UserId].[All Users-UserIds]})} ON COLUMNS,\n" +
                     "NON EMPTY Hierarchize(Union(CrossJoin({[Measures].[count_users]}, " +
                     "[Data.Date].[Year].Members), CrossJoin({[Measures].[count_users]}, [Data.Date].[Month].Members))) ON ROWS\n" +
                     "FROM [data_users]"

   val cellSet = olapStatement.executeOlapQuery(mdx)


   for (row <- cellSet.getAxes.get(1)) {
     for (column <- cellSet.getAxes.get(0)) {

       val dateNotParse = row.getMembers.get(1).toString
       val regex = """\[(.*?)\].\[(\d{4})\].\[(\d{1,2})\]""".r

         dateNotParse match {
         case regex(date, year, month) => {
           val dateParse = convert(month.toInt).toString.+(" " + year).toString

           val cell = cellSet.getCell(column, row)
           val countUsers = cell.getDoubleValue.toString
           registerUsersByDate += ((countUsers, dateParse))
         }
         case _ =>
       }

     }
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

  def getCountUsersByAge = {

    val countUsersByAge = collection.mutable.ArrayBuffer[(String, String)]()
    var ageParse = ""

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\nNON EMPTY {Hierarchize({[Id.Users-UserId].[All Users-UserIds]})} ON COLUMNS,\n" +
                      "NON EMPTY CrossJoin({[Measures].[count_users]}, [Age.User-age].[Age].Members) ON ROWS\n" +
                      "FROM [data_users]"

    val cellSet = olapStatement.executeOlapQuery(mdx)


    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val ageNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        ageNotParse match {
          case regex(data, age) => {
            ageParse = age
            if (age.equals("#null")) {ageParse = "Age not specified"}
            val cell = cellSet.getCell(column, row)
            val countUsers = cell.getDoubleValue.toString

            countUsersByAge += ((countUsers, ageParse))
          }
          case _ =>
        }

      }
    }
    countUsersByAge

  }

  def getUserNamesWithMaxCountViews = {

    val userNamesWithMaxCountViews = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[max_views]})} ON COLUMNS,\n" +
                      "NON EMPTY Order(TopCount({Hierarchize({[Name.Users-Name].[Name].Members})}, 10, [Measures].[max_views]), [Measures].[max_views], BDESC) ON ROWS\n" +
                      "FROM [data_users]"

    val cellSet = olapStatement.executeOlapQuery(mdx)


    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val nameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        nameNotParse match {
          case regex(data, userName) => {
            val cell = cellSet.getCell(column, row)
            val countViews = cell.getDoubleValue.toString
            userNamesWithMaxCountViews += ((countViews, userName))
          }
          case _ =>
        }

      }
    }
    userNamesWithMaxCountViews
  }

  def getUserNamesWithMaxReputation = {

    var allReputation = 0.0

    val userNamesWithMaxReputation = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[max_reputation]})} ON COLUMNS,\n" +
                      "NON EMPTY Order(TopCount({Hierarchize({[Name.Users-Name].[Name].Members})}, 10, [Measures].[max_reputation]), [Measures].[max_reputation], BDESC) ON ROWS\n" +
                      "FROM [data_users]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val nameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        nameNotParse match {
          case regex(data, userName) => {
            val cell = cellSet.getCell(column, row)
            val reputation = cell.getDoubleValue.toString

            userNamesWithMaxReputation += ((reputation, userName))
          }
          case _ =>
        }

      }
    }
    userNamesWithMaxReputation

  }

  def getUserNamesWithMaxPositiveVotes = {

    val userNamesWithMaxPositiveVotes = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[max_positive_votes]})} ON COLUMNS,\n" +
                      "NON EMPTY Order(TopCount({Hierarchize({[Name.Users-Name].[Name].Members})}, 5, [Measures].[max_positive_votes]), [Measures].[max_positive_votes], BDESC) ON ROWS\n" +
                      "FROM [data_users]"



    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val nameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        nameNotParse match {
          case regex(data, userName) => {
            val cell = cellSet.getCell(column, row)
            val positiveVotes = cell.getDoubleValue.toString

            userNamesWithMaxPositiveVotes += ((positiveVotes, userName))
          }
          case _ =>
        }

      }
    }
    userNamesWithMaxPositiveVotes

  }

  def getUserNamesWithMaxNegativeVotes = {

   val userNamesWithMaxNegativeVotes = collection.mutable.ArrayBuffer[(String, String)]()
   val olapStatement = new ConnectToMondrian().getStatement()

   val mdx2: String = "SELECT\n" +
     "NON EMPTY {Hierarchize({[Measures].[max_negative_votes]})} ON COLUMNS,\n" +
     "NON EMPTY TopCount({Hierarchize({[Name.Users-Name].[Name].Members})}, 5, [Measures].[max_negative_votes]) ON ROWS\n" +
     "FROM [data_users]"

   val cellSet = olapStatement.executeOlapQuery(mdx2)

   for (row <- cellSet.getAxes.get(1)) {
     for (column <- cellSet.getAxes.get(0)) {

       val nameNotParse = row.getMembers.get(1).toString
       val regex = """\[(.*?)\].\[(.*?)\]""".r

       nameNotParse match {
         case regex(data, userName) => {
           val cell = cellSet.getCell(column, row)
           val negativeVotes = cell.getDoubleValue.toString

           userNamesWithMaxNegativeVotes += ((negativeVotes, userName))
         }
         case _ =>
       }

     }
   }
   userNamesWithMaxNegativeVotes
 }
}
