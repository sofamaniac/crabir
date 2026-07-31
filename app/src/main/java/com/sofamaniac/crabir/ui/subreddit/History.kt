package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingSource
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.remote.reddit.HISTORY
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import com.sofamaniac.crabir.ui.TabBar
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Singleton
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryViewer(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.History)
    val entity =
        LocalViewSettings.current.rememberedViews[HISTORY] ?: defaultCommunityEntity(HISTORY, title)
    val viewModel: HistoryViewModel = koinViewModel() {
        parametersOf(entity)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val topBar = @Composable {
        TopBar(
            title,
            params,
            slug = HISTORY,
            openDrawer = { scope.launch { drawerState.open() } },
            disableInfo = true,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            entity = entity,
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
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        drawerState = drawerState,
        viewEntity = entity
    )
}


@KoinViewModel
class HistoryViewModel(
    repository: HistoryRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditRepository,
    @InjectedParam viewEntity: CommunityViewEntity,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
    viewEntity,
)

@Singleton
class HistoryRepository(
    private val visitedPostsDao: VisitedPostsDao,
    override val votableRepository: LinksRepository,
) : PostFeedRepository<FeedParams>() {

    val json = Json { ignoreUnknownKeys = true }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        val timestamp = try {
            if (after.name.isBlank()) {
                System.currentTimeMillis()
            } else {
                after.name.toLong()
            }
        } catch (e: NumberFormatException) {
            return PagingSource.LoadResult.Error(e)
        }
        val entities =
            visitedPostsDao.getHistory(before = timestamp)
        cache.putAll(entities.map {
            it.asVotableData() as PostData
        }.associateBy { it.name })
        val nextPage = entities.lastOrNull()?.let {
            visitedPostsDao.getPost(it.id)
        }
        return PagingSource.LoadResult.Page(
            entities.map { it.id },
            nextKey = nextPage?.id,
            prevKey = null
        )
    }
}