package com.sofamaniac.reboost.ui.post

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalFullscreenHandler
import com.sofamaniac.reboost.LocalTheme
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.votable.DownButton
import com.sofamaniac.reboost.ui.votable.SavedButton
import com.sofamaniac.reboost.ui.votable.ScoreString
import com.sofamaniac.reboost.ui.votable.UpButton

@Composable
fun ColumnScope.FullscreenTopBar(
    enabled: Boolean,
    actions: @Composable () -> Unit = {}
) {
    val fullscreenManager = LocalFullscreenHandler.current!!
    AnimatedContent(
        targetState = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .safeContentPadding(),
        transitionSpec = {
            (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
        },
        label = "decoration animation"
    ) { enabled ->
        if (!enabled) {
            return@AnimatedContent
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            IconButton(onClick = fullscreenManager::pop) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = Color.White
                )
            }
            actions()
        }
    }
}

@Composable
fun ColumnScope.FullscreenBottomBar(
    post: PostData,
    enabled: Boolean,
) {
    val theme = LocalTheme.current
    AnimatedContent(
        targetState = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .safeContentPadding(),
        transitionSpec = {
            (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
        },
        label = "decoration bottom animation"
    ) { enabled ->
        if (!enabled) {
            return@AnimatedContent
        }

        Column {
            Text(
                post.title,
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UpButton(post.relationship.liked) { }
                    ScoreString(post.score.score, post.relationship.liked)
                    DownButton(post.relationship.liked) { }
                }
                SavedButton(post.relationship.saved) { }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Comment,
                            contentDescription = "comments",
                            tint = theme.secondaryText
                        )
                    }
                    Text("${post.numComments}", color = theme.secondaryText)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "share",
                        tint = theme.secondaryText
                    )
                }
            }
        }
    }
}