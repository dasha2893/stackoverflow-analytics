package selects

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._
/**
 * Created by dasha on 12.05.2015.
 */
object SelectDataFromBadges {

  def getCountBadges = {

    var countBadges = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Badges-Name].[All Badges-Names]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_badges]})} ON ROWS\n" +
                      "FROM [data_badges]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countBadges = cell.getDoubleValue.toString

      }
    }
    countBadges
  }

  def getCountTypeBadges = {

    var countTypeBadges = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Badges-Name].[All Badges-Names]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_type_badges]})} ON ROWS\n" +
                      "FROM [data_badges]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countTypeBadges = cell.getDoubleValue.toString

      }
    }
    countTypeBadges
 }

  def getAvgCountBadgesFromUsers = {

    var avgCountBadgesFromUsers = ""

    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Badges-Name].[All Badges-Names]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[avg_number_of_badges_from_users]})} ON ROWS\n" +
      "FROM [virtual_cube]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val typeNameNotParse = row.getMembers.get(2).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        typeNameNotParse match {
          case regex(data, typeName) => {
            val cell = cellSet.getCell(column, row)
            val avgNotParse = cell.getDoubleValue
            val formatter = new DecimalFormat("#.###")
            var avg = formatter.format(avgNotParse).replace(",", ".").toString
            avgCountBadgesFromUsers = avg
          }
          case _ =>
        }

      }
    }
    avgCountBadgesFromUsers
  }

  def getTheMostUsedBadges = {

    val mostUsedBadges = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Measures].[count_badges]})} ON COLUMNS,\n" +
      "NON EMPTY Order(TopCount({Hierarchize({[Badges-Name].[Badges-Name].Members})}, 10, [Measures].[count_badges]), [Measures].[count_badges], BDESC) ON ROWS\n" +
      "FROM [data_badges]"

    val cellSet = olapStatement.executeOlapQuery(mdx)


    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val nameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        nameNotParse match {
          case regex(data, badgesName) => {
            val cell = cellSet.getCell(column, row)
            val count = cell.getDoubleValue.toString
            mostUsedBadges += ((count, badgesName))
          }
          case _ =>
        }

      }
    }
    mostUsedBadges
  }
}
