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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.MultiRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.repository.CommunityViewRepository
import com.sofamaniac.crabir.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.crabir.settings.views.rememberViewSettings
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
    name: String,
    modifier: Modifier = Modifier,
    viewModel: MultiViewModel = hiltViewModel<MultiViewModel, MultiViewModel.Factory> { factory ->
        factory.create(name)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val info by viewModel.info.collectAsState()
    val entity by viewModel.entity.collectAsState(null)
    val defaultView = rememberViewSettings().defaultView
    if (info == null) return
    val topBar = @Composable {
        TopBar(
            info!!.displayName,
            params,
            info!!.displayNamePrefixed,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            view = entity?.view ?: defaultView,
            updateView = viewModel::updateView
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
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@HiltViewModel(assistedFactory = MultiViewModel.Factory::class)
class MultiViewModel @AssistedInject constructor(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: MultiRepository,
    viewRepository: CommunityViewRepository,
    @Assisted("name") slug: String,
) : PostFeedViewModel<MultiData>(
    displayName = slug,
    repository,
    visitedPostsDao,
    communityDao,
    viewRepository,
) {


    private val _info = MutableStateFlow<MultiData?>(null)
    val info = _info.asStateFlow()

    init {
        assert(slug.startsWith("m/"))
        viewModelScope.launch(Dispatchers.IO) {
            _info.value = communityDao.getBySlug(slug) ?: return@launch
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


