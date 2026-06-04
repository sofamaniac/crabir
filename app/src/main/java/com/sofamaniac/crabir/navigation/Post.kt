package com.sofamaniac.crabir.navigation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.net.toUri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
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

val NullableFullnameType = object : NavType<Fullname?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Fullname? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Fullname::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Fullname? {
        return if (value == "null") null else Fullname(value)
    }

    override fun put(bundle: Bundle, key: String, value: Fullname?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Fullname?): String {
        return value?.name ?: "null"
    }
}

@Serializable
class PostRoute(val postPermalink: String, val comment: String? = null, val context: Int? = null) :
    Route

@Serializable
class PostCreatorRoute(val kind: Kind, val communityId: Fullname?) : Route

@Serializable
class CrosspostCreatorRoute(val post: Fullname) : Route

@Serializable
class FullscreenImageRoute(val post: Fullname) : Route

@Serializable
class FullscreenVideoRoute(val post: Fullname) : Route

@Serializable
class FullscreenGalleryRoute(val post: Fullname) : Route

private const val ROUTE = "r/{subreddit}/comments/{id}/{title}"
private const val SHORT_ROUTE = "r/{subreddit}/s/{id}"
private const val LONG_ROUTE = "r/{subreddit}/comments/{id}/{title}/{commentId}"
private const val LONGER_ROUTE = "r/{subreddit}/comments/{id}/{title}/comment/{commentId}"

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
    composable(
        route = LONG_ROUTE,
        deepLinks = stringLink(url = LONG_ROUTE) + stringLink(LONGER_ROUTE),
        arguments = listOf(
            navArgument("subreddit") { type = NavType.StringType },
            navArgument("id") { type = NavType.StringType },
            navArgument("title") { type = NavType.StringType },
            navArgument("commentId") { type = NavType.StringType },
        )
    )
    {
        val subreddit = it.arguments?.getString("subreddit")
        val id = it.arguments?.getString("id")
        val title = it.arguments?.getString("title")
        val commentId = it.arguments?.getString("commentId")
        val permalink = if (subreddit != null && id != null && title != null) {
            "/r/$subreddit/comments/$id/$title"
        } else {
            null
        }
        ThreadView(
            permalink = permalink,
            dismiss = { navController.popBackStack() },
            comment = commentId
        )
    }
    composable(
        route = SHORT_ROUTE,
        deepLinks = stringLink(url = SHORT_ROUTE),
        arguments = listOf(
            navArgument("subreddit") { type = NavType.StringType },
            navArgument("id") { type = NavType.StringType },
        )
    )
    { navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getString("id")
        val subreddit = navBackStackEntry.arguments?.getString("subreddit")
        val client = remember { OkHttpClient() }

        LaunchedEffect(id) {
            val url =
                "https://www.reddit.com" + (if (!subreddit.isNullOrBlank()) "/r/$subreddit" else "") + "/s/${id}"
            Log.d("NavGraph", "Trying to resolve short link at $url")
            val request = Request.Builder().url(url).build()
            val finalUrl = withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    response.request.url.toString()
                }
            }
            navController.popBackStack()
            Log.d("NavGraph", finalUrl)
            navController.navigate(deepLink = finalUrl.toUri())
        }
    }
    composable<PostRoute> {
        val route = it.toRoute<PostRoute>()
        ThreadView(
            permalink = route.postPermalink,
            comment = route.comment,
            context = route.context,
            dismiss = { navController.popBackStack() }
        )
    }

    composable<PostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname?>() to NullableFullnameType)
    ) {
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