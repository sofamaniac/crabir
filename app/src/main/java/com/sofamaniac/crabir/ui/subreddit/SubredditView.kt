/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import com.sofamaniac.crabir.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.crabir.ui.TabBar
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
    viewModel: SubredditViewModel = hiltViewModel<SubredditViewModel, SubredditViewModel.Factory> { factory ->
        factory.create(subreddit)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topBar = @Composable {
        TopBar(
            subreddit,
            viewModel,
            scrollBehavior,
        )
    }
    val bottomBar = @Composable {
        TabBar(2)
    }
    val feedInfo by viewModel.info.collectAsState()
    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        feedInfo = {
            val info = feedInfo
            if (info != null) {
                SubredditInfo(info, viewModel)
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Loading...")
                }
            }
        }
    )
}

@Composable
fun SubredditInfo(info: SubredditData, viewModel: SubredditViewModel) {
    val theme = LocalTheme.current
    Column(
        modifier = Modifier
            .background(theme.cardBackground),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            AsyncImage(
                info.bannerImg,
                "Banner background image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            SubredditIcon(
                info.displayName,
                info.icon,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 16.dp)
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        info.displayNamePrefixed,
                        style = MaterialTheme.typography.titleMedium,
                        color = theme.highlight
                    )
                    Text(
                        "${info.subscribers} members",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.secondaryText
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                val joined = info.userIsSubscriber
                OutlinedButton(onClick = {
                    if (joined) {
                        viewModel.unsubscribe()
                    } else {
                        viewModel.subscribe()
                    }
                }) {
                    val icon = if (joined) Icons.Default.CheckCircle else null
                    val text = if (joined) "Joined" else "Subscribe"
                    if (icon != null) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                    }
                    Text(text)
                }
            }
            RedditMarkdown(info.publicDescription)
        }
    }
}

@HiltViewModel(assistedFactory = SubredditViewModel.Factory::class)
class SubredditViewModel @AssistedInject constructor(
    private val repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao,
    private val subredditCache: SubredditCache,
    /** Subreddit's display name */
    @Assisted private val subredditName: String,
) : PostFeedViewModel(id = subredditName, repository, visitedPostsDao, visitedCommunityDao) {

    private val _info = MutableStateFlow<SubredditData?>(null)
    val info = _info.asStateFlow()

    init {
        _info.value = subredditCache.get(subredditName)
        repository.updateSubreddit(subredditName)
        viewModelScope.launch {
            if (_info.value == null) {
                _info.value = repository.getInfo()
                _info.value?.let { subredditCache.save(it) }
            }
            updateData(_info.value)
        }
    }

    private suspend fun updateInfo(subscribed: Boolean) {

    }

    fun subscribe() {
        viewModelScope.launch {
            repository.subscribe()
        }
    }

    fun unsubscribe() {
        viewModelScope.launch {
            repository.unsubscribe()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): SubredditViewModel
    }

}


