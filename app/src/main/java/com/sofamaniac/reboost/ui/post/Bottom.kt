/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.PostRoute
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PostData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@HiltViewModel(assistedFactory = ButtonViewModel.Factory::class)
class ButtonViewModel @AssistedInject constructor(
    @Assisted val post: PostData,
    private val reddit: RedditAPIService
) : ViewModel() {

    private val _likes = MutableStateFlow(post.relationship.liked)
    private val _saved = MutableStateFlow(post.relationship.saved)

    val likes: StateFlow<Boolean?> = _likes
    val saved: StateFlow<Boolean> = _saved


    fun upvote() {
        viewModelScope.launch {
            if (_likes.value == true) {
                reddit.vote(post.name, 0)
                _likes.value = null
            } else {
                reddit.vote(post.name, 1)
                _likes.value = true
            }
        }
    }

    fun downvote() {
        viewModelScope.launch {
            if (_likes.value == false) {
                reddit.vote(post.name, 0)
                _likes.value = null
            } else {
                reddit.vote(post.name, -1)
                _likes.value = false
            }
        }
    }

    fun save(target: Boolean) {
        viewModelScope.launch {
            if (target) {
                reddit.save(post.name)
            } else {
                reddit.unsave(post.name)
            }
            _saved.value = target
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(post: PostData): ButtonViewModel
    }

}

@Composable
private fun UpButton(post: PostData, likes: Boolean?, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (likes == true) Color.Red else Color.Gray,
        label = "button color"
    )
    IconButton(
        onClick = onClick
    ) {
        Icon(Icons.Filled.ThumbUp, "upvote", tint = buttonColor.value)
    }
}

@Composable
private fun DownButton(post: PostData, likes: Boolean?, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) Color.Blue else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ThumbDown, "downvote", tint = buttonColor.value)
    }
}

@Composable
private fun SavedButton(post: PostData, saved: Boolean, onClick: () -> Unit) {
    val buttonColor = animateColorAsState(
        targetValue = if (saved) Color.Yellow else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = onClick) {
        if (saved) {
            Icon(Icons.Filled.Bookmark, "save", tint = buttonColor.value)
        } else {
            Icon(Icons.Outlined.BookmarkBorder, "save", tint = buttonColor.value)
        }
    }
}

@Composable
fun BottomRow(
    post: PostData,
    modifier: Modifier = Modifier,
    viewModel: ButtonViewModel = hiltViewModel(
        key = post.id.id,
        creationCallback = { factory: ButtonViewModel.Factory ->
            factory.create(post)
        })
) {
    val navController = LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    val likes by viewModel.likes.collectAsState()
    val saved by viewModel.saved.collectAsState()
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(post, likes) { viewModel.upvote() }
        DownButton(post, likes) { viewModel.downvote() }
        SavedButton(post, saved) { viewModel.save(!saved) }
        IconButton(onClick = {
            navController.navigate(PostRoute(post.permalink))
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

@Composable
private fun PostOptions(post: PostData, modifier: Modifier = Modifier) {
    var showOptions by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showOptions = true }) {
            Icon(Icons.Default.MoreVert, "more")
        }
        DropdownMenu(expanded = showOptions, onDismissRequest = { showOptions = false }) {
            if (BuildConfig.DEBUG) {
                DropdownMenuItem(text = { Text("Post content") }, onClick = {
                    Log.d("Post", Json.encodeToString(post))
                })
            }
        }
    }
}
