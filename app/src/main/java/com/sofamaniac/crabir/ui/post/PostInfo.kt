package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.SearchRoute
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.Flair
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.votable.ScoreString

/** Show a post title and thumbnail.
 * @param post The post to show.
 * @param modifier The modifier to apply to the root layout.
 * @param enableThumbnail Whether to enable the preview of the thumbnail. Defaults to true.
 * */
@Composable
fun PostInfo(
    post: PostData,
    modifier: Modifier = Modifier,
    enableThumbnail: Boolean = true,
    read: Boolean = false,
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(key = post.id) { factory ->
        factory.create(post.id)
    },
) {
    val navController = LocalNavController.current!!
    val theme = LocalTheme.current
    val likes by viewModel.likes.collectAsState(initial = post.relationship.liked)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val width = if (enableThumbnail) 0.8f else 1f
            val titleModifier = Modifier.fillMaxWidth(fraction = width)
            val titleColor = when {
                post.isDistinguished -> theme.announcement
                read -> theme.readPost
                else -> theme.postTitle
            }
            Text(
                post.title,
                color = titleColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = titleModifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (post.spoiler) {
                    Text(
                        "SPOILER",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Red),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Red,
                                shape = RoundedCornerShape(corner = CornerSize(2.dp)),
                            )
                            .cartouche(Color.Transparent)
                    )
                }
                Flair(post.linkFlair, modifier = Modifier.clickable {
                    navController.navigate(
                        SearchRoute(
                            subreddit = post.subreddit.name,
                            flair = post.linkFlair.text
                        )
                    )
                })
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                ScoreString(post.score.score, likes)
                Text(
                    buildAnnotatedString {
                        append(" · ")
                        append("${post.numComments} comments")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.secondaryText
                )
                if (post.over18) {
                    Text(
                        "NSFW", fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                        modifier = Modifier.cartouche(Color.Red)
                    )
                }
            }
        }
        if (enableThumbnail) {
            Thumbnail(post)
        }
    }
}