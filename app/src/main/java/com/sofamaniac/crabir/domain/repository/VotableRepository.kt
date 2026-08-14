package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface VotableRepository<T : VotableData> {

    val api: RedditAPIService
    val votableDao: VotableDao
    fun VotableEntity?.into(): T?
    fun insert(things: Iterable<T>) {
        votableDao.insert(things.map { it.toEntity() })
    }

    fun insert(thing: VotableData) {
        votableDao.insert(thing.toEntity())
    }

    fun get(name: Fullname): Flow<T?> =
        votableDao.get(name).map { it?.into() }
            .distinctUntilChanged()

    fun getValue(name: Fullname): T? =
        votableDao.getValue(name)?.into()

    fun update(name: Fullname, data: VotableData) {
        votableDao.update(name, data.toEntity().data)
    }

    fun clear() {
        votableDao.clear()
    }

    suspend fun delete(name: Fullname) {
        try {
            val res = api.delete(name)
            if (res.isSuccessful) {
                votableDao.delete(name)
            }
        } catch (e: Exception) {
            Log.e("VotableRepository", "Failed to delete: $e")
        }
    }

    suspend fun getRules(subreddit: String): Rules {
        try {
            val subreddit = if (subreddit.startsWith("r/")) subreddit else "r/$subreddit"
            val res = api.getRules(subreddit)
            return if (res.isSuccessful) {
                res.body()!!
            } else {
                Rules()
            }
        } catch (e: Exception) {
            Log.e("VotableRepository", "Failed to get rules: $e")
            //            return Result.failure(e)
            return Rules()
        }
    }

    suspend fun report(name: Fullname, reason: String): Result<Unit> {
        try {
            val res = api.report(name, reason)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                Log.e("PostRepository", "Error reporting post: ${res.errorBody()}")
                return Result.failure(Exception("Error reporting post"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun vote(name: Fullname, upvote: Boolean?): Result<Unit> {
        val thing: VotableData? = get(name).first()
        if (thing == null) {
            Log.e("PostRepository", "Post not found in cache")
            return Result.failure(Exception("Post not found"))
        }
        val newLikes = if (thing.relationship.liked == upvote) null else upvote
        val dir = when (newLikes) {
            null -> 0
            true -> 1
            false -> -1
        }
        val oldLikes = thing.relationship.liked
        val relationship = thing.relationship.copy(liked = newLikes)
        val newPost = thing.copy(
            relationship = relationship,
        ).updateScore(oldLikes, newLikes)
        votableDao.update(name, newPost.toEntity().data)
        try {
            val res = api.vote(thing.name, dir)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                votableDao.update(name, thing.toEntity().data)
                Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
                return Result.failure(Exception("Error upvoting post"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun upvote(name: Fullname): Result<Unit> {
        return vote(name, true)
    }

    suspend fun downvote(name: Fullname): Result<Unit> {
        return vote(name, false)
    }


    suspend fun saveHelper(name: Fullname, target: Boolean): Result<Unit> {
        val post: VotableData? = get(name).first()
        if (post == null) return Result.failure(Exception("Post not found"))
        val relationship = post.relationship.copy(saved = target)
        val newPost = post.copy(relationship = relationship)
        votableDao.update(name, newPost.toEntity().data)
        try {
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccessful) {
                return Result.success(Unit)
            } else {
                // restore old value on failure
                votableDao.update(name, post.toEntity().data)
                Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
                return Result.failure(Exception("Error saving post"))
            }
        } catch (e: Exception) {
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