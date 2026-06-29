package com.sofamaniac.crabir.navigation

import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
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
/**
 * @param subreddit the prefixed display name of the subreddit
 */
class SubredditRoute(val subreddit: String) : Route {
    init {
        assert(subreddit.startsWith("r/"))
    }
}


@Serializable
class SubredditInfoRoute(val subreddit: String) : Route {

    init {
        assert(subreddit.startsWith("r/"))
    }
}

@Serializable
class MultiRoute(val name: String) : Route {
    init {
        assert(name.startsWith("m/"))
    }
}

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

@Serializable
object FiltersSettingRoute : Route

@Serializable
object DebugOptionsRoute : Route

interface Route

// TODO move closer in the navgraph. Maybe one per tab ? Or move its initalisation to the tabs ?
val LocalNavController = compositionLocalOf<NavController?> { null }
val BASE_URLS = listOf(
    "www.reddit.com",
    "old.reddit.com",
    "new.reddit.com",
    "i.reddit.com",
    "v.reddit.com",
    "preview.reddit.com",
    "reddit.com",
)

/** Convert a reddit URL to a local URL.
 * Examples:
 * - `reddit.com/r/test` -> `/r/test`
 * - `https://www.reddit.com/u/sofamaniac` -> `/u/sofamaniac`
 */
fun String.toLocalUrl(): String {
    val removeProtocol = Regex("^(https?://)?")
    val url = removeProtocol.replace(this, "")
    return BASE_URLS.fold(url) { acc, s ->
        if (acc.startsWith(s))
            acc.replaceFirst(s, "")
        else acc
    }
}


inline fun <reified T : Any> makeDeepLinks(url: String): List<NavDeepLink> {
    val links = BASE_URLS.map {
        navDeepLink<T>(basePath = "$it/$url")
    }
    val linksTrailing = BASE_URLS.map {
        navDeepLink<T>(basePath = "$it/$url/")
    }
    Log.d("makeDeepLinks", "Generating links for $url")
    for (link in links) {
        Log.d("makeDeepLinks", link.uriPattern.toString())
    }
    return links + linksTrailing
}

fun stringLink(url: String): List<NavDeepLink> {
    val url = url.removePrefix("/").removeSuffix("/")
    val links = BASE_URLS.map {
        navDeepLink { uriPattern = "$it/$url" }
    }
    val linksTrailing = BASE_URLS.map { navDeepLink { uriPattern = "$it/$url/" } }
    Log.d("makeDeepLinks", "Generating links for $url")
    for (link in links) {
        Log.d("makeDeepLinks", link.uriPattern.toString())
    }
    return links + linksTrailing
}