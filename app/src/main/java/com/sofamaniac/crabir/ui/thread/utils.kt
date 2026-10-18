package com.sofamaniac.crabir.ui.thread

import android.view.KeyEvent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    nextTopLevel: () -> Unit,
    previousTopLevel: () -> Unit,
    move: (Int) -> Unit,
) {
    HorizontalFloatingToolbar(
        expanded = true,
        modifier = modifier,
        leadingContent = {
            IconButton(onClick = previousTopLevel) {
                Icon(
                    Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = "Previous top level comment"
                )
            }
            IconButton(onClick = { move(-1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Previous comment")
            }
        },
        trailingContent = {
            IconButton(onClick = { move(1) }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Next comment")
            }
            IconButton(onClick = nextTopLevel) {
                Icon(
                    Icons.Default.KeyboardDoubleArrowDown,
                    contentDescription = "Next top level comment"
                )
            }
        }
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        VerticalDivider(modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Composable
fun VolumeKeyNavigation(move: (Int) -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current

    DisposableEffect(context) {
        val keyEventDispatcher = ViewCompat.OnUnhandledKeyEventListenerCompat { _, event ->
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    move(-1)
                    true
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    move(1)
                    true
                }

                else -> false
            }
        }
        ViewCompat.addOnUnhandledKeyEventListener(view, keyEventDispatcher)
        onDispose {
            ViewCompat.removeOnUnhandledKeyEventListener(view, keyEventDispatcher)
        }
    }
}
