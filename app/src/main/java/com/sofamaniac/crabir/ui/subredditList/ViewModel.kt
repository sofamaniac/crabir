/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subredditList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SubscriptionViewModel(private val subscriptionsRepository: SubscriptionsRepository) :
    ViewModel() {
    private val subscriptions: StateFlow<List<Thing.Subreddit>> =
        subscriptionsRepository.subscriptions
    private val sortedSubs =
        subscriptions.map { subs -> subs.sortedBy { it.data.displayName } }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList(),
        )

    private var _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter


    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredSubs = sortedSubs.flatMapLatest { subs ->
        _filter.map { filter ->
            subs.filter {
                it.data.displayName.lowercase().contains(filter.lowercase())
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList(),
    )

    fun setFilter(filter: String) {
        Log.d("SubscriptionViewModel", "setFilter: $filter")
        this._filter.value = filter
    }


}
