package com.sofamaniac.crabir.settings.history

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sofamaniac.crabir.LocalHistorySettings
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.map
import org.koin.compose.koinInject
import org.koin.core.annotation.Single
import java.time.Clock

@Single
class HistoryManager(val history: VisitedPostsDao) {
    suspend fun addPost(name: Fullname, account: Int) {
        val entity = history.getPost(name) ?: VisitedPostEntity(
            id = name,
            visitedAt = 0,
            visitedBy = account
        )
        history.insert(entity.copy(visitedAt = Clock.systemUTC().millis(), visitedBy = account))
    }

    suspend fun updateComments(name: Fullname, comments: List<Fullname>, focusedComment: Fullname) {
        history.getPost(name).map(default = {
            Log.e("HistoryManager", "updateComments: Post not found in database ($name)")
        }) { entity ->
            history.insert(entity.copy(comments = comments, focusedComment = focusedComment))
        }
    }

    suspend fun clearHistory() {
        history.clearAll()
    }
}

@Composable
fun SaveToHistory(name: Fullname, nsfw: Boolean, historyManager: HistoryManager = koinInject()) {
    val account = LocalRedditAccount.current.id
    val settings = LocalHistorySettings.current
    LaunchedEffect(name, nsfw) {
        if (!settings.enabled) {
            Log.v("HistoryManager", "History is disabled")
            return@LaunchedEffect
        }
        if (nsfw && !settings.saveNSFW) {
            Log.v("HistoryManager", "NSFW post is disabled")
            return@LaunchedEffect
        }
        Log.i("HistoryManager", "Saving post to history: $name")
        historyManager.addPost(name, account)
    }
}
