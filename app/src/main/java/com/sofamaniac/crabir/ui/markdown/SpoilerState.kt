package com.sofamaniac.crabir.ui.markdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.snapshots.SnapshotStateMap

interface SpoilerState {
    fun getState(spoiler: String): Boolean?
    fun setState(spoiler: String, target: Boolean)
}

class SpoilerStateImpl(private val state: SnapshotStateMap<String, Boolean> = mutableStateMapOf()) :
    SpoilerState {
    override fun getState(spoiler: String): Boolean? = state[spoiler]

    override fun setState(spoiler: String, target: Boolean) {
        state[spoiler] = target
    }

}

@Composable
fun rememberSpoilerState(): SpoilerState {
    val state = retain { SpoilerStateImpl() }
    return state
}