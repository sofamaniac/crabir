package com.sofamaniac.reboost.ui.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
    AnimatedVisibility(
        visible = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .statusBarsPadding(),
        label = "decoration animation"
    ) {
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
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.id)
        }),
    title: @Composable () -> Unit = {}
) {
    val theme = LocalTheme.current
    AnimatedVisibility(
        visible = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .navigationBarsPadding(),
        label = "decoration animation"
    ) {

        Column(verticalArrangement = Arrangement.Bottom) {
            title()

            Text(
                post.title,
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                modifier = Modifier.padding(start = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UpButton(post.relationship.liked) { viewModel.upvote() }
                    ScoreString(post.score.score, post.relationship.liked)
                    DownButton(post.relationship.liked) { viewModel.downvote() }
                }
                SavedButton(post.relationship.saved) { viewModel.save(!post.relationship.saved) }
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