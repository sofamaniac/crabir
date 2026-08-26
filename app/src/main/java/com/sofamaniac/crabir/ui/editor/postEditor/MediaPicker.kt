package com.sofamaniac.crabir.ui.editor.postEditor

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.ThemedDialog

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
        Stroke(
            width = 1.dp.value,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
        )
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
                        contentDescription = stringResource(R.string.add_media),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
    if (editIndex != null) {
        val uri = viewModel.media[editIndex!!]
        ThemedDialog(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            onDismissRequest = { editIndex = null }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val caption =
                    rememberTextFieldState(initialText = viewModel.captions[uri] ?: "")
                TextField(
                    state = caption,
                    label = { Text(stringResource(R.string.caption_placeholder)) })

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
                        Text(
                            stringResource(R.string.delete),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { editIndex = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        viewModel.captions[uri] = caption.text as String
                        editIndex = null
                    }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}