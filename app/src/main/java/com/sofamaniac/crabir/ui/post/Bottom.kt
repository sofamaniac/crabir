/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import android.content.ClipData
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
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
    viewModel: VotableViewModel,
    action: @Composable () -> Unit = {},
) {
    LocalNavController.current!!
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(viewModel)
        DownButton(viewModel)
        SavedButton(viewModel)
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
    val description = "Open in app"
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    ) {
        IconButton(onClick = {
            uriHandler.openUri(post.url.toString())
        }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, description, tint = Color.Gray)
        }
    }
}

@Composable
fun OpenThreadButton(post: PostData, onClick: (PostData) -> Unit) {
    val description = "Open comments"
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
    viewModel: VotableViewModel,
    modifier: Modifier = Modifier
) {
    val prettyJson = Json {
        prettyPrint = true
        prettyPrintIndent = " "
    }
    var showOptions by remember { mutableStateOf(false) }
    val navController = LocalNavController.current!!
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showOptions = true }) {
            Icon(Icons.Default.MoreVert, "more", tint = Color.Gray)
        }
        DropdownMenu(expanded = showOptions, onDismissRequest = { showOptions = false }) {
            if (post.canModPost) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null
                        )
                    },
                    text = { Text("Moderation") },
                    onClick = {},
                )
            }
            DropdownMenuItem(
                leadingIcon = {
                    SubredditIcon(
                        post.subreddit.name,
                        icon = post.subredditDetails?.icon
                    )
                },
                text = { Text("Go to ${post.subreddit.name}") }, onClick = {
                    navController.navigate(SubredditRoute(post.subreddit.name))
                }
            )
            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                text = { Text("Go to ${post.author.username} profile") },
                onClick = {
                    navController.navigate(
                        ProfileRoute(
                            author = post.author.username,
                            tab = ProfileTabs.Overview
                        )
                    )
                }
            )
            DropdownMenuItem(text = { Text("Hide / Unhide post") }, onClick = {
                if (post.relationship.hidden) {
                    viewModel.unhide()
                } else {
                    viewModel.hide()
                }
            })
            DropdownMenuItem(text = { Text("Report") }, onClick = {
                viewModel.getRules()
                showReportDialog = true
            })
            DropdownMenuItem(text = { Text("Mute") }, onClick = {})
            DropdownMenuItem(text = { Text("Share") }, onClick = {
                showShareDialog = true
            })
            DropdownMenuItem(text = { Text("Copy") }, onClick = {
                scope.launch {
                    val clipData = ClipData.newPlainText("Post URL", post.url.toString())
                    val clipEntry = ClipEntry(clipData)
                    clipboard.setClipEntry(clipEntry)
                }
            })
            if (BuildConfig.DEBUG) {
                DropdownMenuItem(text = { Text("Post content") }, onClick = {
                    Log.d("Post", prettyJson.encodeToString(post))
                })
            }
        }
    }
    if (showShareDialog) {
        ShareMenu(post) {
            showShareDialog = false
        }
    }
    if (showReportDialog) {
        ReportMenu(viewModel) {
            showReportDialog = false
        }
    }
}

@Composable
fun ShareMenu(post: PostData, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val permalink = "https://reddit.com${post.permalink}"
    Dialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            ListItem(
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                headlineContent = { Text("Share link") },
                supportingContent = { Text(post.url) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, post.url.toString())
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, post.title)
                    }
                    context.startActivity(intent)
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
                supportingContent = { Text(permalink) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, permalink)
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, post.title)
                    }
                    context.startActivity(intent)
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
                headlineContent = { Text("Crosspost") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                headlineContent = { Text("Share shortlink") },
                supportingContent = { Text(post.shortlink) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, post.shortlink)
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, post.title)
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ReportMenu(viewModel: VotableViewModel, onDismissRequest: () -> Unit) {
    val rules = viewModel.rules
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(rules.siteRules.firstOrNull()) }
    Dialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            LazyColumn(modifier = Modifier.selectableGroup()) {
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