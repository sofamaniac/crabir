package com.sofamaniac.crabir.ui.thread

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.PostBody
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.VotableViewModel

@Composable
internal fun PostView(
    post: PostData,
    threadViewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = true,
) {
    PostCard(post, threadViewModel = threadViewModel) {
        if (!post.isCrosspost) {
            PostBody(
                post,
                canPlayVideo = canPlayVideo,
                maxLines = Int.MAX_VALUE,
                forceShowSelftext = true,
                enableLinkFullSizePreview = false,
            )
        } else {
            val parent = post.crosspostParentList.first()
            val fullscreenManager = LocalFullscreenHandler.current!!
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Gray), shape = ShapeDefaults.Medium)
                    .clickable(onClick = {
                        fullscreenManager.push {
                            ThreadView(
                                permalink = parent.permalink,
                                dismiss = {
                                    fullscreenManager.pop()
                                })
                        }
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
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.name.name, post.subreddit.name)
        }
    ),
    threadViewModel: ThreadViewModel,
    body: @Composable ColumnScope.() -> Unit,
) {
    val modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    ThemedCard(
        shape = RoundedCornerShape(0),
        modifier = Modifier.fillMaxWidth(),
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
            viewModel = viewModel,
        )
        body()
        BottomRow(post, modifier, viewModel = viewModel) {
            ReplyButton(parentId = post.name, threadViewModel = threadViewModel) {
                ThemedCard(modifier = Modifier.padding(all = 16.dp)) {
                    Text(post.author.username, modifier = modifier)
                    Text(post.title, modifier = modifier)
                    RedditMarkdown(
                        post.selftext.markdown,
                        maxLines = 5,
                        modifier = modifier
                    )
                }
            }
        }
    }
}