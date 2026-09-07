/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditIcon
import com.sofamaniac.crabir.ui.mapColor

@Composable
fun SubredditIcon(
    subreddit: String,
    icon: SubredditIcon?,
    modifier: Modifier = Modifier,
) {
    when (icon) {
        is SubredditIcon.Icon ->
            AsyncImage(
                model = icon.url,
                contentDescription = "$subreddit icon",
                modifier.clip(shape = CircleShape)
            )

        is SubredditIcon.Color -> {
            val color = mapColor(icon.color, Color.Black)
            val tintColor = if (color.luminance() > 0.5) Color.Black else Color.White
            Icon(
                painter = painterResource(id = R.drawable.ic_community),
                contentDescription = null,
                tint = tintColor,
                modifier = modifier
                    .background(color, shape = CircleShape)
            )
        }

        null ->
            Icon(
                painter = painterResource(id = R.drawable.ic_community),
                contentDescription = null,
            )
    }
}
