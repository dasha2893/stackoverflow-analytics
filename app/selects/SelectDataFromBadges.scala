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
}
