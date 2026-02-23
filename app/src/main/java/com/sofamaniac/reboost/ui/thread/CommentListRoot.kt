package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.FullscreenManager
import com.sofamaniac.reboost.domain.model.CommentType
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.markdown.RedditMarkdown
import com.sofamaniac.reboost.ui.post.PostCard
import com.sofamaniac.reboost.ui.post.PostGallery
import com.sofamaniac.reboost.ui.post.PostHeader
import com.sofamaniac.reboost.ui.post.PostImage
import com.sofamaniac.reboost.ui.post.PostInfo
import com.sofamaniac.reboost.ui.post.PostVideo
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        val comments by viewModel.comments.map { it.flattenComments() }
            .collectAsState(initial = emptyList())
        val post by viewModel.post.collectAsState(initial = null)
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            // Show post
            item {
                if (post != null) {
                    PostView(post!!)
                }
            }
            items(comments.size, key = { index -> comments[index].name }) { index ->
                when (val comment = comments[index]) {
                    is CommentType.Comment -> CommentView(
                        comment,
                        viewModel,
                    )

                    is CommentType.More -> MoreViewer(comment)
                }
                if ((comments.getOrNull(index + 1)?.depth ?: 0) == 0) {
                    HorizontalDivider()
                }
            }
            item {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.1f))
            }
        }
    }
}

@Composable
internal fun PostView(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = true,
) {
    PostCard(post, clickable = false, enableThumbnail = !post.isCrosspost) {
        if (!post.isCrosspost) {
            when (post.kind) {
                Kind.Image -> {
                    PostImage(post, modifier.fillMaxWidth())
                }

                Kind.Video -> {
                    PostVideo(post, modifier.fillMaxWidth(), canPlayVideo = canPlayVideo)
                }

                Kind.Gallery -> {
                    PostGallery(
                        post,
                        modifier.fillMaxWidth(),
                        canPlayVideo = canPlayVideo
                    )
                }

                else -> {}
            }
            // always show selftext if there is one
            val selftext = post.selftext.markdown
            if (selftext.isNotBlank()) {
                RedditMarkdown(selftext, modifier = Modifier.padding(horizontal = 16.dp))
            }
        } else {
            val parent = post.crosspostParentList.first()
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Gray), shape = ShapeDefaults.Medium)
                    .clickable(onClick = {
                        FullscreenManager.push {
                            ThreadView(
                                permalink = parent.permalink,
                                dismiss = {
                                    FullscreenManager.pop()
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
            enablePreview = true,
        )
    }
}

fun List<CommentType>.flattenComments(): List<CommentType> {
    val comments = emptyList<CommentType>().toMutableList()
    for (comment in this) {
        comments += comment
        if (comment is CommentType.Comment) {
            comments += comment.comment.replies.flattenComments()
        }
    }
    return comments
}