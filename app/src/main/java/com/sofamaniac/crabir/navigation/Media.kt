package com.sofamaniac.crabir.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.crabir.ui.media.SimpleFullscreenImage
import kotlinx.serialization.Serializable

val URLS = listOf(
    "preview.reddit.com",
    "preview.redd.it",
    "i.redd.it"
)

@Serializable
data class SimpleImageRoute(val url: String) : Route

fun NavGraphBuilder.imagesGraph(navController: NavController) {
    for (url in URLS) {
        Log.d("NavGraph", "registering $url/{url}")
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
            Log.d("NavGraph", "Opening image: ${url}")
            if (url != null) {
                SimpleFullscreenImage(url)
            }
        }
    }
    composable<SimpleImageRoute> {
        val route = it.toRoute<SimpleImageRoute>()
        SimpleFullscreenImage(route.url)
    }
}
