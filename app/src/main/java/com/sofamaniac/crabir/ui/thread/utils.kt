package com.sofamaniac.crabir.ui.thread

import android.view.KeyEvent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat

@Composable
fun Toolbar(modifier: Modifier = Modifier, move: (Int) -> Unit) {
    HorizontalFloatingToolbar(
        expanded = true,
        modifier = modifier,
        leadingContent = {
            IconButton(onClick = { move(1) }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Next comment")
            }
        },
        trailingContent = {
            IconButton(onClick = { move(-1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Previous comment")
            }
        }) {
        Spacer(modifier = Modifier.width(24.dp))
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
