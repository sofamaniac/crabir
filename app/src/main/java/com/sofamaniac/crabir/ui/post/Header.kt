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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.post.InfoSettings
import com.sofamaniac.crabir.ui.components.SubredditIcon
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.user.ProfileTabs


@Composable
fun PostHeader(
    post: PostData,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
    showPrefix: Boolean = false,
    settings: InfoSettings = LocalPostSettings.current.infoSettings,
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    val iconSize = 16.dp
    val density = LocalDensity.current
    val iconSizeSp = with(density) { iconSize.toSp() }
    val inlineContent = mapOf(
        "crosspost" to InlineTextContent(
            Placeholder(
                iconSizeSp,
                iconSizeSp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Icon(
                Icons.Outlined.Shuffle,
                contentDescription = "Crosspost",
                modifier = Modifier.size(16.dp),
                tint = Color.Green
            )
        },
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
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
                    .clickable(
                        enabled = settings.clickableCommunity,
                        onClick = {
                            navController?.navigate(SubredditRoute(post.subreddit.subredditPrefixed))
                        }
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        val text = buildAnnotatedString {
            fun AnnotatedString.Builder.appendCommunity() {
                append(
                    if (showPrefix) post.subreddit.subredditPrefixed
                    else post.subreddit.name
                )
            }
            if (settings.clickableCommunity) {
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "Subreddit",
                        styles = TextLinkStyles(style = SpanStyle(color = theme.highlight)),
                        linkInteractionListener = {
                            navController?.navigate(SubredditRoute(post.subreddit.subredditPrefixed))
                        })
                ) {
                    appendCommunity()
                }
            } else {
                appendCommunity()
            }
            if (settings.clickableAuthor && settings.showAuthor) {
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
            } else if (settings.showAuthor) {
                append(post.author.username)
            }
            if (post.kind != Kind.Self) {
                withSeparator { append(post.domain) }
            }
            if (post.locked) {
                withSeparator { append("\uD83D\uDD12") }
            }
            if (post.archived) {
                withSeparator { append("\uD83D\uDDC4\uFE0F") }
            }
            if (post.isDistinguished) {
                withSeparator {
                    withStyle(SpanStyle(color = theme.highlight, fontWeight = FontWeight.Bold)) {
                        append("A")
                    }
                }
            }
            val timeString = formatElapsedTimeLocalized(post.createdUtc)
            withSeparator { append(timeString) }
            if (post.isCrosspost) appendInlineContent("crosspost", "crosspost")
        }
        Text(
            text,
            inlineContent = inlineContent,
            style = MaterialTheme.typography.bodySmall.copy(color = theme.secondaryText)
        )
    }
}

inline fun <R : Any> AnnotatedString.Builder.withSeparator(
    separator: String = " · ",
    block: AnnotatedString.Builder.() -> R,
): R {
    append(separator)
    return block(this)
}
