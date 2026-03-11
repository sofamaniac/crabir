package com.sofamaniac.crabir

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FullscreenManager {
    private var _fullscreenViews = MutableStateFlow(emptyList<@Composable () -> Unit>())

    val current: Flow<@Composable (() -> Unit)?> =
        _fullscreenViews.map { it.lastOrNull() }.distinctUntilChanged()

    val size: Flow<Int> = _fullscreenViews.map { it.size }.distinctUntilChanged()


    fun push(view: @Composable () -> Unit) {
        _fullscreenViews.update {
            it + view
        }
    }

    fun pop() {
        _fullscreenViews.update {
            it.dropLast(1)
        }
    }

}

val LocalFullscreenHandler = compositionLocalOf<FullscreenManager?> { null }

@Composable
fun FullscreenHandler(content: @Composable () -> Unit) {
    val fullscreenManager = remember {
        FullscreenManager()
    }
    val fullscreenView by fullscreenManager.current.collectAsState(null)
    val backHandlerActive by fullscreenManager.size.map { it > 0 }.collectAsState(false)
    CompositionLocalProvider(LocalFullscreenHandler provides fullscreenManager) {
        BackHandler(enabled = backHandlerActive) {
            fullscreenManager.pop()
        }
        Box {
            content()
            fullscreenView?.invoke()
        }
    }
}