package com.sofamaniac.crabir.ui.editor.postEditor

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.InvalidUrl
import com.sofamaniac.crabir.data.remote.reddit.MissingTitle
import com.sofamaniac.crabir.data.remote.reddit.MissingUrl
import com.sofamaniac.crabir.data.remote.reddit.PostSubmissionBuilder
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.CloseButton
import com.sofamaniac.crabir.ui.ThemedDialog
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.editor.AccountSelector
import com.sofamaniac.crabir.ui.editor.EditorBottomBar
import com.sofamaniac.crabir.ui.mapColor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostCreator(
    communitySlug: String? = null,
    kind: Kind = Kind.Self,
    viewModel: PostCreatorViewModel = koinViewModel(),
    onDismissRequest: () -> Unit,
) {
    LaunchedEffect(communitySlug, kind) {
        Log.d("PostCreator", "PostCreator: $communitySlug $kind")
        viewModel.state = PostSubmissionBuilder()
        if (communitySlug != null) {
            viewModel.setSubreddit(communitySlug)
        }
        viewModel.state = viewModel.state.copy(kind = kind)
    }
    val context = LocalContext.current
    var showFlairDialog by remember { mutableStateOf(false) }
    var showFlairEdit by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    fun submit() {
        scope.launch {
            val res = viewModel.submit(context, account = selectedAccount)
            if (res.isSuccess) {
                onDismissRequest()
            } else {
                snackbarHostState.showSnackbar(res.exceptionOrNull()!!.message!!)
            }
        }
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            EditorTopBar(onDismissRequest, ::submit)
        },
        bottomBar = {
            EditorBottomBar(viewModel.textState)
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CommunitySelector(viewModel) { onDismiss ->
                    CommunitySearch(viewModel, onDismiss = onDismiss)
                }
            }
            item {
                Column {
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
                }
            }
            if (viewModel.state.flairId != null) {
                item {
                    val flair = viewModel.flairs.find { it.id == viewModel.state.flairId }
                    val text = viewModel.state.flairText ?: flair?.text ?: ""
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text)
                        if (flair?.textEditable == true) {
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = { showFlairEdit = true }) {
                                Text(stringResource(R.string.edit_flair))
                            }
                        }
                    }
                }
            }
            item {
                TextButton(onClick = { showFlairDialog = true }) {
                    Text(stringResource(R.string.change_flair))
                }
            }
            item {
                AccountSelector(accounts, selectedAccount) { newId ->
                    selectedAccount = accounts.find { it.id == newId }!!
                }
            }
            item {
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
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.send_reply_notification)) },
                    checked = viewModel.state.sendReplies,
                    onCheckedChange = {
                        viewModel.state = viewModel.state.copy(sendReplies = it)
                    }
                )
            }
            item {
                when (viewModel.state.kind) {
                    Kind.Link -> {
                        UrlField(viewModel)
                    }

                    Kind.Image, Kind.Video -> {
                        MediaPicker(viewModel)
                    }

                    Kind.Self -> {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp),
                            state = viewModel.textState,
                            placeholder = { Text(stringResource(R.string.body_placeholder)) },
                            label = { Text(stringResource(R.string.body_label)) }
                        )
                    }

                    else -> {}
                }
            }
        }

        if (viewModel.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        if (showFlairDialog) {
            LaunchedEffect(viewModel) {
                viewModel.getFlairs()
            }
            FlairDialog(
                viewModel.flairs,
                onSelect = { flair ->
                    viewModel.state = viewModel.state.copy(flairId = flair.id)
                },
                onClickEdit = { flair ->
                    viewModel.state = viewModel.state.copy(flairId = flair.id)
                    showFlairEdit = true
                },
                onDismiss = { showFlairDialog = false }
            )
        }
        if (showFlairEdit) {
            val initialText = viewModel.state.flairText
            FlairEditBox(
                initialText,
                viewModel.flairs.find { it.id == viewModel.state.flairId }!!,
                onConfirm = { flairText ->
                    viewModel.state = viewModel.state.copy(flairText = flairText)
                },
                onDismiss = { showFlairEdit = false }
            )
        }
    }
}

@Composable
private fun EditorTopBar(
    onDismissRequest: () -> Unit,
    submit: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            CloseButton { onDismissRequest() }
        },
        title = { Text(stringResource(R.string.create_post)) },
        actions = {
            IconButton(onClick = {
                submit()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.submit)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlairDialog(
    flairs: List<FlairInfo>,
    flairId: String? = null,
    flairText: String? = null,
    onSelect: (FlairInfo) -> Unit,
    onClickEdit: (FlairInfo) -> Unit,
    onDismiss: () -> Unit,
) {
    ThemedDialog(onDismissRequest = onDismiss) {
        for (flair in flairs) {
            val text = if (flair.id == flairId && flairText != null) {
                flairText
            } else {
                flair.text
            }
            ListItem(
                modifier = Modifier.clickable {
                    onSelect(flair)
                    onDismiss()
                },
                content = {
                    Text(
                        text,
                        color = mapColor(flair.textColor ?: "", Color.Unspecified),
                        modifier = Modifier.cartouche(
                            mapColor(flair.backgroundColor)
                        )
                    )
                },
                trailingContent = {
                    if (flair.textEditable) {
                        IconButton(onClick = {
                            onClickEdit(flair)
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    }
                }
            )
        }
        if (flairs.isEmpty()) {
            Text(stringResource(R.string.community_has_no_flair))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlairEditBox(
    initialText: String? = null,
    flair: FlairInfo,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialText = if (initialText.isNullOrBlank()) flair.text else initialText
    val textFieldState =
        rememberTextFieldState(initialText = initialText)
    ThemedDialog(
        onDismissRequest = onDismiss,
        cancel = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirm = {
            TextButton(onClick = {
                onConfirm(textFieldState.text as String)
                onDismiss()
            }) {
                Text(stringResource(R.string.confirm))
            }
        }) {
        ListItem(content = {
            Text(
                stringResource(R.string.edit_flair_text),
                style = MaterialTheme.typography.titleMedium
            )
        })
        ListItem(
            content = {
                TextField(
                    state = textFieldState
                )
            }
        )
    }
}

@Composable
internal fun UrlField(viewModel: PostCreatorViewModel) {
    val isError = viewModel.error is InvalidUrl || viewModel.error is MissingUrl
    TextField(
        lineLimits = TextFieldLineLimits.SingleLine,
        state = viewModel.urlState,
        label = {
            Text("URL")
        },
        leadingIcon = {
            Icon(Icons.Default.Link, contentDescription = null)
        },
        supportingText = {
            if (isError) {
                Text(
                    stringResource(R.string.invalid_url_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isError) {
                Icon(
                    Icons.Default.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
    )
}