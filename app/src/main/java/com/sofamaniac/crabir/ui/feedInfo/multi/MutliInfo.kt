package com.sofamaniac.crabir.ui.feedInfo.multi

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubredditInfoRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.MultiIcon
import com.sofamaniac.crabir.ui.components.SubredditIcon
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MultiInfo(
    slug: String,
    viewModel: MultiInfoViewModel = koinViewModel { parametersOf(slug) },
) {
    val infoOpt by viewModel.info.collectAsState()

    val theme = LocalTheme.current
    val navController = LocalNavController.current

    Scaffold(
        containerColor = theme.cardBackground,
        topBar = { TopBar(infoOpt?.displayName) }
    ) { padding ->
        if (infoOpt == null) return@Scaffold

        val info = infoOpt!!
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ListItem(leadingContent = {
                    MultiIcon(info)
                }, supportingContent = {
                    val members = LocalResources.current.getQuantityString(
                        R.plurals.multi_subreddits,
                        info.subreddits.size,
                        info.subreddits.size
                    )
                    Text(members, style = MaterialTheme.typography.labelSmall)
                }, content = {
                    Text(info.displayNamePrefixed, style = MaterialTheme.typography.titleMedium)
                })
            }
            items(info.subreddits.size, key = { info.subreddits[it].name }) { index ->
                val subreddit = info.subreddits[index]
                ListItem(
                    onClick = { navController?.navigate(SubredditRoute(subreddit.data.displayNamePrefixed)) },
                    leadingContent = {
                        SubredditIcon(
                            subreddit.data.displayName,
                            subreddit.data.icon,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    content = {
                        Text(subreddit.data.displayNamePrefixed)
                    },
                    trailingContent = {
                        IconButton(onClick = { navController?.navigate(SubredditInfoRoute(subreddit.data.displayNamePrefixed)) }) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = "Info about ${subreddit.data.displayName}"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
internal fun TopBar(subreddit: String?) {
    val navController = LocalNavController.current
    TopAppBar(
        navigationIcon = {
            BackButton {
                navController?.popBackStack()
            }
        },
        title = { Text(stringResource(R.string.subreddit_info_view_title)) },
        actions = {
            IconButton(onClick = {
                if (subreddit != null) {
                    navController?.navigate(SearchRoute(subreddit))
                }
            }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}
