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
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon

@Composable
fun Tile(subreddit: SubredditData, modifier: Modifier = Modifier) {
    val navController = LocalNavController.current!!
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubredditIcon(
            subreddit.display_name,
            subreddit.icon,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Text(text = subreddit.display_name)
    }
}