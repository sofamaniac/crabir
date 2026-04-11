package com.sofamaniac.crabir.ui.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.InsertLink
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme

@Composable
fun Editor(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    topBar: @Composable () -> Unit = {},
    beforeEditor: @Composable () -> Unit = {},
) {
    val theme = LocalTheme.current
    Scaffold(
        topBar = topBar, modifier = modifier,
        bottomBar = { BottomBar(state) },
        snackbarHost = {
            if (snackbarHostState != null) {
                SnackbarHost(snackbarHostState)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(theme.cardBackground)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            beforeEditor()
            TextField(
                label = { Text("Type Comment") },
                state = state,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun BottomBar(state: TextFieldState) {

    fun TextFieldBuffer.insertModifier(before: String, after: String = before) {
        val start = selection.start
        val end = selection.end
        insert(end, after)
        insert(start, before)
        selection =
            TextRange(start + before.length, end + before.length)
    }

    BottomAppBar(modifier = Modifier.imePadding()) {
        IconButton(onClick = {
            state.edit {
                insertModifier("**")
            }
        }) {
            Icon(Icons.Default.FormatBold, contentDescription = null)
        }
        IconButton(onClick = {
            state.edit {
                insertModifier("*")
            }
        }) {
            Icon(Icons.Default.FormatItalic, contentDescription = null)
        }
        IconButton(onClick = {
            state.edit {
                insertModifier("[", "]()")
            }
        }) {
            Icon(Icons.Default.InsertLink, contentDescription = null)
        }
        IconButton(onClick = {
            state.edit {
                insertModifier("> ", after = "")
            }
        }) {
            Icon(Icons.Default.FormatQuote, contentDescription = null)
        }
        IconButton(onClick = {
            state.edit {
                insertModifier(">!", "!<")
            }
        }) {
            Icon(Icons.Default.Warning, contentDescription = null)
        }
        IconButton(onClick = {
            state.edit {
                insertModifier("```", "```")
            }
        }) {
            Icon(Icons.Default.Code, contentDescription = null)
        }
    }
}