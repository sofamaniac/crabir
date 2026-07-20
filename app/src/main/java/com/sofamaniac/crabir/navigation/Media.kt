package com.sofamaniac.crabir.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.ui.media.SimpleFullscreenImage
import com.sofamaniac.crabir.ui.media.videoPlayer.FullscreenVideo
import com.sofamaniac.crabir.ui.post.FullscreenGallery
import com.sofamaniac.crabir.ui.post.FullscreenImageView
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

val URLS = listOf(
    "preview.reddit.com",
    "preview.redd.it",
    "i.redd.it"
)

@Serializable
data class SimpleImageRoute(val url: String) : Route

@Serializable
class FullscreenImageRoute(val post: Fullname) : Route

@Serializable
class FullscreenVideoRoute(val post: Fullname) : Route

@Serializable
class FullscreenGalleryRoute(val post: Fullname) : Route
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
    composable<FullscreenImageRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenImageRoute>()
        FullscreenImageView(
            route.post,
            dismiss = { navController.popBackStack() }
        )
    }
    composable<FullscreenVideoRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenVideoRoute>()
        FullscreenVideo(route.post, dismiss = { navController.popBackStack() })
    }
    composable<FullscreenGalleryRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenGalleryRoute>()
        FullscreenGallery(route.post, dismiss = { navController.popBackStack() })
    }
}
