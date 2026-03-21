package com.sofamaniac.crabir.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
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
class MultiRoute(val displayName: String, val permalink: String) : Route

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