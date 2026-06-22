package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.ui.subreddit.MultiView
import com.sofamaniac.crabir.ui.subreddit.SubredditInfoView
import com.sofamaniac.crabir.ui.subreddit.SubredditViewer
import kotlin.reflect.typeOf

fun NavGraphBuilder.subredditGraph(navController: NavController) {
    composable<SubredditRoute>
    { navBackStackEntry ->
        val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
        SubredditViewer(
            subreddit,
            animatedVisibilityScope = this@composable
        )
    }
    composable(
        route = "/r/{subreddit}",
        deepLinks = stringLink("r/{subreddit}"),
        arguments = listOf(
            navArgument("subreddit") {
                type = NavType.StringType
            }
        )
    ) { navBackStackEntry ->
        val params = navBackStackEntry.arguments
        val subreddit = params!!.getString("subreddit")!!
        SubredditViewer(subreddit = subreddit, animatedVisibilityScope = this@composable)
    }
    composable<MultiRoute>(
        typeMap = mapOf(typeOf<Fullname>() to NullableFullnameType)
    ) { navBackStackEntry ->
        val multi = navBackStackEntry.toRoute<MultiRoute>()
        MultiView(
            multi.name,
            animatedVisibilityScope = this@composable
        )
    }
    composable<SubredditInfoRoute>(
        typeMap = mapOf(typeOf<Fullname>() to NullableFullnameType)
    ) { navBackStackEntry ->
        val subreddit = navBackStackEntry.toRoute<SubredditInfoRoute>()
        SubredditInfoView(subreddit.subreddit)
    }
}
