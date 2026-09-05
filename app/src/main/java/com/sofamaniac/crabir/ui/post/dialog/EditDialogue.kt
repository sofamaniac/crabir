package com.sofamaniac.crabir.ui.post.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.ui.ThemedDialog
import com.sofamaniac.crabir.ui.editor.postEditor.FlairDialog
import com.sofamaniac.crabir.ui.editor.postEditor.FlairEditBox
import com.sofamaniac.crabir.ui.post.LinkInteraction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialogue(viewModel: LinkInteraction, onDismissRequest: () -> Unit) {
    val postOpt by viewModel.post.collectAsState(null)

    if (postOpt == null) return

    val post = postOpt!!

    var showFlairDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    ThemedDialog(onDismissRequest) {
        ListItem(
            onClick = { showFlairDialog = true },
            content = { Text(stringResource(R.string.change_flair)) },
            trailingContent = {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        )
        // TODO
        //        if (post.kind == Kind.Self) {
        //            ListItem(content = { Text(stringResource(R.string.edit_text)) })
        //        }
        ListItem(
            content = { Text(stringResource(R.string.nsfw)) },
            trailingContent = {
                Switch(
                    checked = post.over18,
                    onCheckedChange = {
                        if (it) {
                            viewModel.markNSFW()
                        } else {
                            viewModel.unmarkNSFW()
                        }
                    }
                )

            }
        )
        ListItem(
            content = { Text(stringResource(R.string.spoiler)) },
            trailingContent = {
                Switch(
                    checked = post.spoiler,
                    onCheckedChange = {
                        if (it) {
                            viewModel.markSpoiler()
                        } else {
                            viewModel.unmarkSpoiler()
                        }
                    }
                )

            }
        )
        ListItem(
            content = { Text(stringResource(R.string.inbox_replies)) },
            trailingContent = {
                Switch(
                    checked = post.sendReplies,
                    onCheckedChange = {
                        viewModel.setInboxReplies(it)
                    }
                )
            })
        ListItem(
            modifier = Modifier.clickable { showDeleteConfirmDialog = true },
            content = { Text(stringResource(R.string.delete)) })
    }

    var flair by remember { mutableStateOf<FlairInfo?>(null) }
    var showFlairEditDialog by remember { mutableStateOf(false) }
    if (showFlairDialog) {
        LaunchedEffect(Unit) {
            viewModel.getFlairs()
        }
        val flairs by viewModel.flairs.collectAsState()
        FlairDialog(
            flairs = flairs,
            flairId = flair?.id,
            flairText = flair?.text,
            onSelect = {
                flair = it
            },
            onClickEdit = {
                flair = it
                showFlairEditDialog = true
            },
            onDismiss = { showFlairDialog = false }
        )
    }
    if (showFlairEditDialog) {
        FlairEditBox(
            initialText = flair!!.text, flair = flair!!, onConfirm = {
                viewModel.editFlair(flair!!.id, it)
                showFlairEditDialog = false
                showFlairDialog = false
            },
            onDismiss = { showFlairEditDialog = false }
        )
    }
    if (showDeleteConfirmDialog) {
        ThemedDialog(
            confirm = {
                TextButton(onClick = {
                    viewModel.delete()
                    onDismissRequest()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            cancel = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            onDismissRequest = { showDeleteConfirmDialog = false }) {
            Text(stringResource(R.string.delete_post_confirmation))
        }

    }
}
