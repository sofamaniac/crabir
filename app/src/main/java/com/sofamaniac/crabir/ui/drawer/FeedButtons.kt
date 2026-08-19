package com.sofamaniac.crabir.ui.drawer

import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.RandditAPI
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.navigation.HistoryRoute
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.SavedRoute
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

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
internal fun MultiTile(multi: Thing.Multi, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(multi.data.displayName) },
        selected = false,
        icon = {
            AsyncImage(
                multi.data.iconUrl,
                "${multi.data.displayName} icon",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        onClick = onClick
    )
}

@Composable
internal fun SubredditTile(
    subreddit: Thing.Subreddit,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    NavigationDrawerItem(
        label = { Text(subreddit.data.displayName) },
        selected = false,
        icon = {
            SubredditIcon(
                subreddit.data.displayName,
                subreddit.data.icon,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        badge = if (!subreddit.data.userHasFavorited) null else {
            {
                Icon(Icons.Filled.Star, tint = theme.saved, contentDescription = null)
            }
        },
        onClick = onClick
    )
}

@Composable
internal fun RandomCommunity(label: String, includeNsfw: Boolean) {
    val navController = LocalNavController.current
    val viewModel: RandditViewModel = koinViewModel()
    NavigationDrawerItem(selected = false, label = { Text(label) }, onClick = {
        viewModel.getRandom(
            includeNsfw, {
                val subreddit = it.removePrefix("/")
                val route = SubredditRoute(subreddit)
                navController?.navigate(route)
            },
            {
                it.printStackTrace()
            }
        )
    })
}

@KoinViewModel
class RandditViewModel(private val api: RandditAPI) : ViewModel() {
    fun getRandom(
        includeNsfw: Boolean,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val response = api.getRandomCommunity(includeNsfw)
                if (response.isSuccessful) {
                    onSuccess(response.body()!!.url)
                } else {
                    onError(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}