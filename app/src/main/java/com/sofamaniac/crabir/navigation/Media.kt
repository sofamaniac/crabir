package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.sofamaniac.crabir.ui.SimpleFullscreenImage

val URLS = listOf(
    "preview.reddit.com",
    "preview.redd.it",
    "i.redd.it"
)

fun NavGraphBuilder.imagesGraph(navController: NavController) {
    for (url in URLS) {
        composable(
            route = "$url/{url}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "$url/{url}" }
            ),
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                },
            )
        ) {
            val url = it.arguments?.getString("url")
            if (url != null) {
                SimpleFullscreenImage(url)
            }
        }
    }
}
