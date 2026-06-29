package com.sofamaniac.crabir.ui.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.sofamaniac.crabir.navigation.HistoryRoute
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.SavedRoute
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubredditRoute

enum class FeedButtons(val icon: ImageVector, val route: Route) {
    Home(Icons.Default.Home, HomeRoute),
    Default(Icons.Default.RssFeed, HomeRoute),
    Popular(Icons.Default.Moving, SubredditRoute("r/popular")),
    All(Icons.Default.BarChart, SubredditRoute("r/all")),
    Saved(Icons.Default.BookmarkBorder, SavedRoute),
    History(Icons.Default.History, HistoryRoute),
    Search(Icons.Default.Search, SearchRoute()),
}