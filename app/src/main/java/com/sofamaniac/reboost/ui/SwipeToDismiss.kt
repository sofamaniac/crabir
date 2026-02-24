package com.sofamaniac.reboost.ui

import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.IntOffset
import com.sofamaniac.reboost.LocalFullscreenHandler
import kotlin.math.roundToInt

@Composable
fun VerticalSwipeToDismiss(
    backgroundContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val screenHeight = with(LocalDensity.current) {
        LocalResources.current.displayMetrics.heightPixels.toFloat()
    }


    val state by remember {
        mutableStateOf(
            AnchoredDraggableState(
                initialValue = DismissValue.Default,
                anchors = DraggableAnchors {
                    DismissValue.Default at 0f
                    DismissValue.DismissedUp at -screenHeight
                    DismissValue.DismissedDown at screenHeight
                })
        )
    }
    val fullscreenManager = LocalFullscreenHandler.current!!

    LaunchedEffect(state.currentValue) {
        if (state.currentValue != DismissValue.Default) {
            fullscreenManager.pop()
        }
    }

    Box(
        modifier = Modifier
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Vertical,
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(state, positionalThreshold)
            ),
        propagateMinConstraints = true,
    ) {
        Column(content = backgroundContent, modifier = Modifier.matchParentSize())
        Column(
            content = content,
            modifier = Modifier
                .offset { IntOffset(0, state.requireOffset().roundToInt()) }
        )
    }
}

enum class DismissValue {
    Default,
    DismissedUp,
    DismissedDown
}
