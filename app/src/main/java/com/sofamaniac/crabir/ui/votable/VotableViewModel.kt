package com.sofamaniac.crabir.ui.votable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.VotableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface VotableInteraction {
    val likes: StateFlow<Boolean?>
    val saved: StateFlow<Boolean>
    val rules: StateFlow<Rules>

    fun upvote(name: Fullname)
    fun downvote(name: Fullname)
    fun save(name: Fullname, target: Boolean, upvote: Boolean)
    fun fetchRules()
    fun report(name: Fullname, reason: String)
}

open class VotableViewModel<T : VotableData>(
    val fullname: Fullname,
    val subreddit: String,
    protected val repository: VotableRepository<T>,
    initialData: T? = null,
) : ViewModel(), VotableInteraction {

    open val votable = repository.get(fullname)
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialData
        )

    override val likes = votable.map { it?.relationship?.liked }
        .stateIn(viewModelScope, SharingStarted.Lazily, initialData?.relationship?.liked)
    override val saved = votable.map { it?.relationship?.saved ?: false }
        .stateIn(viewModelScope, SharingStarted.Lazily, initialData?.relationship?.saved ?: false)

    var rulesState = MutableStateFlow(Rules())
    override val rules: StateFlow<Rules> = rulesState

    override fun fetchRules() {
        if (rulesState.value.rules.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            rulesState.value = repository.getRules(subreddit)
        }
    }

    override fun report(name: Fullname, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.report(fullname, reason)
        }
    }

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upvote(fullname)
        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.downvote(fullname)
        }
    }

    override fun save(name: Fullname, target: Boolean, upvote: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                repository.save(fullname)
                if (upvote) {
                    repository.upvote(fullname)
                }
            } else {
                repository.unsave(fullname)
            }
        }
    }
}