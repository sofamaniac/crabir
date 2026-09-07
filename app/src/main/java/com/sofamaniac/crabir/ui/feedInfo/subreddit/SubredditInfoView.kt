package com.sofamaniac.crabir.ui.feedInfo.subreddit

import android.icu.text.CompactDecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.utils.fromHtml
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.components.SubredditIcon
import com.sofamaniac.crabir.ui.richtext.Richtext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun SubredditInfoView(
    subreddit: String,
    viewModel: SubredditInfoViewModel = koinViewModel { parametersOf(subreddit) },
) {
    val infoOpt by viewModel.info.collectAsState()

    val theme = LocalTheme.current

    Scaffold(
        containerColor = theme.cardBackground,
        topBar = { TopBar(infoOpt?.displayName) }
    ) { padding ->
        if (infoOpt == null) return@Scaffold

        val info = infoOpt!!
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubredditIcon(info.displayName, info.icon, modifier = Modifier.size(48.dp))
                    Column {
                        Text(info.displayNamePrefixed, style = MaterialTheme.typography.titleMedium)
                        val num = remember {
                            CompactDecimalFormat.getInstance(
                                Locale.getDefault(),
                                CompactDecimalFormat.CompactStyle.SHORT
                            ).format(info.subscribers)
                        }
                        val members = LocalResources.current.getQuantityString(
                            R.plurals.subreddit_subscribers_count,
                            info.subscribers,
                            num
                        )
                        Text(members, style = MaterialTheme.typography.labelSmall)
                        TextButton(onClick = {}) {
                            Text(stringResource(R.string.edit_flair_button_title))
                        }
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubscribeButton(info.userIsSubscriber) {
                        if (info.userIsSubscriber) {
                            viewModel.unsubscribe()
                        } else {
                            viewModel.subscribe()
                        }
                    }
                    FavoriteButton(info.userHasFavorited) {
                        viewModel.favorite(!info.userHasFavorited)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
            item {
                Richtext(
                    document = RichtextDocument.fromHtml(info.descriptionHtml),
                    mediaMetadata = emptyMap(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
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
