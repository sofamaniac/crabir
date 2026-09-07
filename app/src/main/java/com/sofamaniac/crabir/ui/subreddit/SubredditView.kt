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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import com.sofamaniac.crabir.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.TabBar
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
) {
    val defaultEntity = getCommunityViewEntity(subreddit, subreddit)
    val subredditName = subreddit.split("/").last()
    val viewModel: SubredditViewModel =
        koinViewModel(key = subreddit) {
            parametersOf(
                subreddit,
                defaultEntity,
            )
        }
    var entity by remember(subreddit) { mutableStateOf(defaultEntity) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val params by viewModel.params.collectAsState()
    val feedInfo by viewModel.info.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val viewDataStore = LocalContext.current.viewSettingDataStore
    LaunchedEffect(feedInfo) {
        if (feedInfo == null) return@LaunchedEffect
        scope.launch {
            viewDataStore.updateData {
                it.copy(
                    rememberedViews = it.rememberedViews + (subreddit to entity.copy(
                        displayName = feedInfo!!.displayNamePrefixed
                    ))
                )
            }
        }
    }

    val topBar = @Composable {
        TopBar(
            feedInfo?.displayName ?: subreddit,
            params,
            slug = subreddit,
            updateSort = viewModel::updateSort,
            updateView = { entity = entity.copy(view = it) },
            refresh = viewModel::refresh,
            entity = entity,
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
    val bottomBar = @Composable {
        TabBar(2)
    }
    val feedInfoView = feedInfo?.let { info ->
        @Composable {
            SubredditInfo(info, viewModel)
        }
    }

    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        filter = rememberPostsFilter(whitelistSubreddit = listOf(subredditName)),
        drawerState = drawerState,
        feedInfo = feedInfoView,
        viewEntity = entity,
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
                info.bannerImg.ifBlank { info.bannerBackgroundImage ?: "" },
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
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            }
            RedditMarkdown(info.publicDescription)
        }
    }
}

@KoinViewModel
class SubredditViewModel(
    private val repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditDao,
    private val subredditCache: SubredditCache,
    /** Subreddit's prefixed display name */
    @InjectedParam slug: String,
    @InjectedParam viewEntity: CommunityViewEntity,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
    viewEntity,
) {

    private val _info = MutableStateFlow<SubredditData?>(null)
    val info = _info.asStateFlow()

    init {
        val slug = if (slug.startsWith("r/") || slug.startsWith("/r/")) slug else "r/$slug"
        repository.updateSubreddit(slug)
        viewModelScope.launch {
            _info.value = subredditCache.getBySlug(slug)
            if (_info.value == null) {
                _info.value = repository.getInfo()
                updateData(_info.value)
            }
        }
    }

    fun subscribe() {
        viewModelScope.launch {
            repository.subscribe()
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

    fun unsubscribe() {
        viewModelScope.launch {
            repository.unsubscribe()
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

    fun favorite(favorite: Boolean) {
        viewModelScope.launch {
            repository.favorite(favorite)
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

}


