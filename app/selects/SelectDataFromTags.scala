package selects

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._

/**
 * Created by user on 09.05.2015.
 */
object SelectDataFromTags {

  def getCountTags = {

    var countTags = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[TagName].[All TagNames]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[count_tags]})} ON ROWS\n" +
      "FROM [data_tags]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
          val cell = cellSet.getCell(column, row)
          countTags = cell.getDoubleValue.toString

      }
    }
    countTags
  }

  def getPopularTags = {


    val popularTags = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Measures].[max_count_posts_by_tagName]})} ON COLUMNS,\n" +
      "NON EMPTY Order(TopCount({Hierarchize({[TagName].[TagName].Members})}, 50, [Measures].[max_count_posts_by_tagName]), [Measures].[max_count_posts_by_tagName], BDESC) ON ROWS\n" +
      "FROM [data_tags]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val nameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        nameNotParse match {
          case regex(data, tagName) => {
            val cell = cellSet.getCell(column, row)
            val countPosts = cell.getDoubleValue.toString
            popularTags += ((countPosts, tagName))
          }
          case _ =>
        }

      }
    }
    popularTags
  }
}
