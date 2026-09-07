package com.sofamaniac.crabir.ui.feedInfo.subreddit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R

@Composable
fun SubscribeButton(isSubscriber: Boolean, onClick: () -> Unit = {}) {
    OutlinedButton(onClick = onClick) {
        val icon = if (isSubscriber) Icons.Default.CheckCircle else null
        val text = if (isSubscriber) "Joined" else "Subscribe"
        if (icon != null) {
            Icon(icon, contentDescription = null)
        }
        Text(text)
    }
}

@Composable
fun FavoriteButton(hasFavorited: Boolean, onClick: () -> Unit = {}) {
    val theme = LocalTheme.current
    OutlinedButton(onClick = onClick) {
        val icon = if (hasFavorited) Icons.Filled.Star else Icons.Outlined.Star
        val tint = if (hasFavorited) theme.saved else Color.Gray
        val text = stringResource(
            if (hasFavorited) R.string.unfavorite else R.string.favorite
        )
        Icon(icon, contentDescription = null, tint = tint)
        Text(text)
    }
}
