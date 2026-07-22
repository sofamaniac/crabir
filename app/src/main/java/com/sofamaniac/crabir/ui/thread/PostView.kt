package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.post.AutoPlayVideo
import com.sofamaniac.crabir.settings.post.rememberPostsSettings
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.card.PostBody
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PostView(
    post: PostData,
    threadViewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    val currentAccount = LocalRedditAccount.current
    val autoPlayVideos = rememberPostsSettings().linksSettings.autoPlayVideos
    val connectionState = LocalContext.current.onWifiConnection
    val canPlayVideo = when (autoPlayVideos) {
        AutoPlayVideo.Always -> true
        AutoPlayVideo.Wifi -> connectionState
        AutoPlayVideo.Never -> false
    }
    val markdownState by threadViewModel.markdown.collectAsState()
    PostCard(
        post,
        threadViewModel = threadViewModel
    ) {
        if (!post.isCrosspost) {
            PostBody(
                post,
                canPlayVideo = canPlayVideo,
                maxLines = null,
                enableLinkFullSizePreview = false,
                forceShowSelftext = true,
                markAsRead = { threadViewModel.visitPost(post, currentAccount.id) },
            )
        } else {
            val parent = post.crosspostParentList.first()
            val navController = LocalNavController.current!!
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
            likes = { post.relationship.liked },
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
    val likes by viewModel.likes.collectAsState(null)
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
            likes = { likes }
        )
        body()
        BottomRow(post, modifier, interactions = viewModel) {
            ReplyButton(post.name, threadViewModel)
        }
    }
}