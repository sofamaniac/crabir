package com.sofamaniac.crabir.ui.thread.dialog

import android.content.ClipData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.ui.ThemedDialog
import kotlinx.coroutines.launch

@Composable
fun CopyDialog(comment: CommentData, onDismissRequest: () -> Unit) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var showSelectionDialog by remember { mutableStateOf(false) }
    ThemedDialog(onDismissRequest) {
        ListItem(
            modifier = Modifier.clickable {
                val permalink = "https://reddit.com${comment.permalink}".toUri()
                scope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newRawUri(
                                "comment's permalink",
                                permalink
                            )
                        )
                    )
                }
            },
            leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
            content = { Text("Copy link") }
        )
        ListItem(
            modifier = Modifier.clickable {
                scope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newPlainText(
                                "comment's text",
                                comment.body.markdown
                            )
                        )
                    )
                }
            },
            leadingContent = {
                Icon(
                    Icons.AutoMirrored.Default.Comment,
                    contentDescription = null
                )
            },
            content = { Text("Copy text") }
        )
        ListItem(
            modifier = Modifier.clickable {
                showSelectionDialog = true
            },
            leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
            content = { Text("Select text") }
        )
        ListItem(
            modifier = Modifier.clickable {
                scope.launch {
                    clipboard.setClipEntry(
                        ClipEntry(
                            ClipData.newPlainText(
                                "author's username",
                                comment.author.username
                            )
                        )
                    )
                }
            },
            leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
            content = { Text("Copy username") }
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
                Text(comment.body.markdown)
            }
        }
    }
}