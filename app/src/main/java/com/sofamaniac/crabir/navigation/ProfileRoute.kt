package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.repository.rememberCurrentAccount
import com.sofamaniac.crabir.makeDeepLinks
import com.sofamaniac.crabir.stringLink
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.user.ProfileView
import kotlinx.serialization.Serializable

@Serializable
class ProfileRoute(val author: String, val tab: ProfileTabs) : Route

@Serializable
object SavedRoute : Route

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(
        route = "user/{author}",
        deepLinks = makeDeepLinks<String>(url = "user/{author}") + makeDeepLinks<String>("u/{author}"),
        arguments = listOf(
            navArgument("author") { type = NavType.StringType }
        )
    ) {
        val params = it.toRoute<ProfileRoute>()
        ProfileView(
            params.author,
        )
    }
    composable(
        route = "user/{author}/{tab}",
        deepLinks = stringLink(url = "user/{author}/{tab}")
                + stringLink(url = "u/{author}/{tab}"),
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
    composable<SavedRoute> {
        val currentAccount = rememberCurrentAccount()
        if (currentAccount.isAnonymous()) {
            // TODO: ask user to log in
            return@composable
        }
        ProfileView(currentAccount.username, initialTab = ProfileTabs.Saved)
    }

    composable<ProfileRoute> {
        val params = it.toRoute<ProfileRoute>()
        ProfileView(
            params.author,
            initialTab = params.tab
        )
    }
}