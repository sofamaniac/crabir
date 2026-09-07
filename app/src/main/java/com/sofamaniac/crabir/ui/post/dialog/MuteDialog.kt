package com.sofamaniac.crabir.ui.post.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ThemedDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuteDialog(post: PostData, onDismissRequest: () -> Unit, onClick: () -> Unit) {
    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.filtersDataStore }
    val scope = rememberCoroutineScope()
    val username = post.author.username
    val subreddit = post.subreddit.name
    val flair = post.linkFlair.text
    val domain = post.url.toUri().host
    ThemedDialog(onDismissRequest = onDismissRequest) {
        ListItem(
            content = { Text(stringResource(R.string.mute_posts_from_user, username)) },
            leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.clickable {
                scope.launch {
                    settingsDataStore.updateData { it.addAuthor(username) }
                    onClick()
                }
            }
        )
        ListItem(
            content = {
                Text(
                    stringResource(
                        R.string.mute_posts_from_community,
                        subreddit
                    )
                )
            },
            leadingContent = { Icon(Icons.Default.Groups, contentDescription = null) },
            modifier = Modifier.clickable {
                scope.launch {
                    settingsDataStore.updateData { it.addSubreddit(subreddit) }
                    onClick()
                }
            }
        )
        domain?.isNotBlank()?.let {
            ListItem(
                content = {
                    Text(
                        stringResource(
                            R.string.mute_posts_from_domain,
                            domain
                        )
                    )
                },
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                modifier = Modifier.clickable {
                    scope.launch {
                        settingsDataStore.updateData { it.copy(domainFilters = it.domainFilters + domain) }
                        onClick()
                    }
                }
            )
        }
        if (flair.isNotBlank()) {
            ListItem(
                content = { Text(stringResource(R.string.mute_flair)) },
                leadingContent = { Spacer(modifier = Modifier.size(16.dp)) },
                modifier = Modifier.clickable {
                    scope.launch {
                        settingsDataStore.updateData { it.copy(flairFilters = it.flairFilters + flair) }
                        onClick()
                    }
                }
            )
        }
    }
}
