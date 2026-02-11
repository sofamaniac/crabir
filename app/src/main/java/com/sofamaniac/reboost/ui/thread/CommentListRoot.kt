package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown
import com.sofamaniac.reboost.ui.post.PostCard
import com.sofamaniac.reboost.ui.post.PostGallery
import com.sofamaniac.reboost.ui.post.PostImage
import com.sofamaniac.reboost.ui.post.PostVideo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var fullscreenView: (@Composable () -> Unit)? by remember { mutableStateOf(null) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        val comments by viewModel.comments.collectAsState()
        val post = viewModel.getPost()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            // Show post
            item {
                PostView(
                    post,
                    goFullscreen = { view -> fullscreenView = view },
                    dismiss = { fullscreenView = null })
            }
            items(comments.size) { index ->
                when (val comment = comments[index]) {
                    is Thing.Comment -> CommentView(comment)
                    is Thing.More -> MoreViewer(comment)
                    else -> {
                        Log.e(
                            "CommentListRoot",
                            "Unknown comment type: ${comment.javaClass.name}"
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.1f))
            }
        }
    }
    fullscreenView?.invoke()
}

@Composable
internal fun PostView(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = true,
    goFullscreen: (@Composable () -> Unit) -> Unit,
    dismiss: () -> Unit
) {
    PostCard(post, clickable = false) {
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
                    goFullscreen,
                    dismiss,
                    canPlayVideo = canPlayVideo
                )
            }

            else -> {}
        }
        // always show selftext if there is one
        val selftext = post.selftext.markdown
        if (selftext.isNotBlank()) {
            SimpleMarkdown(selftext)
        }
    }
}