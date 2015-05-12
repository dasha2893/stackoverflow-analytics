package controllers

import play.api.mvc.{Action, _}
import selects.{SelectDataFromUsers, SelectDataFromTags, SelectDataFromPosts, SelectDataFromBadges}

object Application extends Controller {

  def index () = Action {

    Ok(views.html.index("Stackoverflow Analytics"))
  }

  def users () = Action {
    val countUsers = SelectDataFromUsers.getCountUsers

    val registerUsersByDate = SelectDataFromUsers.getRegisterUsersByDate

    val countUsersByAge = SelectDataFromUsers.getCountUsersByAge

    val userNamesWithMaxReputation = SelectDataFromUsers.getUserNamesWithMaxReputation

    val userNamesWithMaxPositiveVotes = SelectDataFromUsers.getUserNamesWithMaxPositiveVotes

    val userNamesWithMaxNegativeVotes = SelectDataFromUsers.getUserNamesWithMaxNegativeVotes

    Ok(views.html.users("Stackoverflow Analytics", registerUsersByDate, countUsersByAge,userNamesWithMaxReputation, userNamesWithMaxPositiveVotes, userNamesWithMaxNegativeVotes, countUsers))
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

    Ok(views.html.posts("Stackoverflow Analytics", countPosts,
      countPostsByType, avgCountPostsByTypeFromUsers, countFavoritePosts,
      countClosedPosts, countClosedPostsByDate, countOpenPostsByDate, countPostsWikiedByType))
  }

  def tags () = Action {
    val countTags = SelectDataFromTags.getCountTags
    val popularTags = SelectDataFromTags.getPopularTags
    Ok(views.html.tags("Stackoverflow Analytics", countTags, popularTags))
  }


  def badges () = Action {

    val countBadges = SelectDataFromBadges.getCountBadges
    val countTypeBadges = SelectDataFromBadges.getCountTypeBadges

    Ok(views.html.badges("Stackoverflow Analytics", countBadges, countTypeBadges))
  }
}
