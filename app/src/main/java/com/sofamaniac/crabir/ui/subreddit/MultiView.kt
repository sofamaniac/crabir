package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.crabir.ui.TabBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiView(
    name: Fullname,
    modifier: Modifier = Modifier,
    viewModel: MultiViewModel = hiltViewModel<MultiViewModel, MultiViewModel.Factory> { factory ->
        factory.create(name.name)
    },
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val info by viewModel.info.collectAsState()
    if (info == null) return
    val topBar = @Composable {
        TopBar(
            info!!.displayName,
            params,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior
        )
    }
    val bottomBar = @Composable {
        TabBar(
            2,
            onTabReselect = {
                scope.launch {
                    viewModel.listState.animateScrollToItem(0)
                }
            }
        )
    }
    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@HiltViewModel(assistedFactory = MultiViewModel.Factory::class)
class MultiViewModel @AssistedInject constructor(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: MultiDao,
    viewDao: CommunityViewDao,
    @Assisted("name") name: String,
) : PostFeedViewModel<MultiData>(
    id = Fullname(name),
    repository,
    visitedPostsDao,
    communityDao,
    viewDao
) {


    private val _info = MutableStateFlow<MultiData?>(null)
    val info = _info.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _info.value = communityDao.getByName(Fullname(name)) ?: return@launch
            repository.updateMulti(_info.value!!.permalink)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("name") name: String
        ): MultiViewModel
    }

}


