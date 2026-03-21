package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sofamaniac.crabir.makeDeepLinks
import com.sofamaniac.crabir.ui.subreddit.MultiView
import com.sofamaniac.crabir.ui.subreddit.SubredditViewer

fun NavGraphBuilder.subredditGraph(navController: NavController) {
    composable<SubredditRoute>
    { navBackStackEntry ->
        val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
        SubredditViewer(
            subreddit,
        )
    }
    composable(
        route = "r/{subreddit}",
        deepLinks = makeDeepLinks<SubredditRoute>("r/{subreddit}")
    ) {
        SubredditViewer(subreddit = it.arguments?.getString("subreddit")!!)
    }
    composable<MultiRoute> { navBackStackEntry ->
        val multi = navBackStackEntry.toRoute<MultiRoute>()
        MultiView(
            multi.displayName,
            multi.permalink
        )
    }
}