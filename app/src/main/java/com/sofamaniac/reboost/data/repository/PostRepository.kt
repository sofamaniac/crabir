package com.sofamaniac.reboost.data.repository

import android.util.Log
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PostData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.Response

class PostRepository(private val api: RedditAPIService) {
    private val _cache = MutableStateFlow<Map<String, PostData>>(emptyMap())

    fun observePost(id: String): Flow<PostData> = _cache.map { it[id]!! }.distinctUntilChanged()

    /** Returns true if the post was added, false if it already existed */
    fun addPost(post: PostData): Boolean {
        val exists = _cache.value.containsKey(post.id.id)
        _cache.value += (post.id.id to post)
        return exists
    }

    fun addPosts(posts: List<PostData>) {
        val newEntries = posts.associateBy { it.id.id }
        _cache.value += newEntries
    }


    suspend fun vote(id: String, upvote: Boolean): Result<Unit> {
        val post: PostData = _cache.value[id]!!
        lateinit var updatedPost: PostData
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
            updatedPost = post.copy(relationship = relationship, score = score)
            if (res.isSuccessful) {
                _cache.value += (post.id.id to updatedPost)
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
        val post: PostData = _cache.value[id]!!
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                _cache.value += (post.id.id to post.copy(relationship = post.relationship.copy(saved = target)))
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