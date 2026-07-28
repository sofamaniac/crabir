package com.sofamaniac.crabir.ui.inbox

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
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.MessageDTO
import com.sofamaniac.crabir.data.remote.utils.fromHtml
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageData
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.repository.InboxFeed
import com.sofamaniac.crabir.domain.repository.InboxRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.richtext.Richtext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxView(viewModel: InboxViewModel = koinViewModel()) {
    val messages = viewModel.data.collectAsLazyPagingItems()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState)
            },
        ) {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("Inbox") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                        }
                    }
                )
            }) { innerPadding ->
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(messages.itemCount, key = messages.itemKey { it.name }) { index ->
                        val message = messages[index]
                        Column {
                            when (message) {
                                is MessageData.Message -> {
                                    Message(
                                        message.message,
                                        viewModel = viewModel,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                is MessageData.Comment -> {
                                    Message(
                                        message,
                                        viewModel = viewModel,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                else -> {}
                            }

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Message(message: MessageDTO, modifier: Modifier = Modifier, viewModel: InboxViewModel) {
    val header = @Composable {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Mail, contentDescription = null)
            Text(message.subject)
        }
    }
    val richtext = RichtextDocument.fromHtml(message.bodyHtml)
    Message(
        header = header,
        author = message.author ?: "",
        body = richtext,
        created = message.createdUtc,
        modifier = modifier,
        viewModel = viewModel,
        new = message.new,
        name = message.name,
        subreddit = message.subreddit
    )
}

@Composable
fun Message(
    message: MessageData.Comment,
    modifier: Modifier = Modifier,
    viewModel: InboxViewModel,
) {
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
    val richtext = RichtextDocument.fromHtml(comment.bodyHtml)
    Message(
        header = header,
        author = comment.author,
        subreddit = comment.subreddit,
        body = richtext,
        created = comment.created_utc,
        modifier = modifier,
        viewModel = viewModel,
        new = comment.new,
        name = comment.name,
    )
}

@Composable
fun Message(
    header: @Composable () -> Unit,
    author: String,
    created: Instant,
    body: RichtextDocument,
    new: Boolean,
    name: Fullname,
    viewModel: InboxViewModel,
    modifier: Modifier = Modifier,
    subreddit: String? = null,
) {
    val theme = LocalTheme.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                header()
                Spacer(modifier = Modifier.weight(1f))
            }
            if (new) {
                IconButton(onClick = { viewModel.read(name) }) {
                    Icon(Icons.Default.MarkEmailRead, contentDescription = "Mark as read")
                }
            } else {
                IconButton(onClick = { viewModel.unread(name) }) {
                    Icon(Icons.Default.MarkEmailUnread, contentDescription = "Mark as unread")
                }
            }
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
        Richtext(
            body,
            mediaMetadata = emptyMap(),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@KoinViewModel
class InboxViewModel(private val repo: InboxRepository) : ViewModel() {
    val feedSource = FeedSource(repo, InboxFeed.Inbox)
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

    fun read(name: Fullname) {
        viewModelScope.launch {
            repo.read(name)
        }
    }

    fun unread(name: Fullname) {
        viewModelScope.launch {
            repo.unread(name)
        }
    }

    fun readAll() {
        viewModelScope.launch {
            repo.readAll()
        }
    }
}