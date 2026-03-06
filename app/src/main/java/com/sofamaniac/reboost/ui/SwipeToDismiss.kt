package com.sofamaniac.reboost.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalFullscreenHandler
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun VerticalSwipeToDismiss(
    modifier: Modifier = Modifier,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val screenHeight = with(LocalDensity.current) {
        LocalResources.current.displayMetrics.heightPixels.toFloat()
    }


    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val density = LocalDensity.current
    @Suppress("DEPRECATION") val state by remember {
        mutableStateOf(
            AnchoredDraggableState(
                initialValue = DismissValue.Default,
                velocityThreshold = { with(density) { 1000.dp.toPx() } },
                positionalThreshold = positionalThreshold,
                snapAnimationSpec = tween(),
                decayAnimationSpec = decayAnimationSpec,
                anchors = DraggableAnchors {
                    DismissValue.Default at 0f
                    DismissValue.DismissedStart at -screenHeight
                    DismissValue.DismissedEnd at screenHeight
                }
            )
        )
    }


    val fullscreenManager = LocalFullscreenHandler.current!!

    LaunchedEffect(state.settledValue) {
        if (state.settledValue != DismissValue.Default) {
            fullscreenManager.pop()
        }
    }

    val blockDrag = Modifier.pointerInput(Unit) {
        detectDragGestures { _, _ -> }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Vertical,
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(state, positionalThreshold)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {}
        Column(
            content = content,
            modifier = Modifier
                .offset { IntOffset(0, state.requireOffset().roundToInt()) }
        )
        Column(
            content = topBar,
            modifier = Modifier
                .align(Alignment.TopCenter)
                // Prevent user from dragging on top bar
                .then(blockDrag)
                .offset { IntOffset(0, -state.requireOffset().roundToInt().absoluteValue) }
        )
        Column(
            content = bottomBar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                // Prevent user from dragging on bottom bar
                .then(blockDrag)
                .offset { IntOffset(0, state.requireOffset().roundToInt().absoluteValue) }
        )

    }
}

enum class DismissValue {
    Default,
    DismissedStart,
    DismissedEnd
}

@Composable
fun HorizontalSwipeToDismiss(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val screenWidth = with(LocalDensity.current) {
        LocalResources.current.displayMetrics.widthPixels.toFloat()
    }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val density = LocalDensity.current
    @Suppress("DEPRECATION") val state by remember {
        mutableStateOf(
            AnchoredDraggableState(
                initialValue = DismissValue.Default,
                velocityThreshold = { with(density) { 1000.dp.toPx() } },
                positionalThreshold = positionalThreshold,
                snapAnimationSpec = tween(),
                decayAnimationSpec = decayAnimationSpec,
                anchors = DraggableAnchors {
                    DismissValue.Default at 0f
                    //DismissValue.DismissedStart at -screenWidth
                    DismissValue.DismissedEnd at screenWidth
                }
            )
        )
    }


    val fullscreenManager = LocalFullscreenHandler.current!!
    LaunchedEffect(state.settledValue) {
        if (state.settledValue != DismissValue.Default) {
            fullscreenManager.pop()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal,
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(state, positionalThreshold)
            ),
    ) {
        Row(
            content = content,
            modifier = Modifier
                .offset { IntOffset(state.requireOffset().roundToInt(), 0) }
        )

    }
}
