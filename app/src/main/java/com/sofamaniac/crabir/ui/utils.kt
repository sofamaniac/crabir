/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui

import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.AccountManager
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import net.openid.appauth.AuthState
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Single
import java.time.Clock
import java.time.Duration
import java.util.Locale

fun formatElapsedTimeLocalized(
    creationDate: kotlin.time.Instant,
    locale: Locale = Locale.getDefault(),
): String {
    val end = Clock.systemUTC().millis()
    val duration = Duration.ofMillis(kotlin.math.abs(end - creationDate.toEpochMilliseconds()))

    return when {
        duration.toDays() >= 365 -> String.format(locale, "%dy", duration.toDays() / 365)
        duration.toDays() >= 30 -> String.format(locale, "%dmo", duration.toDays() / 30)
        duration.toDays() > 0 -> String.format(locale, "%dd", duration.toDays())
        duration.toHours() > 0 -> String.format(locale, "%dh", duration.toHours())
        duration.toMinutes() > 0 -> String.format(locale, "%dm", duration.toMinutes())
        else -> String.format(locale, "%ds", duration.seconds)
    }
}

@Composable
fun rememberCurrentAccount(): RedditAccount {
    val viewModel: CurrentAccountViewModel = koinViewModel()
    val account by viewModel.account.collectAsState()
    return account
}

@KoinViewModel
class CurrentAccountViewModel(accountManager: AccountManager) : ViewModel() {
    val account = accountManager.accountsRepository.activeAccount.distinctUntilChanged { old, new ->
        old.id == new.id && old.info == new.info
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        RedditAccount.uninitialized(-2, AuthState())
    )
}

@Single
class HistoryManager(val history: VisitedPostsDao) {
    suspend fun addPost(name: Fullname, account: Int) {
        val entity = history.getPost(name) ?: VisitedPostEntity(
            id = name,
            visitedAt = 0,
            visitedBy = account
        )
        history.insert(entity.copy(visitedAt = Clock.systemUTC().millis(), visitedBy = account))
    }

    suspend fun updateComments(name: Fullname, comments: List<String>, focusedComment: String) {
        val entity = history.getPost(name)
        if (entity == null) {
            Log.e("HistoryManager", "updateComments: Post not found in database ($name)")
            return
        }
        history.insert(entity.copy(comments = comments, focusedComment = focusedComment))
    }
}

@Composable
fun SaveToHistory(name: Fullname, historyManager: HistoryManager = koinInject()) {
    val account = LocalRedditAccount.current.id
    LaunchedEffect(name) {
        historyManager.addPost(name, account)
    }
}

/** Make composable clickable while preventing touch event in children */
fun Modifier.protectedTouch(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val pass = PointerEventPass.Initial
    if (!enabled) return this
    return this.then(
        Modifier.pointerInput(pass) {
            awaitEachGesture {
                val down = awaitFirstDown(pass = pass)
                down.consume()
                val up = waitForUpOrCancellation(pass = pass)
                if (up != null) {
                    onClick()
                }
            }
        }
    )
}

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back)
        )
    }
}

@Composable
fun CloseButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.Default.Close,
            contentDescription = stringResource(R.string.close)
        )
    }
}