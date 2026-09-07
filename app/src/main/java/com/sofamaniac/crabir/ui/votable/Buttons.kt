package com.sofamaniac.crabir.ui.votable

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
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import kotlinx.coroutines.launch

const val MAX_OFFSET = 10f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpButton(likes: Boolean?, onClick: () -> Unit) {
    val theme = LocalTheme.current

    val offset = remember { Animatable(0f) }
    suspend fun animate(likes: Boolean?) {
        if (likes == true) return
        offset.animateTo(-MAX_OFFSET, animationSpec = tween(50, easing = EaseIn))
        offset.animateTo(
            0f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        )
    }

    val buttonColor = animateColorAsState(
        targetValue = if (likes == true) theme.primaryColor else LocalContentColor.current,
        label = "button color"
    )

    val scope = rememberCoroutineScope()
    val neutral = stringResource(R.string.neutral_vote)
    val upvote = stringResource(R.string.upvote)
    val description = if (likes == true) neutral else upvote
    val icon = if (likes == true) R.drawable.filled_arrow_up else R.drawable.arrow_shape_up
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        state = rememberTooltipState()
    ) {
        IconButton(
            onClick = {
                scope.launch { animate(likes) }
                onClick()
            },
            modifier = Modifier.offset { IntOffset(0, offset.value.toInt()) }
        ) {
            Icon(
                painter = painterResource(icon),
                description,
                tint = buttonColor.value
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownButton(likes: Boolean?, onClick: () -> Unit) {
    val theme = LocalTheme.current
    val offset = remember { Animatable(0f) }

    suspend fun animate(likes: Boolean?) {
        if (likes == false) return
        offset.animateTo(MAX_OFFSET, animationSpec = tween(50, easing = EaseIn))
        offset.animateTo(
            0f,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        )
    }

    val scope = rememberCoroutineScope()

    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) theme.downvote else LocalContentColor.current,
        label = "button color"
    )
    val neutral = stringResource(R.string.neutral_vote)
    val downvote = stringResource(R.string.downvote)
    val description = if (likes == false) neutral else downvote
    val icon = if (likes == false) R.drawable.filled_arrow_up else R.drawable.arrow_shape_up
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        state = rememberTooltipState()
    ) {
        IconButton(
            onClick = {
                scope.launch { animate(likes) }
                onClick()
            },
            modifier = Modifier.offset { IntOffset(0, offset.value.toInt()) }
        ) {
            Icon(
                painter = painterResource(icon),
                description,
                tint = buttonColor.value,
                modifier = Modifier.scale(-1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedButton(saved: Boolean, onClick: () -> Unit) {
    val theme = LocalTheme.current
    val scale = remember { Animatable(1f) }
    val buttonColor = animateColorAsState(
        targetValue = if (saved) theme.saved else LocalContentColor.current,
        label = "button color"
    )

    suspend fun animate(saved: Boolean) {
        if (saved) {
            scale.animateTo(1.7f, animationSpec = tween(100, easing = EaseOut))
            scale.animateTo(1f, animationSpec = tween(100, easing = EaseIn))
        }
    }

    val scope = rememberCoroutineScope()
    val save = stringResource(R.string.save)
    val unsave = stringResource(R.string.unsave)
    val description = if (saved) unsave else save
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        state = rememberTooltipState()
    ) {
        IconButton(onClick = {
            scope.launch { animate(!saved) }
            onClick()
        }, modifier = Modifier.scale(scale.value)) {
            if (saved) {
                Icon(Icons.Filled.Bookmark, description, tint = buttonColor.value)
            } else {
                Icon(Icons.Outlined.BookmarkBorder, description, tint = buttonColor.value)
            }
        }
    }
}
