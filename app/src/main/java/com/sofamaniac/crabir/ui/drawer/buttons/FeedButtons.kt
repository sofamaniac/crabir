package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.navigation.HistoryRoute
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.SavedRoute
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuItems
import com.sofamaniac.crabir.ui.components.MultiIcon
import com.sofamaniac.crabir.ui.components.SubredditIcon

enum class FeedButtons(val icon: ImageVector, val route: Route) {
    Home(Icons.Default.Home, HomeRoute),
    Default(Icons.Default.RssFeed, HomeRoute),
    Popular(Icons.Default.Moving, SubredditRoute("r/popular")),
    All(Icons.Default.BarChart, SubredditRoute("r/all")),
    Saved(Icons.Default.BookmarkBorder, SavedRoute),
    History(Icons.Default.History, HistoryRoute),
    Search(Icons.Default.Search, SearchRoute()),
}

@Composable
internal fun MultiTile(multi: Thing.Multi, showIcon: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(multi.data.displayName) },
        selected = false,
        icon = {
            if (showIcon) {
                MultiIcon(multi.data)
            } else {
                Spacer(modifier = Modifier.size(32.dp))
            }
        },
        onClick = onClick
    )
}

@Composable
internal fun SubredditTile(
    subreddit: Thing.Subreddit,
    showIcon: Boolean,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    NavigationDrawerItem(
        label = { Text(subreddit.data.displayName) },
        selected = false,
        icon = {
            if (showIcon) {
                SubredditIcon(
                    subreddit.data.displayName,
                    subreddit.data.icon,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            } else {
                Spacer(modifier = Modifier.size(32.dp))
            }
        },
        badge = if (!subreddit.data.userHasFavorited) null else {
            {
                Icon(Icons.Filled.Star, tint = theme.saved, contentDescription = null)
            }
        },
        onClick = onClick
    )
}

internal fun LazyListScope.feeds(settings: LateralMenuItems, onClick: (Route) -> Unit) {

    if (settings.defaultFeed) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.Home)) },
                selected = false,
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                onClick = {}
            )
        }
    }

    if (settings.homeFeed) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.home_feed)) },
                selected = false,
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                onClick = {
                    onClick(HomeRoute)
                }
            )
        }
    }

    if (settings.popular) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.popular_feed)) },
                selected = false,
                icon = { Icon(Icons.Default.Moving, contentDescription = null) },
                onClick = {
                    onClick(SubredditRoute("r/popular"))
                }
            )
        }
    }

    if (settings.all) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.all_feed)) },
                selected = false,
                icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                onClick = {
                    onClick(SubredditRoute("r/all"))
                }
            )
        }
    }

    if (settings.saved) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.saved)) },
                selected = false,
                icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null) },
                onClick = {
                    onClick(SavedRoute)
                }
            )
        }
    }
    if (settings.history) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.history)) },
                selected = false,
                icon = { Icon(Icons.Default.History, contentDescription = null) },
                onClick = {
                    onClick(HistoryRoute)
                }
            )
        }
    }

    if (settings.search) {
        item {
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.search)) },
                selected = false,
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                onClick = {
                    onClick(SearchRoute())
                }
            )
        }
    }

}
