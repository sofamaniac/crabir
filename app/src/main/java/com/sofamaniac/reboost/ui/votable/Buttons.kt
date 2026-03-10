package com.sofamaniac.reboost.ui.votable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalTheme
import com.sofamaniac.reboost.ui.post.VotableInteraction
import kotlinx.coroutines.launch

const val MAX_OFFSET = 10f

@Composable
fun UpButton(viewModel: VotableInteraction) {
    val theme = LocalTheme.current

    val offset = remember { Animatable(0f) }
    val likes by viewModel.likes.collectAsState(initial = null)

    suspend fun animate(likes: Boolean?) {
        if (likes == true) return
        offset.animateTo(-MAX_OFFSET, animationSpec = tween(50, easing = EaseIn))
        offset.animateTo(
            0f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        )
    }

    val buttonColor = animateColorAsState(
        targetValue = if (likes == true) theme.primaryColor else Color.Gray,
        label = "button color"
    )

    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch { animate(likes) }
            viewModel.upvote()
        },
        modifier = Modifier.offset(y = offset.value.dp)
    ) {
        Icon(Icons.Filled.ThumbUp, "upvote", tint = buttonColor.value)
    }
}

@Composable
fun DownButton(viewModel: VotableInteraction) {
    val theme = LocalTheme.current
    val offset = remember { Animatable(0f) }
    val likes by viewModel.likes.collectAsState(initial = null)

    suspend fun animate(likes: Boolean?) {
        if (likes == false) return
        offset.animateTo(-MAX_OFFSET, animationSpec = tween(50, easing = EaseIn))
        offset.animateTo(
            0f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        )
    }

    val scope = rememberCoroutineScope()

    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) theme.downvote else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        scope.launch { animate(likes) }
        viewModel.downvote()
    }, modifier = Modifier.offset(y = offset.value.dp)) {
        Icon(Icons.Filled.ThumbDown, "downvote", tint = buttonColor.value)
    }
}

@Composable
fun SavedButton(viewModel: VotableInteraction) {
    val scale = remember { Animatable(1f) }
    val saved by viewModel.saved.collectAsState(initial = false)
    val buttonColor = animateColorAsState(
        targetValue = if (saved) Color.Yellow else Color.Gray,
        label = "button color"
    )

    suspend fun animate(saved: Boolean) {
        if (saved) {
            scale.animateTo(1.7f, animationSpec = tween(100, easing = EaseOut))
            scale.animateTo(1f, animationSpec = tween(100, easing = EaseIn))
        }
    }

    val scope = rememberCoroutineScope()
    IconButton(onClick = {
        scope.launch { animate(!saved) }
        viewModel.save(!saved)
    }, modifier = Modifier.scale(scale.value)) {
        if (saved) {
            Icon(Icons.Filled.Bookmark, "save", tint = buttonColor.value)
        } else {
            Icon(Icons.Outlined.BookmarkBorder, "save", tint = buttonColor.value)
        }
    }
}