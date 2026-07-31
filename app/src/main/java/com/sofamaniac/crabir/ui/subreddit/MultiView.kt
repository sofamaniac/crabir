package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.data.local.dao.MultiRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.TabBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiView(
    slug: String,
    modifier: Modifier = Modifier,
) {
    val entity = LocalViewSettings.current.rememberedViews[slug] ?: defaultCommunityEntity(
        slug,
        slug
    )
    val viewModel: MultiViewModel = koinViewModel<MultiViewModel> {
        parametersOf(slug, entity)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val info by viewModel.info.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val viewDataStore = LocalContext.current.viewSettingDataStore

    LaunchedEffect(info) {
        if (info == null) return@LaunchedEffect
        scope.launch {
            viewDataStore.updateData {
                it.copy(
                    rememberedViews = it.rememberedViews + (slug to entity.copy(
                        displayName = info!!.displayNamePrefixed
                    ))
                )
            }
        }
    }


    val topBar = @Composable {
        TopBar(
            info?.displayName ?: slug,
            params,
            info?.displayNamePrefixed ?: slug,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            entity = entity,
            openDrawer = { scope.launch { drawerState.open() } }
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
        drawerState = drawerState,
        viewEntity = entity
    )
}

@KoinViewModel
class MultiViewModel(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: MultiRepository,
    @InjectedParam slug: String,
    @InjectedParam viewEntity: CommunityViewEntity,
) : PostFeedViewModel<MultiData>(
    repository,
    visitedPostsDao,
    communityDao,
    viewEntity,
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
}


