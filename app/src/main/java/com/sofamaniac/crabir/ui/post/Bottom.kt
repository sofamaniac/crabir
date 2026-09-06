/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.post.ButtonsSettings
import com.sofamaniac.crabir.ui.post.buttons.HideButton
import com.sofamaniac.crabir.ui.post.buttons.HideButtonLong
import com.sofamaniac.crabir.ui.post.buttons.MuteButton
import com.sofamaniac.crabir.ui.post.buttons.OpenInAppButton
import com.sofamaniac.crabir.ui.post.buttons.OpenInAppLong
import com.sofamaniac.crabir.ui.post.buttons.OpenThreadButton
import com.sofamaniac.crabir.ui.post.buttons.ShareButton
import com.sofamaniac.crabir.ui.post.buttons.ShareButtonLong
import com.sofamaniac.crabir.ui.post.dialog.EditDialogue
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.ReportMenu
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.UpButton
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@Composable
fun BottomRow(
    post: PostData,
    modifier: Modifier = Modifier,
    interactions: LinkInteraction,
    buttonsSettings: ButtonsSettings = LocalPostSettings.current.buttonsSettings,
) {
    val navController = LocalNavController.current
    BottomRow(post, modifier, interactions) {
        if (buttonsSettings.comments) {
            OpenThreadButton { navController?.navigate(PostRoute(post.permalink)) }
        }
    }
}

@Composable
fun BottomRow(
    post: PostData,
    modifier: Modifier = Modifier,
    interactions: LinkInteraction,
    buttonsSettings: ButtonsSettings = LocalPostSettings.current.buttonsSettings,
    action: (@Composable () -> Unit)? = null,
) {
    val likes by interactions.likes.collectAsState(post.relationship.liked)
    val saved by interactions.saved.collectAsState(post.relationship.saved)
    val upvoteOnSave =
        interactions.linksSettings.collectAsState(initial = null).value?.upvoteOnSave ?: false
    val navController = LocalNavController.current
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        UpButton(likes, onClick = { interactions.upvote(post.name) })
        DownButton(likes, onClick = { interactions.downvote(post.name) })
        SavedButton(saved, onClick = {
            interactions.save(post.name, !saved, upvoteOnSave)
        })
        if (buttonsSettings.comments && action == null) {
            OpenThreadButton { navController?.navigate(PostRoute(post.permalink)) }
        }
        action?.invoke()
        if (buttonsSettings.openInApp) {
            OpenInAppButton(post)
        }
        if (buttonsSettings.hide) {
            HideButton(post, interactions)
        }
        if (buttonsSettings.share) {
            ShareButton(post)
        }
        PostOptions(post, interactions, buttonsSettings)
    }
}

@OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PostOptions(
    post: PostData,
    interaction: LinkInteraction,
    buttonsSettings: ButtonsSettings,
    modifier: Modifier = Modifier,
) {
    val prettyJson = Json {
        prettyPrint = true
        prettyPrintIndent = " "
    }
    var showOptions by remember { mutableStateOf(false) }
    val navController = LocalNavController.current
    val currentAccount = LocalRedditAccount.current
    val theme = LocalTheme.current
    IconButton(modifier = modifier, onClick = { showOptions = true }) {
        Icon(Icons.Default.MoreVert, "more", tint = Color.Gray)
    }
    if (showOptions) {
        ModalBottomSheet(
            containerColor = theme.cardBackground,
            onDismissRequest = { showOptions = false },
        ) {
            if (post.canModPost) {
                ModerationButton(post)
            }
            if (post.author.authorFullname == currentAccount.info?.name) {
                EditButton(linkInteraction = interaction)
            }
            GoToSubredditButton(post) {
                showOptions = false
                navController?.navigate(SubredditRoute(post.subreddit.subredditPrefixed))
            }
            GoToUserButton(post) {
                showOptions = false
                navController?.navigate(
                    ProfileRoute(
                        username = post.author.username,
                        tab = ProfileTabs.Overview
                    )
                )
            }
            if (!buttonsSettings.hide) {
                HideButtonLong(post, interaction, onClick = { showOptions = false })
            }
            ReportButton(post, interaction)
            MuteButton(post, onDismissRequest = { showOptions = false })
            if (!buttonsSettings.share) {
                ShareButtonLong(post) { showOptions = false }
            }
            CopyButton(post)
            if (!buttonsSettings.openInApp) {
                OpenInAppLong(post, buttonsSettings)
            }
            if (BuildConfig.DEBUG) {
                DebugContentItem(post, prettyJson)
            }
        }
    }
}

@Composable
private fun ModerationButton(post: PostData) {
    ListItem(
        leadingContent = {
            Icon(
                Icons.Default.Shield,
                contentDescription = null
            )
        },
        content = { Text(stringResource(R.string.moderation)) },
    )
}

@Composable
private fun EditButton(linkInteraction: LinkInteraction) {
    var showDialog by remember { mutableStateOf(false) }
    ListItem(
        modifier = Modifier.clickable(onClick = { showDialog = true }),
        leadingContent = {
            Icon(
                Icons.Default.Edit,
                contentDescription = null
            )
        },
        content = { Text(stringResource(R.string.edit)) },
    )
    if (showDialog) {
        EditDialogue(linkInteraction) {
            showDialog = false
        }
    }
}

@Composable
private fun GoToSubredditButton(post: PostData, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            SubredditIcon(
                post.subreddit.name,
                icon = post.subredditDetails?.icon,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        content = {
            Text(
                stringResource(
                    R.string.go_to_subreddit,
                    post.subreddit.subredditPrefixed
                )
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun GoToUserButton(post: PostData, onClick: () -> Unit) {
    ListItem(
        leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
        content = {
            Text(
                stringResource(
                    R.string.go_to_profile,
                    post.author.username
                )
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun ReportButton(post: PostData, interaction: LinkInteraction) {
    var showMenu by remember { mutableStateOf(false) }
    ListItem(
        content = { Text(stringResource(R.string.report)) },
        modifier = Modifier.clickable {
            interaction.fetchRules()
            showMenu = true
        }
    )
    if (showMenu) {
        ReportMenu(post.name, interaction) {
            showMenu = false
        }
    }
}

@Composable
private fun CopyButton(post: PostData) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    ListItem(
        content = { Text(stringResource(R.string.copy)) },
        modifier = Modifier.clickable {
            scope.launch {
                val clipData = ClipData.newPlainText("Post URL", post.url)
                val clipEntry = ClipEntry(clipData)
                clipboard.setClipEntry(clipEntry)
            }
        }
    )
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun DebugContentItem(post: PostData, json: Json) {
    ListItem(
        content = { Text(stringResource(R.string.post_content)) },
        modifier = Modifier.clickable {
            Log.d("Post", json.encodeToString(post))
        }
    )
}
