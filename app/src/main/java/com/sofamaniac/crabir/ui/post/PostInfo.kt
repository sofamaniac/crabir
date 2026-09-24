package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.SearchRoute
import com.sofamaniac.crabir.settings.post.FlairSettings
import com.sofamaniac.crabir.ui.components.Flair
import com.sofamaniac.crabir.ui.components.Over18Cartouche
import com.sofamaniac.crabir.ui.components.SpoilerCartouche
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
    likes: Boolean?,
    flairSettings: FlairSettings = LocalPostSettings.current.flairSettings,
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    val blur = LocalFiltersSettings.current.blurNSFW && post.over18
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val titleColor = when {
                post.stickied -> theme.announcement
                read -> theme.readPost
                else -> theme.postTitle
            }
            Text(
                post.title,
                color = titleColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (post.spoiler) {
                    SpoilerCartouche()
                }
                if (flairSettings.showFlair) {
                    Flair(
                        post.linkFlair,
                        showColor = flairSettings.showFlairColor,
                        showEmoji = flairSettings.showFlairEmoji,
                        modifier = Modifier
                            .clickable(enabled = flairSettings.clickable) {
                                navController?.navigate(
                                    SearchRoute(
                                        subreddit = post.subreddit.name,
                                        flair = post.linkFlair.text
                                    )
                                )
                            }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                ScoreString(post.score.score, likes)
                Text(
                    buildAnnotatedString {
                        append(" · ")
                        append(
                            pluralStringResource(
                                R.plurals.post_comments_count,
                                post.numComments,
                                post.numComments
                            )
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.secondaryText
                )
                if (post.over18) {
                    Over18Cartouche()
                }
            }
        }
        if (enableThumbnail) {
            val modifier = Modifier
                .width(64.dp)
                .aspectRatio(1f)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            Thumbnail(post, blur = blur, modifier = modifier)
        }
    }
}
