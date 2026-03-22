package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sofamaniac.crabir.FullscreenHandler
import com.sofamaniac.crabir.ui.thread.ThreadView
import kotlinx.serialization.Serializable

@Serializable
class PostRoute(val postPermalink: String) : Route {
    companion object {
    }
}

private const val ROUTE = "r/{subreddit}/comments/{id}/{title}"

fun NavGraphBuilder.postGraph(navController: NavController) {
    composable(
        route = ROUTE,
        deepLinks = stringLink(url = ROUTE),
        arguments = listOf(
            navArgument("subreddit") { type = NavType.StringType },
            navArgument("id") { type = NavType.StringType },
            navArgument("title") { type = NavType.StringType },
        )
    )
    {
        val subreddit = it.arguments?.getString("subreddit")
        val id = it.arguments?.getString("id")
        val title = it.arguments?.getString("title")
        val permalink = if (subreddit != null && id != null && title != null) {
            "/r/$subreddit/comments/$id/$title"
        } else {
            null
        }
        FullscreenHandler {
            ThreadView(permalink = permalink, dismiss = { navController.popBackStack() })
        }
    }
}