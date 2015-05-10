package controllers

import play.api.mvc.{Action, _}
import selects.{SelectDataFromUsers, SelectDataFromTags, SelectDataFromPosts}

object Application extends Controller {

  def index () = Action {

    Ok(views.html.index("Welcome to stackoverflow analitics"))
  }

  def users () = Action {
    val countUsers = SelectDataFromUsers.getCountUsers

    val registerUsersByDate = SelectDataFromUsers.getRegisterUsersByDate

    val countUsersByAge = SelectDataFromUsers.getCountUsersByAge

    val userNamesWithMaxReputation = SelectDataFromUsers.getUserNamesWithMaxReputation

    val userNamesWithMaxPositiveVotes = SelectDataFromUsers.getUserNamesWithMaxPositiveVotes

    val userNamesWithMaxNegativeVotes = SelectDataFromUsers.getUserNamesWithMaxNegativeVotes

    Ok(views.html.users("Welcome to stackoverflow analitics", registerUsersByDate, countUsersByAge,userNamesWithMaxReputation, userNamesWithMaxPositiveVotes, userNamesWithMaxNegativeVotes, countUsers))
  }

  def posts () = Action {
    val countPosts = SelectDataFromPosts.getCountPosts

    val countPostsByType = SelectDataFromPosts.getCountPostsByType

    val avgCountPostsByTypeFromUsers = SelectDataFromPosts.getAvgCountPostsByTypeFromUsers

    val countFavoritePosts = SelectDataFromPosts.getCountFavoritePosts

    val countClosedPosts = SelectDataFromPosts.getCountClosedPosts

    val countClosedPostsByDate = SelectDataFromPosts.getCountClosedPostsByDate

    val countOpenPostsByDate = SelectDataFromPosts.getCountOpenPostsByDate

    val countPostsWikiedByType = SelectDataFromPosts.getCountPostsWikiedByType

    Ok(views.html.posts("Welcome to stackoverflow analitics", countPosts,
      countPostsByType, avgCountPostsByTypeFromUsers, countFavoritePosts,
      countClosedPosts, countClosedPostsByDate, countOpenPostsByDate, countPostsWikiedByType))
  }

  def tags () = Action {
    val countTags = SelectDataFromTags.getCountTags
    val popularTags = SelectDataFromTags.getPopularTags
    Ok(views.html.tags("Welcome to stackoverflow analitics", countTags, popularTags))
  }

}
