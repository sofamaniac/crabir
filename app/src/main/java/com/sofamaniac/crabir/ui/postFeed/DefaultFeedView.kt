package com.sofamaniac.crabir.ui.postFeed

import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.data.remote.reddit.HOME
import com.sofamaniac.crabir.ui.postFeed.home.HomeViewer
import com.sofamaniac.crabir.ui.postFeed.multi.MultiView
import com.sofamaniac.crabir.ui.postFeed.subreddit.SubredditViewer

@Composable
fun DefaultFeedView(feed: String?) {
    when (feed) {
        null, HOME -> HomeViewer()
        else -> {
            if (feed.startsWith("m/")) {
                MultiView(feed, tabSelected = 0)
            } else {
                SubredditViewer(feed, tabSelected = 0)
            }
        }
    }
}
