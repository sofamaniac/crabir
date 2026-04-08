package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.ui.markdown.Editor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyButton(
    parentId: Fullname,
    threadViewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    parentPreview: @Composable () -> Unit = {}
) {
    var showEditor by remember { mutableStateOf(false) }

    IconButton(onClick = {
        showEditor = true
    }) {
        Icon(Icons.AutoMirrored.Default.Reply, contentDescription = "Reply")
    }
    if (showEditor) {

        val state = rememberTextFieldState()
        Editor(
            state = state,
            topBar = {
                TopAppBar(
                    title = { Text("Comment") },
                    navigationIcon = {
                        IconButton(onClick = { showEditor = false }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            threadViewModel.postComment(parentId, state.text as String)
                            showEditor = false
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Reply"
                            )
                        }
                    }
                )
            }) {
            parentPreview()
        }
    }
}