package com.sofamaniac.crabir.ui.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun EditorBottomBar(state: TextFieldState) {

    FlexibleBottomAppBar(modifier = Modifier.imePadding()) {
        EditorActions(state)
    }
}

@Composable
fun EditorActions(state: TextFieldState) {
    fun TextFieldBuffer.insertModifier(before: String, after: String = before) {
        val start = selection.start
        val end = selection.end
        insert(end, after)
        insert(start, before)
        selection =
            TextRange(start + before.length, end + before.length)
    }
    Row {
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
}