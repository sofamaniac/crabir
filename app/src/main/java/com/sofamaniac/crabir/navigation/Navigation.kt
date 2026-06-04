package com.sofamaniac.crabir.navigation

import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : Route

@Serializable
object SubscriptionsRoute : Route

@Serializable
class SearchRoute(val subreddit: String = "", val flair: String = "") : Route

@Serializable
object InboxRoute : Route

@Serializable
object HistoryRoute : Route

@Serializable
class SubredditRoute(val subreddit: String) : Route


@Serializable
class SubredditInfoRoute(val subreddit: Fullname) : Route

@Serializable
class MultiRoute(val name: Fullname) : Route

@Serializable
object LicensesRoute : Route

@Serializable
object SettingsRoute : Route

@Serializable
object ThemeRoute : Route

@Serializable
object ThemeEditorRoute : Route

@Serializable
object ViewsSettingRoute : Route

interface Route

// TODO move closer in the navgraph. Maybe one per tab ? Or move its initalisation to the tabs ?
val LocalNavController = compositionLocalOf<NavController?> { null }
val BASE_URL = listOf(
    "reddit.com",
    "www.reddit.com",
    "old.reddit.com",
    "new.reddit.com",
)


inline fun <reified T : Any> makeDeepLinks(url: String): List<NavDeepLink> {
    val links = BASE_URL.map {
        navDeepLink<T>(basePath = "$it/$url")
    }
    val linksTrailing = BASE_URL.map {
        navDeepLink<T>(basePath = "$it/$url/")
    }
    Log.d("makeDeepLinks", "Generating links for $url")
    for (link in links) {
        Log.d("makeDeepLinks", link.uriPattern.toString())
    }
    return links + linksTrailing
}

fun stringLink(url: String): List<NavDeepLink> {
    require(!url.startsWith("/"))
    require(!url.endsWith("/"))
    val links = BASE_URL.map {
        navDeepLink { uriPattern = "$it/$url" }
    }
    val linksTrailing = BASE_URL.map { navDeepLink { uriPattern = "$it/$url/" } }
    Log.d("makeDeepLinks", "Generating links for $url")
    for (link in links) {
        Log.d("makeDeepLinks", link.uriPattern.toString())
    }
    return links + linksTrailing
}