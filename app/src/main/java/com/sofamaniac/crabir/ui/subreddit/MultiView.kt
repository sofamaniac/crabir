package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.crabir.ui.TabBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiView(
    name: String,
    permalink: String,
    selected: State<Int>,
    modifier: Modifier = Modifier,
    viewModel: MultiViewModel = hiltViewModel<MultiViewModel, MultiViewModel.Factory> { factory ->
        factory.create(permalink)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val topBar = @Composable {
        TopBar(
            name,
            viewModel,
            scrollBehavior,
        )
    }
    val bottomBar = @Composable {
        TabBar(
            selected,
            onTabReselect = {
                scope.launch {
                    viewModel.listState.animateScrollToItem(0)
                }
            }
        )
    }
    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@HiltViewModel(assistedFactory = MultiViewModel.Factory::class)
class MultiViewModel @AssistedInject constructor(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao,
    @Assisted private val multiPath: String
) : PostFeedViewModel(id = multiPath, repository, visitedPostsDao, visitedCommunityDao) {

    init {
        repository.updateMulti(multiPath)
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): MultiViewModel
    }

}


