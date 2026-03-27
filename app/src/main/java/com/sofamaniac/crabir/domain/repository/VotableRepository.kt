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
import kotlinx.coroutines.flow.update
import retrofit2.Response

open class VotableRepository(private val api: RedditAPIService) {
    protected val cache = MutableStateFlow(emptyMap<Fullname, VotableData>())
    fun insert(things: List<VotableData>) {
        val newEntries = things.associateBy { it.name }
        cache.update {
            it + newEntries
        }
    }

    fun get(name: Fullname): Flow<VotableData?> = cache.map { it[name] }.distinctUntilChanged()

    fun update(name: Fullname, data: VotableData) {
        cache.update {
            it + (name to data)
        }
    }

    suspend fun delete(name: Fullname) {
        val res = api.delete(name)
        if (res.isSuccessful) {
            cache.update {
                it.toMutableMap().apply {
                    remove(name)
                }
            }
        }
    }

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
    suspend fun vote(name: Fullname, upvote: Boolean): Result<Unit> {
        val post: VotableData? = cache.value[name]
        if (post == null) {
            Log.e("PostRepository", "Post not found in cache")
            return Result.failure(Exception("Post not found"))
        }
        lateinit var res: Response<Unit>
        val oldDelta = when (post.relationship.liked) {
            null -> 0
            true -> 1
            false -> -1
        }
        val newLike = if (post.relationship.liked == upvote) null else upvote
        val dir = when (newLike) {
            null -> 0
            true -> 1
            false -> -1
        }
        val relationship = post.relationship.copy(liked = newLike)
        val newPost = post.copy(
            relationship = relationship,
            score = post.score.copy(score = post.score.score - oldDelta + dir)
        )
        cache.value += (name to newPost)
        try {
            res = api.vote(post.name, dir)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                cache.value += (name to post)
                Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
                return Result.failure(Exception("Error upvoting post"))
            }
        } catch (e: Exception) {
            cache.value += (name to post)
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
        val post: VotableData? = cache.value[fullname]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = true)
        cache.value += (fullname to post.copy(relationship = relationship))
        return Result.success(Unit)
    }

    suspend fun unhide(fullname: Fullname): Result<Unit> {
        val res = api.unhide(fullname)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error unhiding post"))
        }
        val post: VotableData? = cache.value[fullname]
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = false)
        cache.value += (fullname to post.copy(relationship = relationship))
        return Result.success(Unit)
    }


    suspend fun saveHelper(name: Fullname, target: Boolean): Result<Unit> {
        val post: VotableData? = cache.value[name]
        if (post == null) return Result.failure(Exception("Post not found"))
        val relationship = post.relationship.copy(saved = target)
        cache.value += (name to post.copy(relationship = relationship))
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                cache.value += (name to post)
                Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
                return Result.failure(Exception("Error saving post"))
            }
        } catch (e: Exception) {
            cache.value += (name to post)
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