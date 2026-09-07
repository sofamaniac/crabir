package com.sofamaniac.crabir.ui.feedInfo.subreddit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.reddit.SubredditAPI
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SubredditInfoViewModel(
    private val subredditCache: SubredditCache,
    private val redditApi: SubredditAPI,
    /** Subreddit's prefixed name */
    @InjectedParam subredditName: String,
) : ViewModel() {

    val info: MutableStateFlow<SubredditData?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            info.value = subredditCache.getBySlug(subredditName)
        }
    }

    fun favorite(favorite: Boolean) {
        val infoLoc = info.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            redditApi.favorite(info.value!!.displayName, !infoLoc.userHasFavorited).onSuccess {
                info.value = infoLoc.copy(userHasFavorited = favorite)
                info.value?.let { subredditCache.update(it) }
            }
        }
    }

    private fun subscribeInner(action: SubscribeAction) {
        val infoLoc = info.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            redditApi.subscribe(action, info.value!!.displayName).onSuccess {
                info.value =
                    infoLoc.copy(userIsSubscriber = action == SubscribeAction.SUBSCRIBE)
                info.value?.let { subredditCache.update(it) }
            }
        }
    }


    fun subscribe() {
        subscribeInner(SubscribeAction.SUBSCRIBE)
    }

    fun unsubscribe() {
        subscribeInner(SubscribeAction.UNSUBSCRIBE)
    }

}
