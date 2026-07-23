package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.MessageDTO
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageData
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.domain.repository.InboxRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxView(viewModel: InboxViewModel = koinViewModel()) {
    val messages = viewModel.data.collectAsLazyPagingItems()
    Scaffold(topBar = { TopAppBar(title = { Text("Inbox") }) }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(messages.itemCount, key = messages.itemKey { it.name }) { index ->
                val message = messages[index]
                Column {
                    when (message) {
                        is MessageData.Message -> {
                            Message(message.message, modifier = Modifier.padding(8.dp))
                        }

                        is MessageData.Comment -> {
                            Message(message, modifier = Modifier.padding(8.dp))
                        }

                        else -> {}
                    }

                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Message(message: MessageDTO, modifier: Modifier = Modifier) {
    val header = @Composable {
        Row {
            Icon(Icons.Default.Mail, contentDescription = null)
            Text(message.subject)
        }
    }
    Message(
        header = header,
        author = message.author ?: "",
        body = message.body,
        created = message.createdUtc,
        modifier = modifier
    )
}

@Composable
fun Message(message: MessageData.Comment, modifier: Modifier = Modifier) {
    val comment = message.comment
    val icon = if (comment.type == "post_reply") {
        Icons.AutoMirrored.Filled.Message
    } else {
        Icons.AutoMirrored.Filled.Reply
    }
    val header = @Composable {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null)
            Text(comment.subject ?: "")
        }
    }
    Message(
        header = header,
        author = comment.author,
        subreddit = comment.subreddit,
        body = comment.body,
        created = comment.created_utc,
        modifier = modifier
    )
}

@Composable
fun Message(
    header: @Composable () -> Unit,
    author: String,
    created: Instant,
    body: String,
    modifier: Modifier = Modifier,
    subreddit: String? = null,
) {
    val theme = LocalTheme.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            header()
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
        val textStyle = MaterialTheme.typography.titleSmall
        val annotatedString = buildAnnotatedString {
            withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                append(author)
            }
            if (subreddit != null) {
                append(" via ")
                withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                    append(subreddit)
                }
            }
            append(" · ")
            append(formatElapsedTimeLocalized(created))
        }
        Text(annotatedString)
        Spacer(modifier = Modifier.height(8.dp))
        RedditMarkdown(
            ParsedMarkdown(body),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@KoinViewModel
class InboxViewModel(repo: InboxRepository) : ViewModel() {
    val feedSource = FeedSource(repo, Unit)
    val data: Flow<PagingData<MessageData>> = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = Fullname(""),
        pagingSourceFactory = {
            feedSource
        }
    )
        .flow.cachedIn(
            viewModelScope
        )
}