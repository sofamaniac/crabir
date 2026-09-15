package com.sofamaniac.crabir.ui.subredditList

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.SubredditIcon

@Composable
fun Tile(subreddit: SubredditData, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            SubredditIcon(
                subreddit.displayName,
                subreddit.icon,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }) {
        Text(text = subreddit.displayName)
    }
}
