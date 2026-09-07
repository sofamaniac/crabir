package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.ui.components.SubredditIcon
import com.sofamaniac.crabir.ui.feedInfo.subreddit.FavoriteButton
import com.sofamaniac.crabir.ui.feedInfo.subreddit.SubscribeButton
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown

@Composable
fun SubredditInfo(info: SubredditData, viewModel: SubredditViewModel) {
    val theme = LocalTheme.current
    Column(
        modifier = Modifier
            .background(theme.cardBackground),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            AsyncImage(
                info.bannerImg.ifBlank { info.bannerBackgroundImage ?: "" },
                "Banner background image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            SubredditIcon(
                info.displayName,
                info.icon,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 16.dp)
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        info.displayNamePrefixed,
                        style = MaterialTheme.typography.titleMedium,
                        color = theme.highlight
                    )
                    Text(
                        "${info.subscribers} members",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.secondaryText
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SubscribeButton(info.userIsSubscriber) {
                    if (info.userIsSubscriber) {
                        viewModel.unsubscribe()
                    } else {
                        viewModel.subscribe()
                    }
                }
                FavoriteButton(info.userHasFavorited) {
                    viewModel.favorite(!info.userHasFavorited)
                }
            }
            RedditMarkdown(info.publicDescription)
        }
    }
}
