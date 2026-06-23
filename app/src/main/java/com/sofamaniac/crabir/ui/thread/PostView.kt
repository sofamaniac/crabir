package com.sofamaniac.crabir.ui.thread

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalSharedTransitionScope
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.SharedElementKey
import com.sofamaniac.crabir.ui.SharedElementType
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.card.PostBody

@Composable
internal fun PostView(
    post: PostData,
    threadViewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = true,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val currentAccount = LocalRedditAccount.current
    val markdownState by threadViewModel.markdown.collectAsState()
    PostCard(
        post,
        threadViewModel = threadViewModel,
        animatedVisibilityScope = animatedVisibilityScope
    ) {
        if (!post.isCrosspost) {
            PostBody(
                post,
                canPlayVideo = canPlayVideo,
                maxLines = null,
                enableLinkFullSizePreview = false,
                forceShowSelftext = true,
                animatedVisibilityScope = animatedVisibilityScope,
                markAsRead = { threadViewModel.visitPost(post, currentAccount.id) },
                markdownState = markdownState,
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
            likes = post.relationship.liked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: LinkViewModel = hiltViewModel<LinkViewModel, LinkViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post)
        }
    ),
    threadViewModel: ThreadViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    body: @Composable ColumnScope.() -> Unit,
) {
    val likes by viewModel.likes.collectAsState(null)
    val modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        val state = sharedTransitionScope.rememberSharedContentState(
            key = SharedElementKey(
                post.name,
                SharedElementType.Post
            )
        )
        val animatedModifier = Modifier.sharedElement(
            state,
            animatedVisibilityScope = animatedVisibilityScope
        )
        ThemedCard(
            shape = RoundedCornerShape(0),
            modifier = Modifier
                .fillMaxWidth()
                .then(animatedModifier),
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
                ReplyButton(parentId = post.name, submitComment = threadViewModel::submitComment) {
                    ThemedCard(modifier = Modifier.padding(all = 16.dp)) {
                        Text(post.author.username, modifier = modifier)
                        Text(post.title, modifier = modifier)
                        RedditMarkdown(
                            post.selftext.markdown,
                            maxLines = 5,
                            modifier = modifier,
                        )
                    }
                }
            }
        }
    }
}