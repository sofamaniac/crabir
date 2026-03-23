package com.sofamaniac.crabir.ui.postEditor

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.api.InvalidUrl
import com.sofamaniac.crabir.data.remote.api.MissingTitle
import com.sofamaniac.crabir.data.remote.api.MissingUrl
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.ui.markdown.Editor
import com.sofamaniac.crabir.ui.thread.CrossPostView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreator(
    community: SubredditData? = null,
    kind: Kind = Kind.Self,
    viewModel: PostCreatorViewModel = hiltViewModel()
) {
    LaunchedEffect(community, kind) {
        Log.d("PostCreator", "PostCreator: $community $kind")
        viewModel.community = community
        viewModel.state = viewModel.state.copy(kind = kind)
    }
    val fullscreenManager = LocalFullscreenHandler.current!!
    val context = LocalContext.current
    val theme = LocalTheme.current
    Box {
        Editor(
            modifier = Modifier.background(theme.cardBackground),
            state = viewModel.textState, topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { fullscreenManager.pop() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    title = { Text("Create post") },
                    actions = {
                        IconButton(onClick = {
                            viewModel.submit(context) { fullscreenManager.pop() }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    }
                )
            }) {
            CommunitySelector(viewModel)
            Column(
                modifier = Modifier.background(theme.cardBackground),
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
            if (viewModel.flairs.isNotEmpty()) {
                TextButton(onClick = {}) {
                    Text("Flair")
                }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosspostCreator(
    post: PostData,
    viewModel: CrosspostCreatorViewModel = hiltViewModel<CrosspostCreatorViewModel, CrosspostCreatorViewModel.Factory> { factory ->
        factory.create(post.name.name)
    }
) {
    val fullscreenManager = LocalFullscreenHandler.current!!
    val context = LocalContext.current
    val theme = LocalTheme.current
    Box {
        Scaffold(
            modifier = Modifier.background(theme.cardBackground),
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { fullscreenManager.pop() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    title = { Text("Create post") },
                    actions = {
                        IconButton(onClick = {
                            viewModel.submit(context) { fullscreenManager.pop() }
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
            ) {
                CommunitySelector(viewModel)
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
                CrossPostView(post)
            }
            if (viewModel.flairs.isNotEmpty()) {
                TextButton(onClick = {}) {
                    Text("Flair")
                }
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
    }
}


@Composable
fun UrlField(viewModel: PostCreatorViewModel) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPicker(viewModel: PostCreatorViewModel) {
    val maxMedia = when (viewModel.state.kind) {
        Kind.Image, Kind.Gallery -> 20
        Kind.Video -> 1
        else -> 0
    }
    val remaining = maxMedia - viewModel.media.size
    val request = when (viewModel.state.kind) {
        Kind.Image, Kind.Gallery -> PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly,
            maxItems = remaining
        )

        Kind.Video -> PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.VideoOnly,
        )

        else -> throw Exception("Invalid kind")
    }
    val picker = when (viewModel.state.kind) {
        Kind.Image, Kind.Gallery -> rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = remaining
            )
        ) { uris ->
            viewModel.media += uris
        }

        else -> rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.media += uri
            }
        }
    }
    val stroke =
        Stroke(width = 1.dp.value, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
    var editIndex by remember { mutableStateOf<Int?>(null) }

    LazyHorizontalGrid(
        rows = GridCells.FixedSize(100.dp),
        modifier = Modifier.heightIn(max = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(viewModel.media.size) { index ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .drawBehind {
                        drawRoundRect(color = Color.Gray, style = stroke)
                    }
                    .clickable {
                        editIndex = index
                    }
            ) {
                AsyncImage(
                    model = viewModel.media[index], contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        if (remaining > 0) {
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .drawBehind {
                            drawRoundRect(color = Color.Gray, style = stroke)
                        }
                        .clickable {
                            picker.launch(request)
                        }
                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add media",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
    if (editIndex != null) {
        val uri = viewModel.media[editIndex!!]
        Dialog(
            onDismissRequest = { editIndex = null }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    val caption =
                        rememberTextFieldState(initialText = viewModel.captions[uri] ?: "")
                    TextField(state = caption, label = { Text("Caption") })

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(onClick = {
                            viewModel.media = viewModel.media.toMutableList().apply {
                                removeAt(editIndex!!)
                            }
                            editIndex = null
                        }) {
                            Text("Remove", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { editIndex = null }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            viewModel.captions[uri] = caption.text as String
                            editIndex = null
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}