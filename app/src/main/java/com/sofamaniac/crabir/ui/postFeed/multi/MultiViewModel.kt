package com.sofamaniac.crabir.ui.postFeed.multi

import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.repository.feed.MultiPostsRepository
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MultiViewModel(
    repository: MultiPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: MultiDao,
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
            refresh()
        }
    }
}
