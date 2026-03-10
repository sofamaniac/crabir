package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.reboost.LocalTheme
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.votable.DownButton
import com.sofamaniac.reboost.ui.votable.ScoreString
import com.sofamaniac.reboost.ui.votable.UpButton

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
    val onClick = {
        if (clickable) {
            onClick(post)
        }
    }
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors().copy(containerColor = theme.cardBackground),
        onClick = { onClick(post) },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UpButton(viewModel)
                ScoreString(post.score.score, post.relationship.liked)
                DownButton(viewModel)
            }
            Column {
                PostInfo(post, modifier, enableThumbnail = true, viewModel = viewModel)
                PostHeader(post, showSubredditIcon = false, modifier = modifier)
            }
        }
    }
}
