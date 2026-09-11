package com.sofamaniac.crabir.ui.thread.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.Kind
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.TextEditorRoute
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.thread.CommentViewModel
import com.sofamaniac.crabir.ui.votable.ReportMenu

@Composable
fun MoreOptionButton(comment: CommentData, viewModel: CommentViewModel) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(Icons.Default.MoreVert, contentDescription = null)
    }

    if (showMenu) {
        MoreOptionMenu(comment, viewModel) {
            showMenu = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionMenu(
    comment: CommentData,
    viewModel: CommentViewModel,
    onDismissRequest: () -> Unit,
) {
    val theme = LocalTheme.current
    var showReportMenu by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }
    var showShareMenu by remember { mutableStateOf(false) }
    var showCopyMenu by remember { mutableStateOf(false) }
    val currentAccount = LocalRedditAccount.current
    val navController = LocalNavController.current
    ModalBottomSheet(
        containerColor = theme.cardBackground,
        onDismissRequest = onDismissRequest
    ) {

        if (comment.author.authorFullname == currentAccount.info?.name) {
            ListItem(
                onClick = {
                    navController?.navigate(TextEditorRoute(comment.name, comment.body.markdown))
                },
                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                content = { Text(stringResource(R.string.edit)) }
            )
        }
        ListItem(
            onClick = { showUserMenu = true },
            leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
            content = { Text("About ${comment.author.username}") },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = null
                )
            }
        )

        ListItem(
            onClick = { viewModel.collapse(true) },
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = null
                )
            },
            content = { Text(stringResource(R.string.collapse_thread)) },
        )

        ListItem(
            onClick = {
                viewModel.fetchRules()
                showReportMenu = true
            },
            leadingContent = { Icon(Icons.Default.Report, contentDescription = null) },
            content = { Text(stringResource(R.string.report)) },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = null
                )
            }
        )

        ListItem(
            onClick = {
                showShareMenu = true
            },
            leadingContent = { Icon(Icons.Default.Share, contentDescription = null) },
            content = { Text(stringResource(R.string.share)) },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = null
                )
            }
        )

        ListItem(
            onClick = {
                showCopyMenu = true
            },
            leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
            content = { Text(stringResource(R.string.copy)) },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = null
                )
            }
        )
    }

    if (showReportMenu) {
        ReportMenu(comment.name, viewModel, kind = Kind.Comment) {
            showReportMenu = false
        }
    }

    if (showUserMenu) {
        UserMenu(comment.author.username) {
            showUserMenu = false
        }
    }

    if (showShareMenu) {
        ShareMenu(comment) {
            showShareMenu = false
        }
    }

    if (showCopyMenu) {
        CopyDialog(comment) {
            showCopyMenu = false
        }
    }
}

