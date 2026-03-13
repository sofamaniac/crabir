/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.UpButton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


@Composable
fun BottomRow(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: VotableViewModel,
    action: @Composable () -> Unit = {},
) {
    LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(viewModel)
        DownButton(viewModel)
        SavedButton(viewModel)
        action()
        IconButton(onClick = {
            uriHandler.openUri(post.url.toString())
        }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, "open in app")
        }
        PostOptions(post)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun PostOptions(post: PostData, modifier: Modifier = Modifier) {
    val prettyJson = Json { // this returns the JsonBuilder
        prettyPrint = true
        // optional: specify indent
        prettyPrintIndent = " "
    }
    var showOptions by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showOptions = true }) {
            Icon(Icons.Default.MoreVert, "more")
        }
        DropdownMenu(expanded = showOptions, onDismissRequest = { showOptions = false }) {
            if (BuildConfig.DEBUG) {
                DropdownMenuItem(text = { Text("Post content") }, onClick = {
                    Log.d("Post", prettyJson.encodeToString(post))
                })
            }
        }
    }
}
