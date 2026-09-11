package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.ui.feedInfo.multi.MultiInfo
import com.sofamaniac.crabir.ui.feedInfo.subreddit.SubredditInfoView
import com.sofamaniac.crabir.ui.postFeed.multi.MultiView
import com.sofamaniac.crabir.ui.postFeed.subreddit.SubredditViewer
import com.sofamaniac.crabir.ui.user.ProfileTabs
import kotlin.reflect.typeOf

fun NavGraphBuilder.subredditGraph(navController: NavController) {
    composable<SubredditRoute>
    { navBackStackEntry ->
        val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
        if (subreddit.contains("u/")) {
            navController.popBackStack()
            navController.navigate(ProfileRoute(subreddit.split("/").last(), ProfileTabs.Posts))
        } else {
            SubredditViewer(
                subreddit
            )
        }
    }
    composable(
        route = "/r/{subreddit}",
        deepLinks = stringLink("r/{subreddit}") + listOf(
            navDeepLink { uriPattern = "com.sofamaniac.crabir://r/{subreddit}" }
        ),
        arguments = listOf(
            navArgument("subreddit") {
                type = NavType.StringType
            }
        )
    ) { navBackStackEntry ->
        val params = navBackStackEntry.arguments
        val subreddit = params!!.getString("subreddit")!!
        SubredditViewer(subreddit = subreddit)
    }
    composable<MultiRoute>(
        typeMap = mapOf(typeOf<Fullname>() to NullableFullnameType)
    ) { navBackStackEntry ->
        val multi = navBackStackEntry.toRoute<MultiRoute>()
        MultiView(
            multi.name
        )
    }
    composable<SubredditInfoRoute>(
        typeMap = mapOf(typeOf<Fullname>() to NullableFullnameType)
    ) { navBackStackEntry ->
        val subreddit = navBackStackEntry.toRoute<SubredditInfoRoute>()
        SubredditInfoView(subreddit.subreddit)
    }

    composable<MultiInfoRoute> {
        val multi = it.toRoute<MultiInfoRoute>()
        MultiInfo(multi.name)
    }
}
