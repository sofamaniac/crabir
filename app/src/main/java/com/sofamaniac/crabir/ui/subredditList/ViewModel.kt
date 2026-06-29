/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subredditList

import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SubscriptionViewModel @Inject constructor(subscriptionsRepository: SubscriptionsRepository) :
    ViewModel() {

    val subscriptions: StateFlow<List<Thing.Subreddit>?> = subscriptionsRepository.subscriptions

}
