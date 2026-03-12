/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

enum class RouteType {
    Home,
    Search,
    Subscriptions,
    Inbox,
    Profile,
    Subreddit,
    Post,
    Saved,
    Multi,
}

@Serializable
object HomeRoute : Route {
    override val route: String = RouteType.Home.name
    override val title: String = "Home"
}

@Serializable
class ProfileRoute(val author: String) : Route {
    override val route: String = RouteType.Profile.name
    override val title: String = "Profile"
}

@Serializable
object SavedRoute : Route {
    override val route: String = RouteType.Saved.name
    override val title: String = "Saved"
}

@Serializable
object SubscriptionsRoute : Route {
    override val route: String = RouteType.Subscriptions.name
    override val title: String = "Subscriptions"
}

@Serializable
class SearchRoute(val subreddit: String = "", val flair: String = "") : Route {
    override val route: String = RouteType.Search.name
    override val title: String = "Search"
}

@Serializable
object InboxRoute : Route {
    override val route: String = RouteType.Inbox.name
    override val title: String = "Inbox"
}

@Serializable
object HistoryRoute : Route {
    override val route: String = "History"
    override val title: String = "History"
}

@Serializable
class SubredditRoute(val subreddit: String) : Route {
    override val route: String = RouteType.Subreddit.name
    override val title: String = "Subreddit"
}

@Serializable
class MultiRoute(val displayName: String, val permalink: String) : Route {
    override val route: String = RouteType.Multi.name
    override val title: String = "Multi"
}

@Serializable
class PostRoute(val postPermalink: String) : Route {
    //override val route: String = RouteType.Post.name
    override val title: String = "Post"
    override val route = ROUTE

    companion object {
        const val ROUTE = "r/{subreddit}/comments/{id}/{title}"
    }
}

@Serializable
object LicensesRoute : Route {
    override val route: String = "Licenses"
    override val title: String = "Licenses"
}

@Serializable
object SettingsRoute : Route {
    override val route: String = "Settings"
    override val title: String = "Settings"
}

@Serializable
object ThemeRoute : Route {
    override val route: String = "Theme"
    override val title: String = "Theme"
}

@Serializable
object ThemeEditorRoute : Route {
    override val route: String = "ThemeEditor"
    override val title: String = "ThemeEditor"
}

@Serializable
object ViewsSettingRoute : Route {
    override val route: String = "Views"
    override val title: String = "Views"
}

interface Route {

    val route: String
    val title: String
}

// TODO move closer in the navgraph. Maybe one per tab ? Or move its initalisation to the tabs ?
val LocalNavController = compositionLocalOf<NavController?> { null }
