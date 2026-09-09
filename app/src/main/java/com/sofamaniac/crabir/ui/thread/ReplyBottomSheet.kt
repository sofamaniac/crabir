package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.ui.editor.AccountSelector
import com.sofamaniac.crabir.ui.editor.EditorActions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReplyBottomSheet(viewModel: ThreadViewModel) {
    val parentName by viewModel.reply.collectAsState()
    val post by viewModel.post.collectAsState(null)
    val comments by viewModel.comments.collectAsState()
    val parent = if (parentName == post?.name) post else comments.find { it.name == parentName }
    if (parent == null) return
    val textFieldState = rememberTextFieldState()
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    val theme = LocalTheme.current
    ModalBottomSheet(
        containerColor = theme.cardBackground,
        modifier = Modifier
            .imePadding()
            .imeNestedScroll()
            .fillMaxWidth(),
        onDismissRequest = { viewModel.replyTo(null) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { viewModel.replyTo(null) }) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    viewModel.submitComment(
                        parent.name,
                        textFieldState.text.toString(),
                        account = selectedAccount
                    )
                }) {
                    Text(stringResource(R.string.submit))
                }
            }
            AccountSelector(accounts, selectedAccount) { newId ->
                selectedAccount = accounts.find { it.id == newId }!!
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                state = textFieldState,
                label = {
                    Text(
                        stringResource(
                            R.string.reply_to,
                            parent.author?.username ?: "user"
                        )
                    )
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EditorActions(textFieldState)
            }
        }
    }
}
