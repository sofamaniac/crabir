package com.sofamaniac.crabir.ui.postFeed.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.components.ListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    createPost: (Kind) -> Unit,
    cancel: () -> Unit,
) {

    val theme = LocalTheme.current
    ModalBottomSheet(
        containerColor = theme.cardBackground,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        ListItem(content = { Text(stringResource(R.string.create_post)) })
        for (type in postTypes) {
            ListItem(
                content = { Text(stringResource(type.name)) },
                leadingContent = { Icon(type.icon, contentDescription = null) },
                modifier = Modifier.clickable {
                    createPost(type.kind)
                }
            )
        }
        TextButton(onClick = cancel) {
            Text(stringResource(R.string.cancel))
        }
    }
}

data class PostType(@StringRes val name: Int, val kind: Kind, val icon: ImageVector)

val postTypes = listOf(
    PostType(R.string.create_kind_self, Kind.Self, Icons.AutoMirrored.Filled.Article),
    PostType(R.string.create_kind_link, Kind.Link, Icons.Default.Link),
    PostType(R.string.create_kind_image, Kind.Image, Icons.Default.Image),
    PostType(R.string.create_kind_video, Kind.Video, Icons.Default.VideoFile)
)
