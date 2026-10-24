package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.votable.VotableData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface VotableRepository<T : VotableData> : Cache<T> {
    val api: RedditAPIService
    val cache: Cache<T>

    override suspend fun insert(things: List<T>) {
        cache.insert(things)
    }

    override suspend fun insert(thing: T) {
        cache.insert(thing)
    }

    override fun get(name: Fullname): Flow<T?> =
        cache.get(name)

    override suspend fun getAsync(name: Fullname): T? {
        return cache.getAsync(name)
    }

    override suspend fun update(thing: T) {
        cache.update(thing)
    }

    override suspend fun clear() {
        cache.clear()
    }

    override suspend fun delete(name: Fullname) = withContext(Dispatchers.IO) {
        val res = api.delete(name)
        if (res.isSuccess) {
            cache.delete(name)
        } else {
            Log.e("VotableRepository", "Failed to delete: ${res.exceptionOrNull()}")
        }
        Unit
    }

    suspend fun getRules(subreddit: String): Rules = withContext(Dispatchers.IO) {
        val subreddit = if (subreddit.startsWith("r/")) subreddit else "r/$subreddit"
        val res = api.getRules(subreddit)
        if (res.isSuccess) {
            res.getOrNull()!!
        } else {
            Log.e("VotableRepository", "Failed to get rules: ${res.exceptionOrNull()}")
            Rules()
        }
    }

    suspend fun report(name: Fullname, reason: String): Result<Unit> = withContext(Dispatchers.IO) {
        val res = api.report(name, reason)
        if (res.isSuccess) {
            Result.success(Unit)
        } else {
            Log.e("PostRepository", "Error reporting post: ${res.exceptionOrNull()}")
            Result.failure(Exception("Error reporting post"))
        }
    }

    suspend fun vote(name: Fullname, upvote: Boolean?): Result<Unit> = withContext(Dispatchers.IO) {
        val thing: T? = get(name).first()
        if (thing == null) {
            Log.e("PostRepository", "Post not found in cache")
            return@withContext Result.failure(Exception("Post not found"))
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
        cache.update(newPost as T)
        val res = api.vote(thing.name, dir)
        if (res.isFailure) {
            cache.update(thing)
            Log.e("PostRepository", "Error upvoting post: ${res.exceptionOrNull()}")
        }
        return@withContext res
    }

    suspend fun upvote(name: Fullname): Result<Unit> {
        return vote(name, true)
    }

    suspend fun downvote(name: Fullname): Result<Unit> {
        return vote(name, false)
    }

    suspend fun saveHelper(name: Fullname, target: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            val post: T? = get(name).first()
            if (post == null) return@withContext Result.failure(PostNotFoundException(name))
            val relationship = post.relationship.copy(saved = target)
            val newPost = post.copy(relationship = relationship)
            cache.update(newPost as T)
            val res = if (target) api.save(post.name) else api.unsave(post.name)
            if (res.isSuccess) {
                Result.success(Unit)
            } else {
                // restore old value on failure
                cache.update(post)
                Log.e("PostRepository", "Error saving post: ${res.exceptionOrNull()}")
                Result.failure(Exception("Error saving post"))
            }
        }

    suspend fun save(name: Fullname, upvote: Boolean): Result<Unit> {
        return saveHelper(name, true).then {
            val post: VotableData? = get(name).first()
            if (post?.relationship?.liked != true && upvote) {
                upvote(name)
            } else {
                Result.success(Unit)
            }
        }
    }

    suspend fun unsave(name: Fullname): Result<Unit> {
        return saveHelper(name, false)
    }
}

abstract class VotableCache<T : VotableData> : Cache<T> {
    protected abstract val cache: VotableDao
    abstract fun VotableEntity?.transform(): T?

    override suspend fun insert(things: List<T>) = withContext(Dispatchers.IO) {
        cache.insert(things.map { it.toEntity() })
    }

    override suspend fun insert(thing: T) = withContext(Dispatchers.IO) {
        cache.insert(thing.toEntity())
    }

    override fun get(name: Fullname): Flow<T?> =
        cache.get(name).map { it.transform() }.distinctUntilChanged()

    override suspend fun getAsync(name: Fullname): T? = withContext(Dispatchers.IO) {
        cache.getValue(name)?.transform()
    }

    override suspend fun update(thing: T) = withContext(Dispatchers.IO) {
        cache.update(thing.name, thing.toEntity().data)
    }

    override suspend fun delete(name: Fullname) = withContext(Dispatchers.IO) {
        cache.delete(name)
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        cache.clear()
    }

}

inline fun <R, T> Result<T>.then(transform: (T) -> Result<R>): Result<R> {
    return when {
        isSuccess -> transform(getOrNull()!!)
        else -> Result.failure(exceptionOrNull()!!)
    }
}
