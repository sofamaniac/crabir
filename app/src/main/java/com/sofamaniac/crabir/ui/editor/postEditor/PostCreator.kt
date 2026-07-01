package com.sofamaniac.crabir.ui.editor.postEditor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.InvalidUrl
import com.sofamaniac.crabir.data.remote.reddit.MissingTitle
import com.sofamaniac.crabir.data.remote.reddit.MissingUrl
import com.sofamaniac.crabir.data.remote.reddit.PostSubmissionBuilder
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.editor.Editor
import com.sofamaniac.crabir.ui.mapColor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    val theme = LocalTheme.current
    var showFlairDialog by remember { mutableStateOf(false) }
    var showFlairEdit by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Editor(
        modifier = Modifier.background(theme.cardBackground),
        state = viewModel.textState,
        snackbarHostState = snackbarHostState,
        label = { Text("Post content") },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                title = { Text("Create post") },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            val res = viewModel.submit(context)
                            if (res.isSuccess) {
                                onDismissRequest()
                            } else {
                                snackbarHostState.showSnackbar(res.exceptionOrNull()!!.message!!)
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            )
        })
    {
        CommunitySelector(viewModel)
        Column(
        ) {
            TextField(
                state = viewModel.titleState,
                label = { Text("Title") },
                inputTransformation = InputTransformation.maxLength(300),
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.error is MissingTitle,
                supportingText = {
                    if (viewModel.error is MissingTitle)
                        Text("Missing title", color = MaterialTheme.colorScheme.error)
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
        if (viewModel.state.flairId != null) {
            val flair = viewModel.flairs.find { it.id == viewModel.state.flairId }
            val text = viewModel.state.flairText ?: flair?.text ?: ""
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text)
                if (flair?.textEditable == true) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { showFlairEdit = true }) {
                        Text("Edit flair")
                    }
                }
            }
        }
        TextButton(onClick = { showFlairDialog = true }) {
            Text("Change Flair")
        }
        when (viewModel.state.kind) {
            Kind.Link -> {
                UrlField(viewModel)
            }

            Kind.Image, Kind.Video -> {
                MediaPicker(viewModel)
            }

            else -> {}
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
                    viewModel.state = viewModel.state.copy(spoiler = !viewModel.state.spoiler)
                },
                selected = viewModel.state.spoiler,
                label = {
                    Text("SPOILER")
                }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlairDialog(
    flairs: List<FlairInfo>,
    flairId: String? = null,
    flairText: String? = null,
    onSelect: (FlairInfo) -> Unit,
    onClickEdit: (FlairInfo) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        ThemedCard() {
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
                    headlineContent = {
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
                Text("Community has no flair")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlairEditBox(
    initialText: String? = null,
    flair: FlairInfo,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialText = if (initialText.isNullOrBlank()) flair.text else initialText
    val textFieldState =
        rememberTextFieldState(initialText = initialText)
    BasicAlertDialog(onDismissRequest = onDismiss) {
        ThemedCard() {
            ListItem(headlineContent = {
                Text(
                    "Edit flair text",
                    style = MaterialTheme.typography.titleMedium
                )
            })
            ListItem(
                headlineContent = {
                    TextField(
                        state = textFieldState
                    )
                }
            )
            Row() {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    onConfirm(textFieldState.text as String)
                    onDismiss()
                }) {
                    Text("Confirm")
                }
            }
        }
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
                Text("Invalid URL", color = MaterialTheme.colorScheme.error)
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