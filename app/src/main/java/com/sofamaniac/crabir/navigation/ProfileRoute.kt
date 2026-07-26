package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.user.ProfileView
import kotlinx.serialization.Serializable

@Serializable
class ProfileRoute(val author: String, val tab: ProfileTabs = ProfileTabs.Overview) : Route

@Serializable
object SavedRoute : Route

val baseUrls = listOf("user/{author}", "u/{author}")

fun NavGraphBuilder.profileGraph(navController: NavController) {
    for (url in baseUrls) {
        composable(
            route = "/$url",
            deepLinks = makeDeepLinks<String>(url = url),
            arguments = listOf(
                navArgument("author") { type = NavType.StringType }
            )
        ) {
            val params = it.toRoute<ProfileRoute>()
            ProfileView(
                params.author
            )
        }
        composable(
            route = "/$url/{tab}",
            deepLinks = stringLink(url = "$url/{tab}"),
            arguments = listOf(
                navArgument("author") { type = NavType.StringType },
                navArgument("tab") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val params = navBackStackEntry.arguments
            val author = params!!.getString("author")!!
            val tab = ProfileTabs.fromString(params.getString("tab")!!)
            ProfileView(
                author,
                initialTab = tab
            )
        }
    }
    composable<SavedRoute>(
        deepLinks = listOf(
            navDeepLink { uriPattern = "com.sofamaniac.crabir://saved" }
        )) {
        val currentAccount = LocalRedditAccount.current
        if (currentAccount.isAnonymous()) {
            // TODO: ask user to log in (should be done at SavedRoute creation site)
            return@composable
        }
        ProfileView(
            currentAccount.info!!.username,
            initialTab = ProfileTabs.Saved,
        )
    }

    composable<ProfileRoute> {
        val params = it.toRoute<ProfileRoute>()
        ProfileView(
            params.author,
            initialTab = params.tab
        )
    }
}