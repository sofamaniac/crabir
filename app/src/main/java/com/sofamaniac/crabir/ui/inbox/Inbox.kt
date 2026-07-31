package com.sofamaniac.crabir.ui.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.model.MessageType
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.repository.InboxFeed
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.MessageEditorRoute
import com.sofamaniac.crabir.ui.RefreshIndicator
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.richtext.Richtext
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxView() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModels = buildMap {
        put(
            InboxFeed.All,
            koinViewModel<InboxViewModel>(key = InboxFeed.All.name) { parametersOf(InboxFeed.All) })
        put(
            InboxFeed.Unread,
            koinViewModel<InboxViewModel>(key = InboxFeed.Unread.name) { parametersOf(InboxFeed.Unread) })
        put(
            InboxFeed.Sent,
            koinViewModel<InboxViewModel>(key = InboxFeed.Sent.name) { parametersOf(InboxFeed.Sent) })
        put(
            InboxFeed.Mentions,
            koinViewModel<InboxViewModel>(key = InboxFeed.Mentions.name) { parametersOf(InboxFeed.Mentions) })
    }
    val tabs = listOf(InboxFeed.All, InboxFeed.Unread, InboxFeed.Sent, InboxFeed.Mentions)
    val theme = LocalTheme.current
    val navController = LocalNavController.current
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
                    actions = {
                        IconButton(onClick = { navController?.navigate(MessageEditorRoute(null)) }) {
                            Icon(Icons.AutoMirrored.Filled.Send, "Send Message")
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = theme.toolbarBackground,
                        scrolledContainerColor = theme.toolbarBackground,
                        titleContentColor = theme.toolbarText,
                    ),
                )
            }) { innerPadding ->
                val pagerState = rememberPagerState(0, pageCount = { tabs.size })
                Column(
                    verticalArrangement = Arrangement.Top, modifier = Modifier.padding(innerPadding)
                ) {
                    SecondaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(selected = index == pagerState.currentPage, onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            }, text = {
                                Text(
                                    tab.name,
                                )
                            })
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        val state = rememberPullToRefreshState()
                        val viewModel = viewModels[tabs[page]]
                        val messages = viewModel!!.data.collectAsLazyPagingItems()
                        PullToRefreshBox(
                            onRefresh = { viewModel.refresh() },
                            isRefreshing = !messages.loadState.isIdle,
                            state = state,
                            indicator = {
                                RefreshIndicator(!messages.loadState.isIdle, state)
                            }) {
                            LazyColumn {
                                items(
                                    messages.itemCount,
                                    key = messages.itemKey { it.name }) { index ->
                                    val message = messages[index]
                                    if (message != null) {
                                        Column {
                                            Message(message)
                                            HorizontalDivider()
                                        }
                                    }
                                }
                                item {
                                    if (messages.loadState.hasError) {
                                        val error = messages.loadState.refresh as LoadState.Error
                                        Text(error.error.message ?: "Unknown error")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageHeader(message: Message) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Mail, contentDescription = null)
        Text(message.subject)
    }
}

@Composable
fun CommentHeader(message: Message) {
    val icon = if (message.type == MessageType.PostReply) {
        Icons.AutoMirrored.Filled.Message
    } else {
        Icons.AutoMirrored.Filled.Reply
    }
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null)
        Text(message.subject)
    }
}


@Composable
fun Message(
    message: Message,
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = koinViewModel(key = message.name.name) {
        parametersOf(message.name, message)
    },
) {
    val message by viewModel.message.collectAsState()
    val richtext by viewModel.richtext.collectAsState()
    val theme = LocalTheme.current
    val header = @Composable {
        when (message.type) {
            MessageType.Message -> MessageHeader(message)
            else -> CommentHeader(message)
        }
    }
    val navController = LocalNavController.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row( //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                header()
                Spacer(modifier = Modifier.weight(1f))
            }
            if (message.new) {
                IconButton(onClick = { viewModel.markRead() }) {
                    Icon(Icons.Default.MarkEmailRead, contentDescription = "Mark as read")
                }
                //            } else {
                //                IconButton(onClick = { viewModel.markUnread() }) {
                //                    Icon(Icons.Default.MarkEmailUnread, contentDescription = "Mark as unread")
                //                }
            }
            IconButton(onClick = { navController?.navigate(MessageEditorRoute(message.name)) }) {
                Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = "Reply")
            }
            //            IconButton(onClick = {}) {
            //                Icon(Icons.Default.MoreVert, contentDescription = null)
            //            }
        }
        val textStyle = MaterialTheme.typography.titleSmall
        val annotatedString = buildAnnotatedString {
            withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                append(message.author)
            }
            if (message.subreddit != null) {
                append(" via ")
                withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                    append(message.subreddit)
                }
            }
            append(" · ")
            append(formatElapsedTimeLocalized(message.createdUtc))
        }
        Text(annotatedString)
        Spacer(modifier = Modifier.height(8.dp))
        Richtext(
            richtext, mediaMetadata = emptyMap(), modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
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
        Row( //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
            body, mediaMetadata = emptyMap(), modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

