package com.sofamaniac.reboost.ui.post

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.sofamaniac.reboost.settings.DefaultReboostTheme
import com.sofamaniac.reboost.settings.themeDataStore

@Composable
fun UpButton(likes: Boolean?, onClick: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
        coroutineScope.coroutineContext
    )

    val buttonColor = animateColorAsState(
        targetValue = if (likes == true) theme.primaryColor else Color.Gray,
        label = "button color"
    )
    IconButton(
        onClick = onClick
    ) {
        Icon(Icons.Filled.ThumbUp, "upvote", tint = buttonColor.value)
    }
}

@Composable
fun DownButton(likes: Boolean?, onClick: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
        coroutineScope.coroutineContext
    )

    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) theme.downvote else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ThumbDown, "downvote", tint = buttonColor.value)
    }
}

@Composable
fun SavedButton(saved: Boolean, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (saved) Color.Yellow else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = onClick) {
        if (saved) {
            Icon(Icons.Filled.Bookmark, "save", tint = buttonColor.value)
        } else {
            Icon(Icons.Outlined.BookmarkBorder, "save", tint = buttonColor.value)
        }
    }
}