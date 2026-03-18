package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.api.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.Response

class VotableRepository(private val api: RedditAPIService) {
    private val _cache =
        MutableStateFlow(emptyMap<Fullname, VotableData>())

    fun observePost(name: Fullname): Flow<VotableData?> = _cache.map { it[name] }
        .distinctUntilChanged()

    suspend fun getRules(subreddit: String): Rules {
        val res = api.getRules(subreddit)
        return if (res.isSuccessful) {
            res.body()!!
        } else {
            Rules()
        }
    }

    suspend fun report(name: Fullname, reason: String): Result<Unit> {
        val res = api.report(name, reason)
        if (res.isSuccessful) {
            return Result.success(Unit)
        } else {
            Log.e("PostRepository", "Error reporting post: ${res.errorBody()}")
            return Result.failure(Exception("Error reporting post"))
        }
    }

    /** Returns true if the post was added, false if it already existed */
    fun addPost(post: VotableData): Boolean {
        val exists = _cache.value.containsKey(post.name)
        _cache.value += (post.name to post)
        return exists
    }

    fun addPosts(posts: List<VotableData>) {
        val newEntries = posts.associateBy { it.name }
        _cache.value += newEntries
    }

    fun getPost(name: Fullname): VotableData? = _cache.value[name]

    suspend fun vote(name: Fullname, upvote: Boolean): Result<Unit> {
        val post: VotableData? = _cache.value[name]
        if (post == null) {
            Log.e("PostRepository", "Post not found in cache")
            return Result.failure(Exception("Post not found"))
        }
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
        _cache.value += (name to newPost)
        try {
            res = api.vote(post.name, dir)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                _cache.value += (name to post)
                Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
                return Result.failure(Exception("Error upvoting post"))
            }
        } catch (e: Exception) {
            _cache.value += (name to post)
            Log.e("PostRepository", "Error upvoting post: ${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun upvote(name: Fullname): Result<Unit> {
        return vote(name, true)
    }

    suspend fun downvote(name: Fullname): Result<Unit> {
        return vote(name, false)
    }

    suspend fun hide(fullname: Fullname): Result<Unit> {
        val res = api.hide(fullname)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error hiding post"))
        }
        val post: VotableData? = _cache.value[fullname]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = true)
        _cache.value += (fullname to post.copy(relationship = relationship))
        return Result.success(Unit)
    }

    suspend fun unhide(fullname: Fullname): Result<Unit> {
        val res = api.unhide(fullname)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error unhiding post"))
        }
        val post: VotableData? = _cache.value[fullname]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = false)
        _cache.value += (fullname to post.copy(relationship = relationship))
        return Result.success(Unit)
    }


    suspend fun saveHelper(name: Fullname, target: Boolean): Result<Unit> {
        val post: VotableData? = _cache.value[name]
        if (post == null) return Result.failure(Exception("Post not found"))
        val relationship = post.relationship.copy(saved = target)
        _cache.value += (name to post.copy(relationship = relationship))
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                _cache.value += (name to post)
                Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
                return Result.failure(Exception("Error saving post"))
            }
        } catch (e: Exception) {
            _cache.value += (name to post)
            Log.e("PostRepository", "Error saving post: ${e.message}")
            return Result.failure(e)
        }
    }

    suspend fun save(name: Fullname): Result<Unit> {
        return saveHelper(name, true)
    }

    suspend fun unsave(name: Fullname): Result<Unit> {
        return saveHelper(name, false)
    }

}