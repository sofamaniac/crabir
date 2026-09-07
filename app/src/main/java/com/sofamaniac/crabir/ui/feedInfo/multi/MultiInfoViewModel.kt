package com.sofamaniac.crabir.ui.feedInfo.multi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.data.remote.reddit.SubredditAPI
import com.sofamaniac.crabir.domain.repository.feed.MultiCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MultiInfoViewModel(
    private val cache: MultiCache,
    private val redditApi: SubredditAPI,
    /** Subreddit's prefixed name */
    @InjectedParam subredditName: String,
) : ViewModel() {

    val info: MutableStateFlow<MultiData?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            info.value = cache.getBySlug(subredditName)
        }
    }
}
