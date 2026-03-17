package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.dto.post.PostFullname
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.Response

class VotableRepository(private val api: RedditAPIService) {
    private val _cache =
        MutableStateFlow(emptyMap<String, VotableData>())

    fun observePost(id: String): Flow<VotableData?> = _cache.map { it[id] }
        .distinctUntilChanged()

    /** Returns true if the post was added, false if it already existed */
    fun addPost(post: VotableData): Boolean {
        val exists = _cache.value.containsKey(post.id)
        _cache.value += (post.id to post)
        return exists
    }

    fun addPosts(posts: List<VotableData>) {
        val newEntries = posts.associateBy { it.id }
        _cache.value += newEntries
    }

    fun getPost(id: String): VotableData? = _cache.value[id]


    suspend fun vote(id: String, upvote: Boolean): Result<Unit> {
        val post: VotableData? = _cache.value[id]
        if (post == null) return Result.failure(Exception("Post not found"))
        lateinit var res: Response<Unit>
        val newLike = if (post.relationship.liked == upvote) null else upvote
        val dir = when (newLike) {
            null -> 0
            true -> 1
            false -> -1
        }
        val relationship = post.relationship.copy(liked = newLike)
        val newPost = post.copy(
            relationship = relationship,
            score = post.score.copy(score = post.score.score + dir)
        )
        _cache.value += (id to newPost)
        try {
            res = api.vote(post.name, dir)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                _cache.value += (id to post)
                Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
                return Result.failure(Exception("Error upvoting post"))
            }
        } catch (e: Exception) {
            _cache.value += (id to post)
            Log.e("PostRepository", "Error upvoting post: ${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun upvote(id: String): Result<Unit> {
        return vote(id, true)
    }

    suspend fun downvote(id: String): Result<Unit> {
        return vote(id, false)
    }

    suspend fun hide(id: String): Result<Unit> {
        val res = api.hide(PostFullname(id))
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error hiding post"))
        }
        val post: VotableData? = _cache.value[id]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = true)
        _cache.value += (id to post.copy(relationship = relationship))
        return Result.success(Unit)
    }

    suspend fun unhide(id: String): Result<Unit> {
        val res = api.unhide(PostFullname(id))
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error unhiding post"))
        }
        val post: VotableData? = _cache.value[id]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = false)
        _cache.value += (id to post.copy(relationship = relationship))
        return Result.success(Unit)
    }


    suspend fun saveHelper(id: String, target: Boolean): Result<Unit> {
        val post: VotableData? = _cache.value[id]
        if (post == null) return Result.failure(Exception("Post not found"))
        val relationship = post.relationship.copy(saved = target)
        _cache.value += (id to post.copy(relationship = relationship))
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                _cache.value += (id to post)
                Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
                return Result.failure(Exception("Error saving post"))
            }
        } catch (e: Exception) {
            _cache.value += (id to post)
            Log.e("PostRepository", "Error saving post: ${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun save(id: String): Result<Unit> {
        return saveHelper(id, true)
    }

    suspend fun unsave(id: String): Result<Unit> {
        return saveHelper(id, false)
    }

}