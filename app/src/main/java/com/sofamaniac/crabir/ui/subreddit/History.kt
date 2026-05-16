package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
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
    val topBar = @Composable { TopBar("Home", viewModel, scrollBehavior) }
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
    visitedCommunityDao: VisitedCommunityDao,
) : PostFeedViewModel(id = "_HISTORY", repository, visitedPostsDao, visitedCommunityDao)

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
        Log.d("HistoryRepository", "getThings: $timestamp")
        val entities =
            visitedPostsDao.getHistory(before = timestamp)
        cache.putAll(entities.map {
            json.decodeFromString<PostData>(it.post)
        }.associateBy { it.name })
        return PagedResponse(
            data = entities.map { it.id },
            after = Fullname(entities.lastOrNull()?.visitedAt.toString()),
            total = entities.size
        )
    }
}