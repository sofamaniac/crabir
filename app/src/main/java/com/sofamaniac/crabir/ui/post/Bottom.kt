/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import android.content.ClipData
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.api.FlairInfo
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.rememberCurrentAccount
import com.sofamaniac.crabir.navigation.CrosspostCreatorRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.postEditor.FlairDialog
import com.sofamaniac.crabir.ui.postEditor.FlairEditBox
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
    viewModel: LinkViewModel,
    action: @Composable () -> Unit = {},
) {
    LocalNavController.current!!
    val likes by viewModel.likes.collectAsState(post.relationship.liked)
    val saved by viewModel.saved.collectAsState(post.relationship.saved)
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(likes, onClick = { viewModel.upvote(post.name) })
        DownButton(likes, onClick = { viewModel.downvote(post.name) })
        SavedButton(saved, onClick = { viewModel.save(post.name, !saved) })
        action()
        OpenInAppButton(post)
        PostOptions(post, viewModel)
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
fun OpenThreadButton(post: PostData, onClick: (PostData) -> Unit) {
    val description = stringResource(R.string.open_comments)
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    ) {
        IconButton(onClick = { onClick(post) }) {
            Icon(
                Icons.AutoMirrored.Filled.Comment,
                contentDescription = description,
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun PostOptions(
    post: PostData,
    viewModel: LinkViewModel,
    modifier: Modifier = Modifier
) {
    val prettyJson = Json {
        prettyPrint = true
        prettyPrintIndent = " "
    }
    var showOptions by remember { mutableStateOf(false) }
    val navController = LocalNavController.current!!
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val currentAccount = rememberCurrentAccount()
    var showShareDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    IconButton(onClick = { showOptions = true }) {
        Icon(Icons.Default.MoreVert, "more", tint = Color.Gray)
    }
    if (showOptions) {
        Dialog(onDismissRequest = { showOptions = false }) {
            Card {
                if (post.canModPost) {
                    ListItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text("Moderation") },
                    )
                }
                if (post.author.authorFullname == currentAccount.info?.name?.name) {
                    ListItem(
                        modifier = Modifier.clickable {
                            Log.d("PostOptions", "Edit clicked")
                            showEditDialog = true
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text("Edit") },
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
                    headlineContent = { Text("Go to ${post.subreddit.name}") },
                    modifier = Modifier.clickable {
                        navController.navigate(SubredditRoute(post.subreddit.name))
                    }
                )
                ListItem(
                    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                    headlineContent = { Text("Go to ${post.author.username} profile") },
                    modifier = Modifier.clickable {
                        navController.navigate(
                            ProfileRoute(
                                author = post.author.username,
                                tab = ProfileTabs.Overview
                            )
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text("Hide / Unhide post") },
                    modifier = Modifier.clickable {
                        if (post.relationship.hidden) {
                            viewModel.unhide()
                        } else {
                            viewModel.hide()
                        }
                    })
                ListItem(headlineContent = { Text("Report") }, modifier = Modifier.clickable {
                    viewModel.getRules()
                    showReportDialog = true
                })
                ListItem(headlineContent = { Text("Mute") }, modifier = Modifier.clickable {})
                ListItem(headlineContent = { Text("Share") }, modifier = Modifier.clickable {
                    showShareDialog = true
                })
                ListItem(headlineContent = { Text("Copy") }, modifier = Modifier.clickable {
                    scope.launch {
                        val clipData = ClipData.newPlainText("Post URL", post.url.toString())
                        val clipEntry = ClipEntry(clipData)
                        clipboard.setClipEntry(clipEntry)
                    }
                })
                if (BuildConfig.DEBUG) {
                    ListItem(
                        headlineContent = { Text("Post content") },
                        modifier = Modifier.clickable {
                            Log.d("Post", prettyJson.encodeToString(post))
                        })
                }
            }
        }
    }
    if (showShareDialog) {
        ShareMenu(post) {
            showShareDialog = false
            showOptions = false
        }
    }
    if (showReportDialog) {
        ReportMenu(viewModel) {
            showReportDialog = false
            showOptions = false
        }
    }
    if (showEditDialog) {
        EditDialogue(viewModel) {
            showEditDialog = false
            showOptions = false
        }
    }
}

@Composable
fun ShareMenu(post: PostData, onDismissRequest: () -> Unit) {
    val permalink = "https://reddit.com${post.permalink}"
    val shareLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onDismissRequest()
        }
    //val fullscreenManager = LocalFullscreenHandler.current!!
    val navController = LocalNavController.current!!
    Dialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            ListItem(
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                headlineContent = { Text("Share link") },
                supportingContent = {
                    Text(
                        post.url,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, post.url.toString())
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Default.Comment,
                        contentDescription = null
                    )
                },
                headlineContent = { Text("Share post") },
                supportingContent = {
                    Text(
                        permalink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, permalink)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
            val titleLink = "${post.title} $permalink"
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.TextFields,
                        contentDescription = null
                    )
                },
                headlineContent = { Text("Share title + post") },
                supportingContent = {
                    Text(
                        titleLink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, titleLink)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
            HorizontalDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.CallSplit,
                        modifier = Modifier.rotate(90f),
                        contentDescription = null,
                    )
                },
                headlineContent = { Text("Crosspost") },
                modifier = Modifier.clickable {
                    onDismissRequest()
                    navController.navigate(CrosspostCreatorRoute(post.name))
//                    fullscreenManager.push {
//                        CrosspostCreator(post)
//                    }
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                headlineContent = { Text("Share shortlink") },
                supportingContent = {
                    Text(
                        post.shortlink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, post.shortlink)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : VotableData> ReportMenu(viewModel: VotableViewModel<T>, onDismissRequest: () -> Unit) {
    val rules = viewModel.rules
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(rules.siteRules.firstOrNull()) }
    BasicAlertDialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxHeight(0.8f)
            ) {
                items(rules.rules.size) { index ->
                    ListItem(
                        modifier = Modifier.selectable(
                            selected = selectedOption == rules.rules[index].violationReason,
                            onClick = { setSelectedOption(rules.rules[index].violationReason) },
                            role = Role.RadioButton
                        ),
                        leadingContent = {
                            RadioButton(
                                selected = selectedOption == rules.rules[index].violationReason,
                                onClick = null
                            )
                        },
                        headlineContent = { Text(rules.rules[index].shortName) }
                    )
                }
                items(rules.siteRules.size) { index ->
                    ListItem(
                        modifier = Modifier.selectable(
                            selected = selectedOption == rules.siteRules[index],
                            onClick = { setSelectedOption(rules.siteRules[index]) },
                            role = Role.RadioButton
                        ),
                        leadingContent = {
                            RadioButton(
                                selected = selectedOption == rules.siteRules[index],
                                onClick = null
                            )
                        },
                        headlineContent = { Text(rules.siteRules[index]) }
                    )
                }
            }

            Row {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    if (selectedOption == null) return@TextButton
                    viewModel.report(selectedOption)
                    onDismissRequest()
                }) {
                    Text("Report")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialogue(viewModel: LinkViewModel, onDismissRequest: () -> Unit) {
    val postOpt by viewModel.post.collectAsState()

    if (postOpt == null) return

    val post = postOpt!!

    var showFlairDialog by remember { mutableStateOf(false) }

    BasicAlertDialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            ListItem(
                modifier = Modifier.clickable { showFlairDialog = true },
                headlineContent = { Text("Change Flair") },
                trailingContent = {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            )
            if (post.kind == Kind.Self) {
                ListItem(headlineContent = { Text("Edit text") })
            }
            ListItem(
                headlineContent = { Text("NSFW") },
                trailingContent = {
                    Switch(
                        checked = post.over18,
                        onCheckedChange = {
                            if (it) {
                                viewModel.markNSFW()
                            } else {
                                viewModel.unmarkNSFW()
                            }
                        }
                    )

                }
            )
            ListItem(
                headlineContent = { Text("Spoiler") },
                trailingContent = {
                    Switch(
                        checked = post.spoiler,
                        onCheckedChange = {
                            if (it) {
                                viewModel.markSpoiler()
                            } else {
                                viewModel.unmarkSpoiler()
                            }
                        }
                    )

                }
            )
            ListItem(
                headlineContent = { Text("Inbox replies") },
                trailingContent = {
                    Switch(
                        checked = post.sendReplies,
                        onCheckedChange = {
                            viewModel.setInboxReplies(it)
                        }
                    )
                })
            ListItem(headlineContent = { Text("Delete") })
        }
    }

    var flair by remember { mutableStateOf<FlairInfo?>(null) }
    var showFlairEditDialog by remember { mutableStateOf(false) }
    if (showFlairDialog) {
        LaunchedEffect(Unit) {
            viewModel.getFlairs()
        }
        val flairs by viewModel.flairs
        FlairDialog(
            flairs = flairs,
            flairId = flair?.id,
            flairText = flair?.text,
            onSelect = {
                flair = it
            },
            onClickEdit = {
                flair = it
                showFlairEditDialog = true
            },
            onDismiss = { showFlairDialog = false }
        )
    }
    if (showFlairEditDialog) {
        FlairEditBox(
            initialText = flair!!.text, flair = flair!!, onConfirm = {
                viewModel.editFlair(flair!!.id, it)
                showFlairEditDialog = false
                showFlairDialog = false
            },
            onDismiss = { showFlairEditDialog = false }
        )
    }
}