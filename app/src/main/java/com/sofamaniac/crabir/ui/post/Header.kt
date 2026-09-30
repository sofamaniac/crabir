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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.ProfileRoute
import com.sofamaniac.crabir.navigation.routes.SubredditRoute
import com.sofamaniac.crabir.settings.post.InfoSettings
import com.sofamaniac.crabir.ui.components.FlowRowWithSeparator
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
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(color = theme.secondaryText)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val navigateToSubreddit = {
                if (post.subredditDetails?.subredditType == "user") {
                    navController?.navigate(
                        post.subredditDetails.url
                    )
                } else {
                    navController?.navigate(SubredditRoute(post.subreddit.subredditPrefixed))
                }
            }
            if (showSubredditIcon) {
                SubredditIcon(
                    post.subreddit.name,
                    post.subredditDetails?.icon,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable(
                            enabled = settings.clickableCommunity,
                            onClick = { navigateToSubreddit() },
                        )
                )
            }
            FlowRowWithSeparator(
                separator = { Text(stringResource(R.string.text_separator)) },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center,
                content = buildList {
                    add({
                            Text(
                                text = if (showPrefix) post.subreddit.subredditPrefixed
                                else post.subreddit.name,
                                style = LocalTextStyle.current.copy(color = theme.highlight),
                                modifier = Modifier.clickable(
                                    enabled = settings.clickableCommunity,
                                    onClick = { navigateToSubreddit() }
                                )
                            )
                        }
                    )
                    if (settings.showAuthor) {
                        add({
                                Text(
                                    post.author.username,
                                    style = LocalTextStyle.current.copy(color = theme.secondaryText),
                                    modifier = Modifier.clickable(enabled = settings.clickableAuthor) {
                                        navController?.navigate(
                                            ProfileRoute(
                                                post.author.username,
                                                ProfileTabs.Overview
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    }
                    if (post.kind != Kind.Self) {
                        add({ Text(post.domain) })
                    }
                    if (post.locked) {
                        add({ Text(stringResource(R.string.post_locked)) })
                    }
                    if (post.archived) {
                        add({ Text(stringResource(R.string.post_archived)) })
                    }
                    if (post.isDistinguished) {
                        add({
                                Text(
                                    "A",
                                    style = LocalTextStyle.current.copy(
                                        color = theme.highlight,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            })
                    }
                    val timeString = formatElapsedTimeLocalized(post.createdUtc)
                    add({ Text(timeString) })
                    if (post.isCrosspost) {
                        add({
                                Icon(
                                    Icons.Outlined.Shuffle,
                                    contentDescription = "Crosspost",
                                    modifier = Modifier.size(LocalTextStyle.current.fontSize.value.dp),
                                    tint = Color.Green,
                                )
                            })
                    }
                }
            )
        }
    }
}

inline fun <R : Any> AnnotatedString.Builder.withSeparator(
    separator: String = " · ",
    block: AnnotatedString.Builder.() -> R,
): R {
    append(separator)
    return block(this)
}
