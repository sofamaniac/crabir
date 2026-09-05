package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.SubredditAPI
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@KoinViewModel
class SubredditInfoViewModel(
    private val subredditCache: SubredditCache,
    private val redditApi: SubredditAPI,
    /** Subreddit's prefixed name */
    @InjectedParam subredditName: String,
) : ViewModel() {

    val info: MutableStateFlow<SubredditData?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            info.value = subredditCache.get(subredditName)
        }
    }

    fun favorite(favorite: Boolean) {
        val infoLoc = info.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            redditApi.favorite(info.value!!.displayName, !infoLoc.userHasFavorited).onSuccess {
                info.value = infoLoc.copy(userHasFavorited = favorite)
                info.value?.let { subredditCache.save(it) }
            }
        }
    }

    private fun subscribeInner(action: SubscribeAction) {
        val infoLoc = info.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            redditApi.subscribe(action, info.value!!.displayName).onSuccess {
                info.value =
                    infoLoc.copy(userIsSubscriber = action == SubscribeAction.SUBSCRIBE)
                info.value?.let { subredditCache.save(it) }
            }
        }
    }


    fun subscribe() {
        subscribeInner(SubscribeAction.SUBSCRIBE)
    }

    fun unsubscribe() {
        subscribeInner(SubscribeAction.UNSUBSCRIBE)
    }

}

@Composable
fun SubscribeButton(isSubscriber: Boolean, onClick: () -> Unit = {}) {
    OutlinedButton(onClick = onClick) {
        val icon = if (isSubscriber) Icons.Default.CheckCircle else null
        val text = if (isSubscriber) "Joined" else "Subscribe"
        if (icon != null) {
            Icon(icon, contentDescription = null)
        }
        Text(text)
    }
}

@Composable
fun FavoriteButton(hasFavorited: Boolean, onClick: () -> Unit = {}) {
    val theme = LocalTheme.current
    OutlinedButton(onClick = onClick) {
        val icon = if (hasFavorited) Icons.Filled.Star else Icons.Outlined.Star
        val tint = if (hasFavorited) theme.saved else Color.Gray
        val text = if (hasFavorited) "Unfavorite" else "Favorite"
        Icon(icon, contentDescription = null, tint = tint)
        Text(text)
    }
}

@Composable
fun SubredditInfoView(
    subreddit: String,
    viewModel: SubredditInfoViewModel = koinViewModel { parametersOf(subreddit) },
) {
    val infoOpt by viewModel.info.collectAsState()

    if (infoOpt == null) return

    val info = infoOpt!!

    Scaffold(topBar = { TopBar(info.displayName) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Row {
                    SubredditIcon(info.displayName, info.icon)
                    Column {
                        Text(info.displayNamePrefixed, style = MaterialTheme.typography.titleMedium)
                        val members = LocalResources.current.getQuantityString(
                            R.plurals.members,
                            info.subscribers,
                            info.subscribers
                        )
                        Text(members, style = MaterialTheme.typography.labelSmall)
                        TextButton(onClick = {}) {
                            Text("Edit Flair")
                        }
                    }
                }
            }
            item {
                Row {
                    SubscribeButton(info.userIsSubscriber) {
                        if (info.userIsSubscriber) {
                            viewModel.unsubscribe()
                        } else {
                            viewModel.subscribe()
                        }
                    }
                    FavoriteButton(info.userHasFavorited) {
                        viewModel.favorite(!info.userHasFavorited)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
            item {
                RedditMarkdown(
                    info.description,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
internal fun TopBar(subreddit: String) {
    val navController = LocalNavController.current
    TopAppBar(
        navigationIcon = {
            BackButton {
                navController?.popBackStack()
            }
        },
        title = {},
        actions = {
            IconButton(onClick = {
                navController?.navigate(SearchRoute(subreddit))
            }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}
