package com.sofamaniac.crabir.ui.votable

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.redditmarkdown.redditFlavour.RedditFlavourDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.mikepenz.markdown.model.State as MarkdownState

interface VotableInteraction {
    val likes: Flow<Boolean?>
    val saved: Flow<Boolean>
    val rules: StateFlow<Rules>

    val markdown: StateFlow<MarkdownState>
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

    private var _post = posts.get(fullname)

    @OptIn(ExperimentalCoroutinesApi::class)
    override var markdown: StateFlow<MarkdownState> = _post.flatMapLatest { post ->
        if (post == null) flowOf(MarkdownState.Loading())
        else parseMarkdownFlow(post.body.markdown, flavour = RedditFlavourDescriptor(true))
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MarkdownState.Loading()
    )

    init {
        require(name.contains("_"))
        viewModelScope.launch(Dispatchers.Default) {
            val post = posts.getValue(fullname)
            if (post != null) {
                markdown = parseMarkdownFlow(
                    post.body.markdown,
                    flavour = RedditFlavourDescriptor(true)
                ).stateIn(
                    viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = MarkdownState.Loading()
                )
            } else {
                Log.e("VotableViewModel", "$fullname not found")
                markdown = parseMarkdownFlow(
                    "",
                    flavour = RedditFlavourDescriptor(true)
                ).stateIn(
                    viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = MarkdownState.Loading()
                )
            }
        }
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