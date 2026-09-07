package com.sofamaniac.crabir.ui.editor.crosspostEditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.MissingTitle
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.CloseButton
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.editor.AccountSelector
import com.sofamaniac.crabir.ui.editor.postEditor.CommunitySelector
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.subredditList.Tile
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosspostCreator(
    post: Fullname,
    viewModel: CrosspostCreatorViewModel = koinViewModel { parametersOf(post) },
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    val post = viewModel.post.collectAsState(initial = null).value ?: return
    LaunchedEffect(post) {
        viewModel.titleState.edit {
            delete(0, length)
            insert(0, post.title)
        }
        viewModel.state = viewModel.state.copy(
            title = post.title,
            nsfw = post.over18,
            spoiler = post.spoiler,
        )
    }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    Box {
        Scaffold(
            modifier = Modifier.background(theme.cardBackground),
            snackbarHost = { SnackbarHost(snackbar) },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        CloseButton { navController?.popBackStack() }
                    },
                    title = { Text(stringResource(R.string.create_post)) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                val res = viewModel.submit(account = null)
                                if (res.isSuccess) {
                                    navController?.popBackStack()
                                } else {
                                    snackbar.showSnackbar(res.exceptionOrNull()!!.message!!)
                                }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    }
                )
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(color = theme.cardBackground)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CommunitySelector(viewModel) { onDismiss ->
                    CrosspostCommunitySearch(viewModel = viewModel, onDismiss = onDismiss)
                }
                TextField(
                    state = viewModel.titleState,
                    label = { Text(stringResource(R.string.title_field)) },
                    inputTransformation = InputTransformation.maxLength(300),
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.error is MissingTitle,
                    supportingText = {
                        if (viewModel.error is MissingTitle)
                            Text(
                                stringResource(R.string.missing_title_error),
                                color = MaterialTheme.colorScheme.error
                            )
                    },
                    trailingIcon = {
                        if (viewModel.error is MissingTitle)
                            Icon(
                                Icons.Filled.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                    }
                )
                Text(
                    "${viewModel.titleState.text.length}/300",
                    modifier = Modifier.align(Alignment.End)
                )
                if (viewModel.flairs.isNotEmpty()) {
                    TextButton(onClick = {}) {
                        Text(stringResource(R.string.flair_field))
                    }
                }
                AccountSelector(accounts, selectedAccount) { newId ->
                    selectedAccount = accounts.find { it.id == newId }!!
                    viewModel.resetSubreddit()
                    viewModel.setAccount(selectedAccount)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        onClick = {
                            viewModel.state = viewModel.state.copy(nsfw = !viewModel.state.nsfw)
                        },
                        selected = viewModel.state.nsfw,
                        colors = FilterChipDefaults.filterChipColors().copy(
                            selectedContainerColor = Color.Red,
                        ),
                        label = {
                            Text("NSFW")
                        },
                    )
                    FilterChip(
                        onClick = {
                            viewModel.state =
                                viewModel.state.copy(spoiler = !viewModel.state.spoiler)
                        },
                        selected = viewModel.state.spoiler,
                        label = {
                            Text("SPOILER")
                        }
                    )
                }
                SwitchTile(
                    leadingContent = {},
                    headlineContent = { Text(stringResource(R.string.send_reply_notification)) },
                    checked = viewModel.state.sendReplies,
                    onCheckedChange = {
                        viewModel.state = viewModel.state.copy(sendReplies = it)
                    }
                )
                CrosspostView(post)
            }
        }
        if (viewModel.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
internal fun CrosspostView(
    post: PostData,
    modifier: Modifier = Modifier,
) {
    val modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val theme = LocalTheme.current

    ThemedCard(
        roundedCorners = false,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = theme.secondaryText)
                .padding(8.dp)
        ) {
            PostHeader(post, showSubredditIcon = false)
            PostInfo(
                post,
                modifier = modifier,
                enableThumbnail = true,
                likes = post.relationship.liked
            )
        }
    }
}

@Composable
fun CrosspostCommunitySearch(
    viewModel: CrosspostCreatorViewModel,
    onDismiss: () -> Unit,
) {
    val communities = viewModel.data.collectAsLazyPagingItems()
    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.select_community)) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.close)
                            )
                        }
                    },
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (communities.itemCount > 0) {
                    items(
                        count = communities.itemCount,
                        key = communities.itemKey { p -> p.name }) { index ->
                        val subreddit = communities[index]!!
                        Tile(subreddit, modifier = Modifier.clickable {
                            viewModel.setSubreddit(subreddit)
                            onDismiss()
                        })
                    }
                } else {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}

