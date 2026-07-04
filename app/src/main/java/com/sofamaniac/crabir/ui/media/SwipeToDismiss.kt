package com.sofamaniac.crabir.ui.media

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.IntOffset
import com.sofamaniac.crabir.settings.theme.ThemeMode
import com.sofamaniac.crabir.settings.theme.rememberThemeMode
import com.sofamaniac.crabir.settings.theme.rememberThemeSettings
import com.sofamaniac.crabir.settings.theme.setSystemBarsColor
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

@Composable
fun VerticalSwipeToDismiss(
    modifier: Modifier = Modifier.Companion,
    onDismiss: () -> Unit,
    velocityThreshold: Float = 5000f,
    threshold: Float = 0.3f,
    enabled: Boolean = true,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val screenHeight = LocalResources.current.displayMetrics.heightPixels.toFloat()
    val offsetY = remember { Animatable(0f) }


    val scope = rememberCoroutineScope()
    val state = rememberDraggableState { delta ->
        scope.launch {
            offsetY.snapTo(offsetY.value + delta)
        }
    }


    // prevent dragging from the top / bottom bars
    val blockDrag = Modifier.pointerInput(Unit) {
        detectDragGestures { _, _ -> }
    }

    val theme = rememberThemeSettings()
    val updateBars = setSystemBarsColor()
    var isDismissing by remember { mutableStateOf(false) }
    val themeMode = rememberThemeMode()
    DisposableEffect(theme.mode) {
        updateBars(ThemeMode.Dark, Color.Black)
        onDispose {
            updateBars(theme.mode, theme.currentTheme(themeMode).toolbarBackground)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .draggable(
                enabled = enabled,
                state = state,
                orientation = Orientation.Vertical,
                onDragStopped = { velocity ->
                    val fraction = abs(offsetY.value) / screenHeight
                    if (fraction >= threshold || abs(velocity) >= velocityThreshold) {
                        isDismissing = true
                        scope.launch {
                            offsetY.animateTo(offsetY.value.sign * screenHeight)
                            onDismiss()
                        }
                    } else if (!isDismissing) {
                        scope.launch {
                            offsetY.animateTo(0f, spring())
                        }
                    }
                },
            )
    ) {
        Column(
            content = content,
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
        )
        Column(
            content = topBar,
            modifier = Modifier
                .align(Alignment.TopCenter)
                // Prevent user from dragging on top bar
                .then(blockDrag)
                .offset { IntOffset(0, -offsetY.value.roundToInt().absoluteValue) }
        )
        Column(
            content = bottomBar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                // Prevent user from dragging on bottom bar
                .then(blockDrag)
                .offset { IntOffset(0, offsetY.value.roundToInt().absoluteValue) }
        )

    }
}