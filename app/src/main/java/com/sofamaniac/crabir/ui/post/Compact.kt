package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton

@Composable
fun CompactView(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    onClick: (PostData) -> Unit = {},
    canStartVideo: Boolean = false,
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.id)
        }),
) {
    val theme = LocalTheme.current
    val onClick = { post: PostData ->
        if (clickable) {
            onClick(post)
        }
    }
    val likes by viewModel.likes.collectAsState(post.relationship.liked)
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors().copy(containerColor = theme.cardBackground),
        onClick = { onClick(post) },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UpButton(viewModel)
                ScoreString(post.score.score, likes)
                DownButton(viewModel)
            }
            Column {
                PostInfo(post, modifier, enableThumbnail = true, viewModel = viewModel)
                PostHeader(post, showSubredditIcon = false, modifier = modifier)
            }
        }
    }
}
