package com.sofamaniac.reboost.domain.repository

import android.util.Log
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.Response

class PostRepository(private val api: RedditAPIService) {
    private val _cache =
        MutableStateFlow<MutableMap<String, VotableData>>(emptyMap<String, VotableData>().toMutableMap())

    fun observePost(id: String): Flow<VotableData> = _cache.map { it[id]!! }.distinctUntilChanged()

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

    fun getPost(id: String): VotableData = _cache.value[id]!!


    suspend fun vote(id: String, upvote: Boolean): Result<Unit> {
        val post: VotableData = _cache.value[id]!!
        lateinit var res: Response<Unit>
        val newLike = if (post.relationship.liked == upvote) null else upvote
        val dir = when (newLike) {
            null -> 0
            true -> 1
            false -> -1
        }
        try {
            res = api.vote(post.name, dir)
            val score = post.score.copy(score = post.score.score + dir)
            val relationship = post.relationship.copy(liked = newLike)
            if (res.isSuccessful) {
                _cache.value.compute(id) { _, value ->
                    value?.relationship = relationship
                    value?.score = score
                    value
                }
                return Result.success(Unit)
            } else {
                Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
                return Result.failure(Exception("Error upvoting post"))
            }
        } catch (e: Exception) {
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

    suspend fun saveHelper(id: String, target: Boolean): Result<Unit> {
        val post: VotableData = _cache.value[id]!!
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                val relationship = post.relationship.copy(saved = target)
                _cache.value.compute(id) { _, value ->
                    value?.relationship = relationship
                    value
                }
                return Result.success(Unit)
            } else {
                Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
                return Result.failure(Exception("Error saving post"))
            }
        } catch (e: Exception) {
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