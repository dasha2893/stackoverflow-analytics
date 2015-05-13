package selects

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._
/**
 * Created by dasha on 13.05.2015.
 */
object SelectDataFromComments {

  def getCountComments = {

    var countComments = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[IdComment.Comment-Id].[All Comment-Ids]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_comments]})} ON ROWS\n" +
                      "FROM [data_comments]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countComments = cell.getDoubleValue.toString

      }
    }
    countComments
  }

  def getAvgCountCommentsByPosts = {

    var avgCountCommentsByPosts = ""

    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[IdComment.Comment-Id].[All Comment-Ids]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[avg_number_of_comments_from_posts]})} ON ROWS\n" +
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
            avgCountCommentsByPosts = avg
          }
          case _ =>
        }

      }
    }
    avgCountCommentsByPosts
  }


}
