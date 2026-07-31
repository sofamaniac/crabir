package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity

@Composable
fun defaultCommunityEntity(name: String, displayName: String): CommunityViewEntity {
    Log.d("defaultCommunityEntity", "name: $name, displayName: $displayName")
    return CommunityViewEntity(name, displayName)
}