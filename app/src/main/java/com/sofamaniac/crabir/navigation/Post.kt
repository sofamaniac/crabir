package com.sofamaniac.crabir.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.post.FullscreenGallery
import com.sofamaniac.crabir.ui.post.FullscreenImageView
import com.sofamaniac.crabir.ui.post.FullscreenVideo
import com.sofamaniac.crabir.ui.postEditor.CrosspostCreator
import com.sofamaniac.crabir.ui.postEditor.PostCreator
import com.sofamaniac.crabir.ui.thread.ThreadView
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

val FullnameType = object : NavType<Fullname>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Fullname? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Fullname::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Fullname {
        return Fullname(value)
    }

    override fun put(bundle: Bundle, key: String, value: Fullname) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Fullname): String {
        return value.name
    }
}

@Serializable
class PostRoute(val postPermalink: String, val comment: String?) : Route

@Serializable
class PostCreatorRoute(val kind: Kind, val communityId: String?) : Route

@Serializable
class CrosspostCreatorRoute(val post: Fullname) : Route

@Serializable
class FullscreenImageRoute(val post: Fullname) : Route

@Serializable
class FullscreenVideoRoute(val post: Fullname) : Route

@Serializable
class FullscreenGalleryRoute(val post: Fullname) : Route

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
        ThreadView(permalink = permalink, dismiss = { navController.popBackStack() })
    }
    composable<PostRoute> {
        val route = it.toRoute<PostRoute>()
        ThreadView(permalink = route.postPermalink, dismiss = { navController.popBackStack() })
    }

    composable<PostCreatorRoute> {
        val route = it.toRoute<PostCreatorRoute>()
        PostCreator(kind = route.kind, communityId = route.communityId, onDismissRequest = {
            navController.popBackStack()
        })
    }

    composable<FullscreenImageRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<FullscreenImageRoute>()
        FullscreenImageView(route.post, dismiss = { navController.popBackStack() })
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
    composable<CrosspostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<CrosspostCreatorRoute>()
        CrosspostCreator(route.post)
    }

}