package com.sofamaniac.crabir.ui.search.community

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.icu.text.CompactDecimalFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.utils.fromHtml
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostCreatorRoute
import com.sofamaniac.crabir.navigation.SubredditInfoRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.Over18Cartouche
import com.sofamaniac.crabir.ui.components.SubredditIcon
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.components.ThemedDialog
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.post.withSeparator
import com.sofamaniac.crabir.ui.richtext.Richtext
import java.util.Locale

@Composable
fun SubredditItem(
    subreddit: SubredditData,
    subscribe: (SubredditData) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    ThemedCard(modifier = modifier.clickable {
        navController?.navigate(SubredditRoute(subreddit.displayNamePrefixed))
    }) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubredditIcon(
                    subreddit = subreddit.displayName,
                    icon = subreddit.icon,
                    modifier = Modifier.size(48.dp)
                )
                SubredditItemContent(subreddit, subscribe)
            }
            Richtext(
                document = RichtextDocument.fromHtml(subreddit.publicDescriptionHtml),
                mediaMetadata = emptyMap()
            )
        }
    }
}

@Composable
private fun SubredditItemContent(
    subreddit: SubredditData,
    subscribe: (SubredditData) -> Unit,
    modifier: Modifier = Modifier,
) {
    val num = remember {
        CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        ).format(subreddit.subscribers)
    }
    val supportString = buildAnnotatedString {
        append(
            pluralStringResource(
                R.plurals.subreddit_subscribers_count,
                subreddit.subscribers,
                num,
            )
        )
        if (subreddit.createdUtc != null) {
            withSeparator {
                append(formatElapsedTimeLocalized(subreddit.createdUtc))
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = modifier) {
            Text(subreddit.displayName, style = MaterialTheme.typography.labelMedium)
            Text(
                supportString,
                style = MaterialTheme.typography.labelSmall
            )
        }
        if (subreddit.over18) {
            Over18Cartouche()
        }
        if (subreddit.subredditType == "private") {
            PrivateCartouche()
        }
        if (subreddit.subredditType == "restricted") {
            RestrictedCartouche()
        }
        Spacer(modifier = Modifier.weight(1f))
        ShortSubscribeButton(subreddit, subscribe)
        OptionsButton(subreddit)
    }
}

@Composable
private fun ShortSubscribeButton(subreddit: SubredditData, subscribe: (SubredditData) -> Unit) {
    IconButton(onClick = { subscribe(subreddit) }) {
        if (subreddit.userIsSubscriber) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Subscribed",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(Icons.Default.AddCircle, contentDescription = "Subscribe")
        }
    }
}

@Composable
fun OptionsButton(subreddit: SubredditData) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Options")
    }

    val navController = LocalNavController.current

    if (showDialog) {
        ThemedDialog(onDismissRequest = { showDialog = false }) {
            ListItem(
                onClick = {
                    navController?.navigate(SubredditInfoRoute(subreddit.displayNamePrefixed))
                },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                content = { Text("Community info") }
            )
            CreatePostItem(subreddit)
            ShareCommunityItem(subreddit)

        }
    }
}


@Composable
fun CreatePostItem(subreddit: SubredditData) {
    var showPostKindSelector by rememberSaveable { mutableStateOf(false) }
    val navController = LocalNavController.current
    fun navigate(kind: Kind) {
        navController?.navigate(
            PostCreatorRoute(
                kind,
                subreddit.displayNamePrefixed
            )
        )
    }
    ListItem(
        onClick = { showPostKindSelector = true },
        leadingContent = { Icon(Icons.Default.Create, contentDescription = null) },
        content = { Text("Create post") }
    )
    if (showPostKindSelector) {
        ThemedDialog(onDismissRequest = { showPostKindSelector = false }) {
            ListItem(
                onClick = { navigate(Kind.Self) },
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.Article,
                        contentDescription = null
                    )
                },
                content = { Text("Text") }
            )
            ListItem(
                onClick = { navigate(Kind.Image) },
                leadingContent = {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null
                    )
                },
                content = { Text("Image") }
            )
            ListItem(
                onClick = { navigate(Kind.Link) },
                leadingContent = {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null
                    )
                },
                content = { Text("Link") }
            )
            ListItem(
                onClick = { navigate(Kind.Video) },
                leadingContent = {
                    Icon(
                        Icons.Default.VideoFile,
                        contentDescription = null
                    )
                },
                content = { Text("Video") }
            )
        }
    }
}

@Composable
fun ShareCommunityItem(subreddit: SubredditData) {
    val context = LocalContext.current
    fun shareLink() {
        val sendIntent = Intent().apply {
            action = ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://reddit.com/r/${subreddit.displayName}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
    ListItem(
        onClick = { shareLink() },
        leadingContent = { Icon(Icons.Default.Share, contentDescription = null) },
        content = { Text("Share community") }
    )
}
