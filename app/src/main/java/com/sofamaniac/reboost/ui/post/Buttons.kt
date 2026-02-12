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
import androidx.compose.ui.graphics.Color

@Composable
fun UpButton(likes: Boolean?, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (likes == true) Color.Companion.Red else Color.Companion.Gray,
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
    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) Color.Companion.Blue else Color.Companion.Gray,
        label = "button color"
    )
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ThumbDown, "downvote", tint = buttonColor.value)
    }
}

@Composable
fun SavedButton(saved: Boolean, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (saved) Color.Companion.Yellow else Color.Companion.Gray,
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