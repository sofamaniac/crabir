/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subredditList

import androidx.lifecycle.ViewModel
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.repository.SubscriptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(private val subscriptionsRepository: SubscriptionsRepository) :
    ViewModel() {

    val subscriptions: StateFlow<List<Thing.Subreddit>?> = subscriptionsRepository.subscriptions

}
