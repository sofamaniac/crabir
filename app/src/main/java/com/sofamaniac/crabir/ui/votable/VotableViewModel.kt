package com.sofamaniac.crabir.ui.votable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.VotableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface VotableInteraction {
    val likes: Flow<Boolean?>
    val saved: Flow<Boolean>
    val rules: StateFlow<Rules>
    fun upvote(name: Fullname)
    fun downvote(name: Fullname)
    fun save(name: Fullname, target: Boolean)
    fun fetchRules()
    fun report(reason: String)
}

open class VotableViewModel<T : VotableData>(
    val name: String,
    val subreddit: String,
    private val posts: VotableRepository<T>,
) : ViewModel(), VotableInteraction {

    val fullname =
        Fullname(name)

    init {
        require(name.contains("_"))
    }

    override val likes = posts.get(fullname).map { it?.relationship?.liked }
    override val saved = posts.get(fullname).map { it?.relationship?.saved ?: false }

    var _rules = MutableStateFlow(Rules())
    override val rules: StateFlow<Rules> = _rules

    override fun fetchRules() {
        if (_rules.value.rules.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _rules.value = posts.getRules(subreddit)
        }
    }

    override fun report(reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.report(fullname, reason)
        }
    }

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.upvote(fullname)
        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.downvote(fullname)
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                posts.save(fullname)
            } else {
                posts.unsave(fullname)
            }
        }
    }
}