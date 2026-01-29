package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.reboost.ui.TabBar
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
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    viewModel: MultiViewModel = hiltViewModel<MultiViewModel, MultiViewModel.Factory> { factory ->
        factory.create(permalink)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                name,
                viewModel,
                drawerState,
                scrollBehavior,
            )
        },
        bottomBar = {
            TabBar(
                selected,
                onTabReselect = {
                    scope.launch {
                        viewModel.listState.animateScrollToItem(0)
                    }
                }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PostFeedViewer(
            viewModel, showSubredditIcon = false, modifier = Modifier.padding(innerPadding)
        )
    }
}

@HiltViewModel(assistedFactory = MultiViewModel.Factory::class)
class MultiViewModel @AssistedInject constructor(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    @Assisted private val multiPath: String
) : PostFeedViewModel(repository, visitedPostsDao) {

    init {
        repository.updateMulti(multiPath)
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): MultiViewModel
    }

}


