/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon
import com.sofamaniac.crabir.ui.user.ProfileTabs


@Composable
fun PostHeader(
    post: PostData,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
    showPrefix: Boolean = false,
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        //verticalAlignment = Alignment.CenterVertically,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        if (showSubredditIcon) {
            SubredditIcon(
                post.subreddit.name,
                post.subredditDetails?.icon,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        navController?.navigate(SubredditRoute(post.subreddit.name))
                    })
            )
        }
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "Subreddit",
                    styles = TextLinkStyles(style = SpanStyle(color = theme.highlight)),
                    linkInteractionListener = {
                        navController?.navigate(SubredditRoute(post.subreddit.name))
                    })
            ) {
                append(
                    if (showPrefix) post.subreddit.subredditPrefixed
                    else post.subreddit.name
                )
            }
            withSeparator {
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "User",
                        styles = TextLinkStyles(style = SpanStyle(color = theme.secondaryText)),
                        linkInteractionListener = {
                            navController?.navigate(
                                ProfileRoute(
                                    post.author.username,
                                    ProfileTabs.Overview
                                )
                            )
                        })
                ) {
                    append(post.author.username)
                }
            }
            if (post.kind != Kind.Self) {
                withSeparator { append(post.domain) }
            }
            withSeparator { append(formatElapsedTimeLocalized(post.createdUtc)) }
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

fun <R : Any> AnnotatedString.Builder.withSeparator(
    separator: String = " · ",
    block: AnnotatedString.Builder.() -> R
): R {
    append(separator)
    return block(this)
}