package com.sofamaniac.crabir.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Editor(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    label: @Composable TextFieldLabelScope.() -> Unit,
    topBar: @Composable () -> Unit = {},
    beforeEditor: @Composable ColumnScope.() -> Unit = {},
) {
    val theme = LocalTheme.current
    Scaffold(
        topBar = topBar,
        modifier = modifier
            .navigationBarsPadding()
            .imePadding()
            .imeNestedScroll(),
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
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            beforeEditor()
            TextField(
                label = label,
                state = state,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BottomBar(state: TextFieldState) {

    FlexibleBottomAppBar(modifier = Modifier.imePadding()) {
        EditorActions(state)
    }
}

@Composable
fun RowScope.EditorActions(state: TextFieldState) {
    fun TextFieldBuffer.insertModifier(before: String, after: String = before) {
        val start = selection.start
        val end = selection.end
        insert(end, after)
        insert(start, before)
        selection =
            TextRange(start + before.length, end + before.length)
    }
    IconButton(onClick = {
        state.edit {
            insertModifier("**")
        }
    }) {
        Icon(Icons.Default.FormatBold, contentDescription = "Insert bold")
    }
    IconButton(onClick = {
        state.edit {
            insertModifier("*")
        }
    }) {
        Icon(Icons.Default.FormatItalic, contentDescription = "Insert italic")
    }
    IconButton(onClick = {
        state.edit {
            insertModifier("[", "]()")
        }
    }) {
        Icon(Icons.Default.InsertLink, contentDescription = "Insert link")
    }
    IconButton(onClick = {
        state.edit {
            insertModifier("> ", after = "")
        }
    }) {
        Icon(Icons.Default.FormatQuote, contentDescription = "Insert quote")
    }
    IconButton(onClick = {
        state.edit {
            insertModifier(">!", "!<")
        }
    }) {
        Icon(Icons.Default.Warning, contentDescription = "Insert spoiler")
    }
    IconButton(onClick = {
        state.edit {
            insertModifier("```", "```")
        }
    }) {
        Icon(Icons.Default.Code, contentDescription = "Insert code block")
    }
}