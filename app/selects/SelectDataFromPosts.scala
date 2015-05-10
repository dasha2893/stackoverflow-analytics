package selects

/**
 * Created by user on 09.05.2015.
 */

import java.text.{DecimalFormat, NumberFormat}
import org.olap4j.{Cell, CellSet, OlapStatement}
import scala.collection.JavaConversions._
import scala.util.matching.Regex

object SelectDataFromPosts {

  def getCountPosts = {

    var countPosts = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Post-Id].[All Post-Ids]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[count_posts]})} ON ROWS\n" +
      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countPosts = cell.getDoubleValue.toString

      }
    }
    countPosts
  }

  def getCountPostsByType = {

    var countPosts = 0.0
    val countPostsByTypeResult = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Post-Id].[All Post-Ids]})} ON COLUMNS,\n" +
      "NON EMPTY CrossJoin({[Measures].[count_posts]}, [Post-type].[Post-type].Members) ON ROWS\n" +
      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for(i <- 0 until cellSet.getAxes.get(1).size ){
      countPosts += cellSet.getCell(i).getDoubleValue
    }

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val typeNameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        typeNameNotParse match {
          case regex(data, name) => {
            val cell = cellSet.getCell(column, row)

            val countPostsByTypeNotParse = cell.getDoubleValue

            val formatter = new DecimalFormat("#.###")
            var countPostsByType = formatter.format(countPostsByTypeNotParse * 100 / countPosts).replace(",", ".").toString
            countPostsByTypeResult += ((countPostsByType, name))
          }
          case _ =>
        }

      }
    }
    countPostsByTypeResult
  }

  def getAvgCountPostsByTypeFromUsers = {

    val avgCountPostsByTypeFromUsers = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[Measures].[avg_number_of_posts_from_users]})} ON COLUMNS,\n" +
      "NON EMPTY CrossJoin({[Post-Id].[All Post-Ids]}, CrossJoin({[OwnerUserId.Posts-OwnerUserId].[All Posts-OwnerUserIds]}, [Post-type].[Post-type].Members)) ON ROWS\n" +
      "FROM [virtual_cube]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val typeNameNotParse = row.getMembers.get(2).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        typeNameNotParse match {
          case regex(data, typeName) => {
            println("typeName = " + typeName)
            val cell = cellSet.getCell(column, row)
            val avgNotParse = cell.getDoubleValue
            val formatter = new DecimalFormat("#.###")
            var avg = formatter.format(avgNotParse).replace(",", ".").toString
            avgCountPostsByTypeFromUsers += ((avg, typeName))
          }
          case _ =>
        }

      }
    }
    avgCountPostsByTypeFromUsers
  }

  def getCountFavoritePosts = {

    var countFavoritePosts = ""
    val olapStatement = new ConnectToMondrian().getStatement()

    val mdx: String = "SELECT\n" +
      "NON EMPTY {Hierarchize({[FavoriteCount.Posts-FavoriteCount].[All Posts-FavoriteCounts]})} ON COLUMNS,\n" +
      "NON EMPTY {Hierarchize({[Measures].[count_favorite_posts]})} ON ROWS\n" +
      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {
        val cell = cellSet.getCell(column, row)
        countFavoritePosts = cell.getDoubleValue.toString

      }
    }
    countFavoritePosts
  }

  def getCountClosedPosts = {

    var countPosts = 0.0
    val countPostsByTypeResult = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Measures].[count_closed_posts]})} ON COLUMNS,\n" +
                      "NON EMPTY {Hierarchize({[ClosedDate.Posts-ClosedDate].[ClosedDate].Members})} ON ROWS\n" +
                      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for(i <- 0 until cellSet.getAxes.get(1).size ){
      countPosts += cellSet.getCell(i).getDoubleValue
    }

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val typeNameNotParse = row.getMembers.get(1).toString
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        typeNameNotParse match {
          case regex(data, name) => {
            val cell = cellSet.getCell(column, row)

            val countPostsByTypeNotParse = cell.getDoubleValue

            val formatter = new DecimalFormat("#.###")
            var countPostsByType = formatter.format(countPostsByTypeNotParse * 100 / countPosts).replace(",", ".").toString
            countPostsByTypeResult += ((countPostsByType, name))
          }
          case _ =>
        }

      }
    }
    countPostsByTypeResult
  }

  def getCountClosedPostsByDate = {

    var typeName = ""
    var typeNameClosed = ""
    var countDate = ""

    val countClosedPostsByDate = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
      "{Hierarchize({[Measures].[count_closed_posts]})} ON COLUMNS,\n" +
      "Order(Hierarchize(Union(CrossJoin([ClosedDate.Posts-ClosedDate].[ClosedDate].Members, [Date].[Year].Members), CrossJoin([ClosedDate.Posts-ClosedDate].[ClosedDate].Members, [Date].[Month].Members))), [ClosedDate.Posts-ClosedDate].CurrentMember.Name, BASC) ON ROWS\n" +
      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)


    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val dateNotParse = row.getMembers.get(1).toString
        val regexDate = """\[(.*?)\].\[(\d{4})\].\[(\d{1,2})\]""".r

        dateNotParse match {
          case regexDate(date, year, month) => {
            val dateParse = SelectDataFromUsers.convert(month.toInt).toString.+(" " + year).toString
            val nameNotParse = row.getMembers.get(0).toString
            val regexName = """\[(.*?)\].\[(.*?)\]""".r

            nameNotParse match {
              case regexName(data, name) => {
                typeName=name
                if(typeName.equals("closed")){
                  typeNameClosed=typeName
                  val cell = cellSet.getCell(column, row)
                  val count = cell.getFormattedValue
                  if(count.equals("")){
                    countDate = "0"
                  }
                  else {
                    countDate=cell.getDoubleValue.toString
                  }
                  countClosedPostsByDate += (( countDate, dateParse))
                }

              }
              case _ =>
            }
          }
          case _ =>
        }

      }
    }
    countClosedPostsByDate
  }

  def getCountOpenPostsByDate = {

    var typeName = ""
    var typeNameOpen = ""
    var countDate = ""
    val countOpenPostsByDate = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
      "{Hierarchize({[Measures].[count_closed_posts]})} ON COLUMNS,\n" +
      "Order(Hierarchize(Union(CrossJoin([ClosedDate.Posts-ClosedDate].[ClosedDate].Members, [Date].[Year].Members), CrossJoin([ClosedDate.Posts-ClosedDate].[ClosedDate].Members, [Date].[Month].Members))), [ClosedDate.Posts-ClosedDate].CurrentMember.Name, BASC) ON ROWS\n" +
      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)


    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val dateNotParse = row.getMembers.get(1).toString
        val regexDate = """\[(.*?)\].\[(\d{4})\].\[(\d{1,2})\]""".r

        dateNotParse match {
          case regexDate(date, year, month) => {
            val dateParse = SelectDataFromUsers.convert(month.toInt).toString.+(" " + year).toString
            val nameNotParse = row.getMembers.get(0).toString
            val regexName = """\[(.*?)\].\[(.*?)\]""".r

            nameNotParse match {
              case regexName(data, name) => {
                typeName=name
                if(typeName.equals("open")){
                  typeNameOpen=typeName
                  val cell = cellSet.getCell(column, row)
                  val count = cell.getFormattedValue
                  if(count.equals("")){
                    countDate = "0"
                  }
                  else {
                    countDate= cell.getDoubleValue.toString
                  }
                  countOpenPostsByDate += ((countDate,dateParse))
                }

              }
              case _ =>
            }
          }
          case _ =>
        }

      }
    }
    countOpenPostsByDate

  }

  def getCountPostsWikiedByType = {

    var countPosts = 0.0
    val countPostsWikiedByTypeResult = collection.mutable.ArrayBuffer[(String, String)]()

    val olapStatement = new ConnectToMondrian().getStatement()
    val mdx: String = "SELECT\n" +
                      "NON EMPTY {Hierarchize({[Posts-Wikied].[All Posts-Wikieds]})} ON COLUMNS,\n" +
                      "NON EMPTY CrossJoin({[Measures].[count_posts_wikied]}, [Post-type].[Post-type].Members) ON ROWS\n" +
                      "FROM [data_posts]"

    val cellSet = olapStatement.executeOlapQuery(mdx)

    for(i <- 0 until cellSet.getAxes.get(1).size ){
      countPosts += cellSet.getCell(i).getDoubleValue
    }

    for (row <- cellSet.getAxes.get(1)) {
      for (column <- cellSet.getAxes.get(0)) {

        val typeNameNotParse = row.getMembers.get(1).toString
        println("typeNameNotParse = " + typeNameNotParse)
        val regex = """\[(.*?)\].\[(.*?)\]""".r

        typeNameNotParse match {
          case regex(data, name) => {
            val cell = cellSet.getCell(column, row)

            val countPostsByTypeNotParse = cell.getDoubleValue

            val formatter = new DecimalFormat("#.###")
            var countPostsByType = formatter.format(countPostsByTypeNotParse * 100 / countPosts).replace(",", ".").toString
            countPostsWikiedByTypeResult += ((countPostsByType, name))
          }
          case _ =>
        }

      }
    }
    countPostsWikiedByTypeResult
  }
}
