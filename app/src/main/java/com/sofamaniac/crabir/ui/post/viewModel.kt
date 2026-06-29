package com.sofamaniac.crabir.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.DUMMY_POST
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.ui.votable.VotableInteraction
import com.sofamaniac.crabir.ui.votable.VotableViewModel
import com.sofamaniac.redditmarkdown.redditFlavour.RedditFlavourDescriptor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface LinkInteraction : VotableInteraction {
    val post: Flow<PostData?>
    val flairs: StateFlow<List<FlairInfo>>
    fun hide()

    fun unhide()

    fun delete()

    fun editFlair(flairId: String, text: String?)

    fun getFlairs()

    fun markNSFW()

    fun unmarkNSFW()

    fun markSpoiler()

    fun unmarkSpoiler()

    fun setInboxReplies(enabled: Boolean)
}


@HiltViewModel(assistedFactory = LinkViewModel.Factory::class)
open class LinkViewModel @AssistedInject constructor(
    @Assisted("post") initialPost: PostData,
    private val posts: LinksRepository,
    private val history: VisitedPostsDao,
) : VotableViewModel<PostData>(
    initialPost.name.name,
    initialPost.subreddit.name,
    posts,
    initialPost
),
    PostViewModelInterface {

    override val post = posts.get(initialPost.name).map { it ?: initialPost }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = initialPost
    )

    private var _flairs = MutableStateFlow(emptyList<FlairInfo>())
    override val flairs: StateFlow<List<FlairInfo>> = _flairs

    fun markPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity: VisitedPostEntity =
                VisitedPostEntity(post.name, System.currentTimeMillis(), visitedBy)
            history.insert(entity)
        }
    }

    override fun hide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.hide(fullname)
        }
    }

    override fun unhide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unhide(fullname)
        }
    }

    override fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.delete(fullname)
        }
    }

    override fun editFlair(flairId: String, text: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.editFlair(fullname, flairId, text)
        }
    }

    override fun getFlairs() {
        viewModelScope.launch(Dispatchers.IO) {
            _flairs.value = posts.getFlairs(fullname)
        }
    }

    override fun markNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markNSFW(fullname)
        }
    }

    override fun unmarkNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkNSFW(fullname)
        }
    }

    override fun markSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markSpoiler(fullname)
        }
    }

    override fun unmarkSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkSpoiler(fullname)
        }
    }

    override fun setInboxReplies(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.setInboxReplies(fullname, enabled)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("post") post: PostData
        ): LinkViewModel
    }
}

interface PostViewModelInterface : LinkInteraction, VotableInteraction

class DummyInteraction : LinkInteraction, ViewModel() {
    private var _post = MutableStateFlow(DUMMY_POST.copy(kind = Kind.Self))
    override val post: StateFlow<PostData> = _post
    override val flairs: StateFlow<List<FlairInfo>> = MutableStateFlow(emptyList())
    override val markdown: StateFlow<State> =
        parseMarkdownFlow(
            DUMMY_POST.selftext.markdown.markdown,
            flavour = RedditFlavourDescriptor(true)
        ).stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = State.Loading())

    override fun hide() {
    }

    override fun unhide() {
    }

    override fun delete() {
    }

    override fun editFlair(flairId: String, text: String?) {
    }

    override fun getFlairs() {
    }

    override fun markNSFW() {
        _post.value = _post.value.copy(over18 = true)
    }

    override fun unmarkNSFW() {
        _post.value = _post.value.copy(over18 = false)
    }

    override fun markSpoiler() {
        _post.value = _post.value.copy(spoiler = true)
    }

    override fun unmarkSpoiler() {
        _post.value = _post.value.copy(spoiler = false)
    }

    override fun setInboxReplies(enabled: Boolean) {
    }

    override val likes: Flow<Boolean?> = _post.map { it.relationship.liked }
    override val saved: Flow<Boolean> = _post.map { it.relationship.saved }
    override val rules: StateFlow<Rules> = MutableStateFlow(Rules())

    override fun upvote(name: Fullname) {
        val likes = _post.value.relationship.liked
        var relationship = _post.value.relationship
        var score = _post.value.score
        if (likes == true) {
            relationship = relationship.copy(liked = null)
            score = score.copy(ups = score.ups - 1, score = score.score - 1)
        } else {
            relationship = relationship.copy(liked = true)
            score = score.copy(ups = score.ups + 1, score = score.score + 1)
        }
        _post.value =
            _post.value.copy(
                relationship = relationship,
                score = score
            )
    }

    override fun downvote(name: Fullname) {
        val likes = _post.value.relationship.liked
        var relationship = _post.value.relationship
        var score = _post.value.score
        if (likes == false) {
            relationship = relationship.copy(liked = null)
            score = score.copy(downs = score.downs - 1, score = score.score + 1)
        } else {
            relationship = relationship.copy(liked = false)
            score = score.copy(downs = score.downs + 1, score = score.score - 1)
        }
        _post.value =
            _post.value.copy(
                relationship = relationship,
                score = score
            )
    }

    override fun save(name: Fullname, target: Boolean) {
        _post.value = _post.value.copy(relationship = _post.value.relationship.copy(saved = target))
    }

    override fun fetchRules() {
    }

    override fun report(reason: String) {
    }
}