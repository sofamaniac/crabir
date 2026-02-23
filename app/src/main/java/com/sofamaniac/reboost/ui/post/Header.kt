/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.SubredditRoute
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.settings.DefaultReboostTheme
import com.sofamaniac.reboost.settings.themeDataStore
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon


@Composable
fun PostHeader(
    post: PostData,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
) {
    val navController = LocalNavController.current!!
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
        coroutineScope.coroutineContext
    )
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showSubredditIcon) {
            SubredditIcon(
                post.subreddit.name,
                post.subredditDetails?.icon,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        navController.navigate(SubredditRoute(post.subreddit.name))
                    })
            )
        }
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "Subreddit",
                    styles = TextLinkStyles(style = SpanStyle(color = theme.highlight)),
                    linkInteractionListener = {
                        navController.navigate(SubredditRoute(post.subreddit.name))
                    })
            ) {
                append(post.subreddit.name)
            }
            append(" · ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "User",
                    styles = TextLinkStyles(style = SpanStyle(color = theme.secondaryText)),
                    linkInteractionListener = {
                        navController.navigate(ProfileRoute(post.author.username))
                    })
            ) {
                append(post.author.username)
            }
            if (!post.domain.contains("reddit") && !post.domain.endsWith("redd.it")) {
                append(" · ")
                append(post.domain)
            }
            append(" · ")
            append(formatElapsedTimeLocalized(post.createdUtc))
        }
        Text(text, style = MaterialTheme.typography.bodySmall.copy(color = theme.secondaryText))
        if (post.isCrosspost) Icon(
            Icons.Outlined.Shuffle,
            contentDescription = "Crosspost",
            modifier = Modifier.size(16.dp),
            tint = Color.Green
        )
        // TODO: take last edit into account
    }
}
