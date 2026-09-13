package com.sofamaniac.crabir.ui.drafts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.Draft
import com.sofamaniac.crabir.data.remote.dto.DraftBody
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostCreatorRoute
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.components.RefreshIndicator
import com.sofamaniac.crabir.ui.components.SubredditIcon
import com.sofamaniac.crabir.ui.components.ThemedDialog
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.richtext.Richtext
import org.koin.androidx.compose.koinViewModel

@Composable
fun DraftsView(viewModel: DraftsViewModel = koinViewModel()) {
    val drafts by viewModel.drafts.collectAsState()
    val subreddits by viewModel.subreddits.collectAsState()
    val navController = LocalNavController.current
    val refreshing by viewModel.refreshing.collectAsState()
    val refreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.drafts_page_title)) },
                navigationIcon = { BackButton { navController?.popBackStack() } }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            state = refreshState,
            isRefreshing = refreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize(),
            indicator = {
                RefreshIndicator(refreshing, refreshState)
            }
        ) {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(drafts.size) {
                    Draft(drafts[it], subreddits, delete = viewModel::delete)
                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
fun Draft(draft: Draft, subreddits: Map<Fullname, SubredditData>, delete: (Draft) -> Unit) {
    val navController = LocalNavController.current
    val subreddit = subreddits[draft.subreddit]
    var showDeleteDialog by remember { mutableStateOf(false) }
    ListItem(
        onClick = {
            val kind = when (draft.kind) {
                "link" -> Kind.Link
                else -> Kind.Self
            }
            navController?.navigate(
                PostCreatorRoute(
                    kind = kind,
                    communityNamePrefixed = subreddit?.displayNamePrefixed,
                    draftId = draft.id
                )
            )
        },
        verticalAlignment = Alignment.CenterVertically,
        leadingContent = {
            val icon = when (draft.kind) {
                "link" -> Icons.Default.Link
                else -> Icons.AutoMirrored.Outlined.Article
            }
            Icon(icon, contentDescription = null)
        },
        trailingContent = {
            IconButton(onClick = {
                showDeleteDialog = true
            }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingContent = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (subreddit != null) {
                        SubredditIcon(
                            subreddit = subreddit.id,
                            subreddit.icon,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(subreddit.displayName)
                    }
                    Text(formatElapsedTimeLocalized(draft.created))
                }
                if (draft.body is DraftBody.Richtext) {
                    Richtext(draft.body.richtext, mediaMetadata = emptyMap())
                } else if (draft.kind != "link" && draft.body is DraftBody.Text) {
                    Text(draft.body.text)
                }
            }
        }) {
        Text(draft.title)
    }

    if (showDeleteDialog) {
        ThemedDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    stringResource(R.string.delete),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            cancel = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirm = {
                TextButton(onClick = {
                    delete(draft)
                    showDeleteDialog = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            }
        ) {
            Text(stringResource(R.string.delete_draft_content))
        }
    }
}
