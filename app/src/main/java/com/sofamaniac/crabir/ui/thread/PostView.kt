package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.domain.model.votable.post.RemovalKind
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostRoute
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.data.NetworkPolicy
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.card.PostBody
import com.sofamaniac.crabir.ui.richtext.Richtext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PostView(
    post: PostData,
    threadViewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    val videoSettings = LocalDataSettings.current.videoQuality
    val connectionState = LocalContext.current.onWifiConnection
    val canPlayVideo = when (videoSettings.autostart) {
        NetworkPolicy.Always -> true
        NetworkPolicy.OnWifi -> connectionState
        else -> false
    }
    val imageHeight = LocalCommentsSettings.current.postMediaPreview
    PostCard(
        post,
        threadViewModel = threadViewModel,
        modifier = modifier,
    ) {
        if (!post.isCrosspost) {
            PostBody(
                post,
                canPlayVideo = canPlayVideo,
                maxLines = null,
                enableLinkFullSizePreview = false,
                imageHeight = imageHeight,
                forceShowSelftext = true,
                markAsRead = {},
            )
        } else {
            val parent = post.crosspostParentList.first()
            val navController = LocalNavController.current!!
            Column {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(BorderStroke(1.dp, Color.Gray), shape = ShapeDefaults.Medium)
                        .clickable(onClick = {
                            navController.navigate(PostRoute(parent.permalink, null))
                        })
                        .padding(8.dp)
                ) {
                    CrossPostView(parent)
                }
                if (post.selftext.richtext.document.isNotEmpty()) {
                    Richtext(
                        post.selftext.richtext,
                        mediaMetadata = post.mediaMetadata,
                        threadId = post.id
                    )
                }
            }
        }
        val removalKind = post.removalInfo.kind
        if (removalKind != null) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                HorizontalDivider()
                when (removalKind) {
                    RemovalKind.Moderator -> Text(
                        stringResource(
                            R.string.post_removed_by_moderation,
                            post.subredditDetails?.displayNamePrefixed ?: "r/${post.subreddit.name}"
                        ),
                        fontWeight = FontWeight.Bold
                    )

                    RemovalKind.Reddit -> Text(
                        stringResource(R.string.post_removed_by_reddit),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
internal fun CrossPostView(
    post: PostData,
    modifier: Modifier = Modifier,
) {
    Column {
        PostHeader(post, showSubredditIcon = false)
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = true,
            likes = post.relationship.liked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: LinkViewModel = koinViewModel { parametersOf(post) },
    threadViewModel: ThreadViewModel,
    body: @Composable ColumnScope.() -> Unit,
) {
    val likes by viewModel.likes.collectAsState()
    val modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)

    ThemedCard(
        roundedCorners = false,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PostHeader(
            post,
            modifier = modifier.padding(vertical = 8.dp)
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && !post.isCrosspost,
            likes = likes
        )
        body()
        BottomRow(post, modifier, interactions = viewModel) {
            ReplyButton(startReply = { threadViewModel.replyTo(post.name) })
        }
    }
}
