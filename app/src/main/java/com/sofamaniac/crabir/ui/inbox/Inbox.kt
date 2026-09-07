package com.sofamaniac.crabir.ui.inbox

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.model.MessageType
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.repository.InboxFeed
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.MessageEditorRoute
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.theme.rememberTopAppBarColors
import com.sofamaniac.crabir.ui.ListItem
import com.sofamaniac.crabir.ui.RefreshIndicator
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.ThemedDialog
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
                    colors = rememberTopAppBarColors(),
                    title = { Text(stringResource(R.string.inbox)) },
                    actions = {
                        IconButton(onClick = { navController?.navigate(MessageEditorRoute(null)) }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                stringResource(R.string.send_message)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = stringResource(R.string.open_drawer)
                            )
                        }
                    },
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
    ThemedCard(modifier = modifier.fillMaxWidth()) {
        Row( //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                header()
                Spacer(modifier = Modifier.weight(1f))
            }
            if (message.new) {
                IconButton(onClick = { viewModel.markRead(message.name) }) {
                    Icon(
                        Icons.Default.MarkEmailRead,
                        contentDescription = stringResource(R.string.mark_as_read)
                    )
                }
                //            } else {
                //                IconButton(onClick = { viewModel.markUnread() }) {
                //                    Icon(Icons.Default.MarkEmailUnread, contentDescription = "Mark as unread")
                //                }
            }
            IconButton(onClick = { navController?.navigate(MessageEditorRoute(message.name)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.Reply,
                    contentDescription = stringResource(R.string.reply)
                )
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
        val textStyle = MaterialTheme.typography.titleSmall
        val annotatedString = buildAnnotatedString {
            withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                append(message.author)
            }
            if (message.parent?.subreddit != null) {
                append(" via ")
                withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                    append(message.parent?.subreddit)
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
                IconButton(onClick = { viewModel.markRead(name) }) {
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

@Composable
fun MessageDropdownMenu(message: Message, viewModel: MessageViewModel) {
    val navController = LocalNavController.current
    var expanded by remember { mutableStateOf(false) }
    var showCopyDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More actions")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null)
                },
                text = { Text(stringResource(R.string.reply)) },
                onClick = { navController?.navigate(MessageEditorRoute(message.name)) }
            )
            if (message.new) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Default.MarkEmailRead, contentDescription = null)
                    },
                    text = { Text(stringResource(R.string.mark_as_read)) },
                    onClick = { viewModel.markRead(message.name) }
                )
            }

            if (message.author != null) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    text = { Text(stringResource(R.string.about_user, message.author)) },
                    onClick = {
                        navController?.navigate(ProfileRoute(message.author))
                    }
                )
            }
            if (message.author != null) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Default.Block,
                            contentDescription = null
                        )
                    },
                    text = { Text(stringResource(R.string.block_user, message.author)) },
                    onClick = {
                        viewModel.blockAuthor(message.author)
                    }
                )
            }
            if (message.parent?.subredditPrefixed != null) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            stringResource(
                                R.string.go_to_parametrized,
                                message.parent.subredditPrefixed
                            )
                        )
                    }, onClick = {
                        navController?.navigate(SubredditRoute(message.parent.subredditPrefixed))
                    }
                )
            }
            if (message.parent?.title != null) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                    },
                    text = { Text(stringResource(R.string.go_to_parent_post)) }, onClick = {
                        navController?.navigate(PostRoute(message.parent.title))
                    }
                )
            }

            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                text = { Text(stringResource(R.string.copy)) },
                onClick = {
                    showCopyDialog = true
                }
            )
        }
    }
    if (showCopyDialog) CopyDialog(message, onDismissRequest = { showCopyDialog = false })
}

@Composable
fun CopyDialog(message: Message, onDismissRequest: () -> Unit) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var showSelectionDialog by remember { mutableStateOf(false) }
    ThemedDialog(onDismissRequest) {
        ListItem(
            onClick = {
                scope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newPlainText(
                                "message's text",
                                message.body
                            )
                        )
                    )
                }
            },
            leadingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.Message,
                    contentDescription = null
                )
            },
            content = { Text(stringResource(R.string.copy_text)) }
        )
        ListItem(
            onClick = {
                showSelectionDialog = true
            },
            leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
            content = { Text(stringResource(R.string.select_text)) }
        )
        ListItem(
            onClick = {
                scope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newPlainText(
                                "author's username",
                                message.author
                            )
                        )
                    )
                }
            },
            leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
            content = { Text(stringResource(R.string.copy_username)) }
        )
    }

    if (showSelectionDialog) {
        ThemedDialog(
            onDismissRequest = { showSelectionDialog = false },
            confirm = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(stringResource(R.string.done))
                }
            }) {
            SelectionContainer(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                Text(message.body)
            }
        }
    }
}

