package com.sofamaniac.crabir.ui.subredditList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.ui.components.SubredditIcon

@Composable
fun Tile(subreddit: SubredditData, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubredditIcon(
            subreddit.displayName,
            subreddit.icon,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Text(text = subreddit.displayName)
    }
}
