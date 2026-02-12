package com.sofamaniac.reboost.domain.repository

import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.remote.api.DOWNVOTED
import com.sofamaniac.reboost.data.remote.api.NEUTRAL
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.api.UPVOTED
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.domain.model.PostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ThreadRepository {
    suspend fun getComments(
        permalink: String,
        sort: Sort, timeframe: Timeframe? = null
    ): List<Thing>

    suspend fun getPost(id: String): PostData?
    suspend fun getMoreComments(more: Thing.More): List<Thing>

    /** Extract id from post permalink. */
    fun getPostId(permalink: String): String

    fun refresh()

    suspend fun upvote(id: String)
    suspend fun neutralVote(id: String)
    suspend fun downvote(id: String)
    suspend fun save(id: String)
    suspend fun unsave(id: String)

    val isRefreshing: StateFlow<Boolean>
}

class ThreadRepositoryImpl(
    val api: RedditAPIService,
    val visitedPostsDao: VisitedPostsDao,
    val postRepository: PostRepository
) :
    ThreadRepository {
    private var post: PostData? = null
    private var comments: List<Thing> = emptyList()
    private var _isRefreshing = MutableStateFlow(false)
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    suspend fun fetchThread(permalink: String, sort: Sort) {
        if (post != null && comments.isNotEmpty()) {
            return
        }
        _isRefreshing.value = true
        val response = api.getThread(permalink, sort = sort)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val data = body.post.data.children.first()
                post = PostDataMapper.map(data.data)
                comments = body.comments.data.children
                postRepository.addPost(post!!)
            }
        }
        _isRefreshing.value = false
    }

    override suspend fun getPost(id: String): PostData? {
        if (post != null) {
            return post
        } else {
            post = postRepository.getPost(id) as PostData
        }
        return post
    }

    override suspend fun getComments(
        permalink: String,
        sort: Sort,
        timeframe: Timeframe?
    ): List<Thing> {
        fetchThread(permalink, sort)
        return comments
    }

    override fun getPostId(permalink: String): String {
        val segments = permalink.split("/")
        val index = segments.indexOf("comments")
        return segments[index + 1]
    }

    override suspend fun getMoreComments(more: Thing.More): List<Thing.Comment> {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        comments = emptyList()
    }

    override suspend fun upvote(id: String) {
        api.vote(id, UPVOTED)
        comments = comments.map {
            if (it is Thing.Comment && it.data.id == id) {
                it.copy(data = it.data.copy(likes = true))
            } else {
                it
            }
        }
    }

    override suspend fun neutralVote(id: String) {
        api.vote(id, NEUTRAL)
        comments = comments.map {
            if (it is Thing.Comment && it.data.id == id) {
                it.copy(data = it.data.copy(likes = null))
            } else {
                it
            }
        }
    }

    override suspend fun downvote(id: String) {
        api.vote(id, DOWNVOTED)
        comments = comments.map {
            if (it is Thing.Comment && it.data.id == id) {
                it.copy(data = it.data.copy(likes = false))
            } else {
                it
            }
        }
    }

    override suspend fun save(id: String) {
        api.save(id)
        comments = comments.map {
            if (it is Thing.Comment && it.data.id == id) {
                it.copy(data = it.data.copy(saved = true))
            } else {
                it
            }
        }
    }

    override suspend fun unsave(id: String) {
        api.save(id)
        comments = comments.map {
            if (it is Thing.Comment && it.data.id == id) {
                it.copy(data = it.data.copy(saved = false))
            } else {
                it
            }
        }
    }

}

