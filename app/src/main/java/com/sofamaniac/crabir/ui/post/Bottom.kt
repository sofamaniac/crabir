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
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.post.dialog.EditDialogue
import com.sofamaniac.crabir.ui.post.dialog.MuteDialog
import com.sofamaniac.crabir.ui.post.dialog.PostDialog
import com.sofamaniac.crabir.ui.post.dialog.ReportMenu
import com.sofamaniac.crabir.ui.post.dialog.ShareMenu
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.DownButton
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
    action: @Composable () -> Unit = {},
) {
    val likes by interactions.likes.collectAsState(post.relationship.liked)
    val saved by interactions.saved.collectAsState(post.relationship.saved)
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(likes, onClick = { interactions.upvote(post.name) })
        DownButton(likes, onClick = { interactions.downvote(post.name) })
        SavedButton(saved, onClick = { interactions.save(post.name, !saved) })
        action()
        OpenInAppButton(post)
        PostOptions(post, interactions)
    }
}

@Composable
fun OpenInAppButton(
    post: PostData,
) {
    val uriHandler = LocalUriHandler.current
    val description = stringResource(R.string.open_in_app)
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
    ) {
        IconButton(onClick = {
            uriHandler.openUri(post.url)
        }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, description, tint = Color.Gray)
        }
    }
}

@Composable
fun OpenThreadButton(onClick: () -> Unit) {
    val description = stringResource(R.string.open_comments)
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
        ),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                Icons.AutoMirrored.Filled.Comment,
                contentDescription = description,
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PostOptions(
    post: PostData,
    interaction: LinkInteraction,
    modifier: Modifier = Modifier
) {
    val prettyJson = Json {
        prettyPrint = true
        prettyPrintIndent = " "
    }
    var showOptions by remember { mutableStateOf(false) }
    val navController = LocalNavController.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val currentAccount = LocalRedditAccount.current
    var currentDialog: PostDialog? by remember { mutableStateOf(null) }
    IconButton(onClick = { showOptions = true }) {
        Icon(Icons.Default.MoreVert, "more", tint = Color.Gray)
    }
    if (showOptions) {
        ModalBottomSheet(onDismissRequest = { showOptions = false }) {
            if (post.canModPost) {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.moderation)) },
                )
            }
            if (post.author.authorFullname == currentAccount.info?.name?.name) {
                ListItem(
                    modifier = Modifier.clickable {
                        currentDialog = PostDialog.Edit
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.edit)) },
                )
            }
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
                headlineContent = {
                    Text(
                        stringResource(
                            R.string.go_to_subreddit,
                            post.subreddit.name
                        )
                    )
                },
                modifier = Modifier.clickable {
                    navController?.navigate(SubredditRoute(post.subreddit.name))
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                headlineContent = {
                    Text(
                        stringResource(
                            R.string.go_to_profile,
                            post.author.username
                        )
                    )
                },
                modifier = Modifier.clickable {
                    navController?.navigate(
                        ProfileRoute(
                            author = post.author.username,
                            tab = ProfileTabs.Overview
                        )
                    )
                }
            )
            ListItem(
                headlineContent = {
                    val text =
                        if (post.relationship.hidden) R.string.unhide_post else R.string.hide_post
                    Text(stringResource(text))
                },
                modifier = Modifier.clickable {
                    if (post.relationship.hidden) {
                        interaction.unhide()
                    } else {
                        interaction.hide()
                    }
                })
            ListItem(
                headlineContent = { Text(stringResource(R.string.report)) },
                modifier = Modifier.clickable {
                    interaction.fetchRules()
                    currentDialog = PostDialog.Report
                })
            ListItem(
                headlineContent = { Text(stringResource(R.string.mute)) },
                modifier = Modifier.clickable {
                    currentDialog = PostDialog.Report
                })
            ListItem(
                headlineContent = { Text(stringResource(R.string.share)) },
                modifier = Modifier.clickable {
                    currentDialog = PostDialog.Share
                })
            ListItem(
                headlineContent = { Text(stringResource(R.string.copy)) },
                modifier = Modifier.clickable {
                    scope.launch {
                        val clipData = ClipData.newPlainText("Post URL", post.url)
                        val clipEntry = ClipEntry(clipData)
                        clipboard.setClipEntry(clipEntry)
                    }
                })
            if (BuildConfig.DEBUG) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.post_content)) },
                    modifier = Modifier.clickable {
                        Log.d("Post", prettyJson.encodeToString(post))
                    })
            }
        }
    }
    when (currentDialog) {
        PostDialog.Mute -> {
            MuteDialog(post) {
                currentDialog = null
            }
        }

        PostDialog.Report -> {
            ReportMenu(interaction) {
                currentDialog = null
            }
        }

        PostDialog.Share -> {
            ShareMenu(post) {
                currentDialog = null
            }
        }

        PostDialog.Edit -> {
            EditDialogue(interaction) {
                currentDialog = null
            }
        }

        else -> {}
    }
}