package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.remote.reddit.HISTORY
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.CommunityViewRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.TabBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryViewer(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val title = stringResource(R.string.History)
    val entity by viewModel.entity.collectAsState(null)
    val defaultView = rememberViewSettings().defaultView
    val params by viewModel.params.collectAsState()
    val topBar = @Composable {
        TopBar(
            title,
            params,
            slug = HISTORY,
            disableInfo = true,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            view = entity?.view ?: defaultView,
            updateView = viewModel::updateView
        )
    }
    val bottomBar = @Composable {
        TabBar(0, onTabReselect = {
            scope.launch {
                viewModel.listState.animateScrollToItem(0)
            }
        })
    }
    FullFeedView(
        topBar,
        bottomBar,
        viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}


@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: HistoryRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditRepository,
    viewDao: CommunityViewRepository,
) : PostFeedViewModel<SubredditData>(
    displayName = HISTORY,
    repository,
    visitedPostsDao,
    communityDao,
    viewDao,
) {
    override suspend fun createViewEntity(name: String): CommunityViewEntity {
        return CommunityViewEntity(name = name, displayName = "History")
    }
}

@Singleton
class HistoryRepository @Inject constructor(
    private val visitedPostsDao: VisitedPostsDao,
    override val votableRepository: LinksRepository,
) : PostFeedRepository<FeedParams>() {

    val json = Json { ignoreUnknownKeys = true }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams
    ): PagedResponse<Fullname> {
        val timestamp = try {
            if (after.name.isBlank()) {
                System.currentTimeMillis()
            } else {
                after.name.toLong()
            }
        } catch (e: NumberFormatException) {
            return PagedResponse(
                data = emptyList(),
                after = null,
                total = 0
            )
        }
        val entities =
            visitedPostsDao.getHistory(before = timestamp)
        cache.putAll(entities.map {
            it.asVotableData() as PostData
        }.associateBy { it.name })
        val nextPage = entities.lastOrNull()?.let {
            visitedPostsDao.getPost(it.id)
        }
        return PagedResponse(
            data = entities.map { it.id },
            after = nextPage?.id,
            total = entities.size
        )
    }
}