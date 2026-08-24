package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RefreshIndicator(
    refreshing: Boolean,
    state: PullToRefreshState = rememberPullToRefreshState(),
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        if (refreshing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            PullToRefreshDefaults.Indicator(
                state,
                isRefreshing = false,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}