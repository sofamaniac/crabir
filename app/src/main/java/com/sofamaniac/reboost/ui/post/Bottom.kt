/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.votable.DownButton
import com.sofamaniac.reboost.ui.votable.SavedButton
import com.sofamaniac.reboost.ui.votable.UpButton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


@Composable
fun BottomRow(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: VotableViewModel,
    visitPost: (PostData) -> Unit = {},
) {
    val navController = LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    val likes by viewModel.likes.collectAsState(initial = post.relationship.liked)
    val saved by viewModel.saved.collectAsState(initial = post.relationship.saved)
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(likes) { viewModel.upvote() }
        DownButton(likes) { viewModel.downvote() }
        SavedButton(saved) { viewModel.save(!saved) }
        IconButton(onClick = {
            visitPost(post)
        }) {
            Icon(Icons.AutoMirrored.Outlined.Chat, "comments")
        }
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
