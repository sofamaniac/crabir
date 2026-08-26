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
class FullscreenGalleryRoute(val post: Fullname, val page: Int = 0) : Route

fun NavGraphBuilder.imagesGraph(navController: NavController) {
    for (base in URLS) {
        Log.d("NavGraph", "registering $base/{url}")
        composable(
            route = "$base/{url}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "$base/{url}" }
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
                SimpleFullscreenImage("https://i.redd.it/$url")
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
    composable(
        route = "i.imgur.com/{filename}", deepLinks = listOf(
            navDeepLink { uriPattern = "i.imgur.com/{filename}" }
        )) {
        val filename = it.arguments?.getString("filename")
        if (filename != null) {
            SimpleFullscreenImage("https://i.imgur.com/$filename")
        }
    }
    composable<FullscreenVideoRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenVideoRoute>()
        FullscreenVideo(route.post, dismiss = { navController.popBackStack() })
    }
    composable(
        route = "v.redd.it/{url}",
        deepLinks = listOf(
            navDeepLink { uriPattern = "v.redd.it/{url}" }
        ),
    ) {
        val url = it.arguments?.getString("url")
        Log.d("NavGraph", "Opening video: ${url}")
        if (url != null) {
            FullscreenVideo("https://v.redd.it/${url}") { navController.popBackStack() }
        }
    }
    composable(
        route = "v.redd.it/link/{parent}/asset/{id}/HLSPlaylist.m3u8",
        deepLinks = listOf(
            navDeepLink { uriPattern = "v.redd.it/link/{parent}/asset/{id}/HLSPlaylist.m3u8" }
        ),
    ) {
        val id = it.arguments?.getString("id")
        val parent = it.arguments?.getString("parent")
        Log.d("NavGraph", "Opening video: ${id}")
        if (id != null) {
            FullscreenVideo("https://v.redd.it/link/${parent}/asset/${id}/HLSPlaylist.m3u8") { navController.popBackStack() }
        }
    }
    composable<FullscreenGalleryRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenGalleryRoute>()
        FullscreenGallery(
            route.post,
            initialPage = route.page,
            dismiss = { navController.popBackStack() })
    }
}
