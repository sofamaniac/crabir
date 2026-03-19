/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : Route

@Serializable
class ProfileRoute(val author: String, val tab: String) : Route

@Serializable
object SavedRoute : Route

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
class MultiRoute(val displayName: String, val permalink: String) : Route

@Serializable
class PostRoute(val postPermalink: String) : Route {
    companion object {
        const val ROUTE = "r/{subreddit}/comments/{id}/{title}"
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

interface Route

// TODO move closer in the navgraph. Maybe one per tab ? Or move its initalisation to the tabs ?
val LocalNavController = compositionLocalOf<NavController?> { null }