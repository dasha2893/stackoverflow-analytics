package selects

import org.olap4j.{Cell, CellSet, OlapStatement}
import org.olap4j.OlapConnection
import org.olap4j._
import org.olap4j.metadata.Member
import org.olap4j.metadata.Cube
import org.olap4j.metadata.NamedList

import scala.collection.JavaConversions._

/**
 * Created by user on 07.05.2015.
 */
object SelectDataFromUsers {

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

       val dateNotParser = row.getMembers.get(1).toString
       println("dateNotParser = " + dateNotParser)
       val regex = """\[(.*?)\].\[(\d{4})\].\[(\d{1,2})\]""".r

         dateNotParser match {
         case regex(date, year, month) => {
           val dateParser = convert(month.toInt).toString.+(" " + year).toString
           println("DateParser = " + dateParser)

           val cell = cellSet.getCell(column, row)
           val countUsers = cell.getDoubleValue.toString
           System.out.println("countUsers = " + countUsers)
           System.out.println()
           registerUsersByDate += ((countUsers, dateParser))
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


}
