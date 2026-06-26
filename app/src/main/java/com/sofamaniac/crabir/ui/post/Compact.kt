package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton

@Composable
fun CompactView(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    markAsRead: () -> Unit = {},
    canStartVideo: Boolean = false,
    read: Boolean = false,
    showHidden: Boolean = false,
    viewModel: LinkViewModel = hiltViewModel<LinkViewModel, LinkViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post)
        }),
) {
    val navController = LocalNavController.current
    val onClick = {
        if (clickable) {
            markAsRead()
            navController?.navigate(PostRoute(post.permalink))
        }
    }
    val likes by viewModel.likes.collectAsState(post.relationship.liked)
    val post by viewModel.post.collectAsState(post)
    if (!showHidden && post.relationship.hidden) {
        return
    }
    ThemedCard(
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UpButton(likes, onClick = { viewModel.upvote(post.name) })
                ScoreString(post.score.score, likes)
                DownButton(likes, onClick = { viewModel.downvote(post.name) })
            }
            Column {
                PostInfo(
                    post,
                    modifier,
                    enableThumbnail = true,
                    read = read,
                    likes = likes,
                )
                PostHeader(post, showSubredditIcon = false, modifier = modifier)
            }
        }
    }
}
