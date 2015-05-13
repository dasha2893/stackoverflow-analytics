package selects

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._
/**
 * Created by dasha on 13.05.2015.
 */
object SelectDataFromVotes {

  def getCountVotes = {

    var countVotes = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_votes]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[TypeName.Votes-TypeName].[All Votes-TypeNames]})} ON ROWS\n" +
                      "FROM [data_votes]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countVotes = cell.getDoubleValue.toString

      }
    }
    countVotes
  }

  def getCountVotesByType = {

    var countVotesByType  = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_type_votes]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[TypeName.Votes-TypeName].[All Votes-TypeNames]})} ON ROWS\n" +
                      "FROM [data_votes]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countVotesByType = cell.getDoubleValue.toString

      }
    }
    countVotesByType
  }

}
