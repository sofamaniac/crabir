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
import com.sofamaniac.crabir.HistoryRoute
import com.sofamaniac.crabir.HomeRoute
import com.sofamaniac.crabir.Route
import com.sofamaniac.crabir.SavedRoute
import com.sofamaniac.crabir.SearchRoute
import com.sofamaniac.crabir.SubredditRoute

enum class FeedButtons(val icon: ImageVector, val route: Route) {
    Home(Icons.Default.Home, HomeRoute),
    Default(Icons.Default.RssFeed, HomeRoute),
    Popular(Icons.Default.Moving, SubredditRoute("popular")),
    All(Icons.Default.BarChart, SubredditRoute("all")),
    Saved(Icons.Default.BookmarkBorder, SavedRoute),
    History(Icons.Default.History, HistoryRoute),
    Search(Icons.Default.Search, SearchRoute()),
}