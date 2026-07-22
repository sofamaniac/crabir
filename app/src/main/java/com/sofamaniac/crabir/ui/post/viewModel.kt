package com.sofamaniac.crabir.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.DUMMY_POST
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.settings.post.LinksSettings
import com.sofamaniac.crabir.settings.post.PostSettingsDefaults
import com.sofamaniac.crabir.settings.post.PostSettingsRepository
import com.sofamaniac.crabir.ui.votable.VotableInteraction
import com.sofamaniac.crabir.ui.votable.VotableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

interface LinkInteraction : VotableInteraction {
    val post: StateFlow<PostData?>
    val flairs: StateFlow<List<FlairInfo>>
    val linksSettings: Flow<LinksSettings>
    val read: StateFlow<Boolean>
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


@KoinViewModel
open class LinkViewModel(
    @InjectedParam initialPost: PostData,
    private val posts: LinksRepository,
    private val history: VisitedPostsDao,
    private val settings: PostSettingsRepository,
) : VotableViewModel<PostData>(
    initialPost.name.name,
    initialPost.subreddit.name,
    posts,
    initialPost
),
    PostViewModelInterface {

    override val linksSettings: Flow<LinksSettings> = settings.postSettings.map { it.linksSettings }
    override val post = posts.get(initialPost.name).map { it ?: initialPost }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = initialPost
    )

    private var _flairs = MutableStateFlow(emptyList<FlairInfo>())
    override val flairs: StateFlow<List<FlairInfo>> = _flairs

    override val read = history.contains(initialPost.name).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    fun markPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity =
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
}

interface PostViewModelInterface : LinkInteraction

class DummyInteraction(post: PostData = DUMMY_POST) : PostViewModelInterface, ViewModel() {
    override val read = flowOf(false).stateIn(viewModelScope, SharingStarted.Lazily, false)
    private var _post = MutableStateFlow(post.copy(kind = Kind.Self))
    override val post: StateFlow<PostData> = _post
    override val linksSettings: Flow<LinksSettings> =
        flowOf(PostSettingsDefaults.defaultLinksSettings)
    override val flairs: StateFlow<List<FlairInfo>> = MutableStateFlow(emptyList())

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

    override val likes: StateFlow<Boolean?> =
        _post.map { it.relationship.liked }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    override val saved: StateFlow<Boolean> =
        _post.map { it.relationship.saved }.stateIn(viewModelScope, SharingStarted.Lazily, false)
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