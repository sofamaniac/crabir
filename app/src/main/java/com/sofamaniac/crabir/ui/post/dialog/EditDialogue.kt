package com.sofamaniac.crabir.ui.post.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.post.LinkInteraction
import com.sofamaniac.crabir.ui.postEditor.FlairDialog
import com.sofamaniac.crabir.ui.postEditor.FlairEditBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialogue(viewModel: LinkInteraction, onDismissRequest: () -> Unit) {
    val postOpt by viewModel.post.collectAsState(null)

    if (postOpt == null) return

    val post = postOpt!!

    var showFlairDialog by remember { mutableStateOf(false) }

    BasicAlertDialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            ListItem(
                modifier = Modifier.clickable { showFlairDialog = true },
                headlineContent = { Text(stringResource(R.string.change_flair)) },
                trailingContent = {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            )
            if (post.kind == Kind.Self) {
                ListItem(headlineContent = { Text(stringResource(R.string.edit_text)) })
            }
            ListItem(
                headlineContent = { Text(stringResource(R.string.nsfw)) },
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
                headlineContent = { Text(stringResource(R.string.spoiler)) },
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
                headlineContent = { Text(stringResource(R.string.inbox_replies)) },
                trailingContent = {
                    Switch(
                        checked = post.sendReplies,
                        onCheckedChange = {
                            viewModel.setInboxReplies(it)
                        }
                    )
                })
            ListItem(headlineContent = { Text(stringResource(R.string.delete)) })
        }
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
}