package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown
import com.sofamaniac.reboost.ui.post.View

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
        val comments by viewModel.comments.collectAsState()
        val post = viewModel.getPost()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            // Show post
            item {
                View(post, clickable = false) {
//                    val selftext = post.selftext.markdown
//                    if (selftext.isNotBlank()) {
//                        SimpleMarkdown(selftext)
//                    }
                    val selftext = post.selftext.markdown
                    if (selftext.isNotBlank()) {
                        SimpleMarkdown(selftext)
                        //Text(AnnotatedString.fromHtml(selftext))
                    }

                }
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
        }
    }
}