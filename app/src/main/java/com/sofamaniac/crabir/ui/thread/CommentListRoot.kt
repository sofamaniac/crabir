package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.PostBody
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.VotableViewModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val theme = LocalTheme.current

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        val comments by viewModel.comments.map {
            it.flattenComments()
        }
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
                    is CommentType.Comment -> CommentNode(
                        comment.comment,
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = theme.cardBackground)
                            .depthIndent(comment.depth, color = Color.Gray)
                    )

                    is CommentType.More -> MoreViewer(
                        comment,
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = theme.cardBackground)
                            .depthIndent(comment.depth, color = Color.Gray)
                    )
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
    PostCard(post) {
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

@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.id)
        }
    ),
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
        BottomRow(post, modifier, visitPost = {}, viewModel = viewModel)
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