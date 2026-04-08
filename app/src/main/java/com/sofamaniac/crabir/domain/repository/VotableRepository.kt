package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.api.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class VotableRepository(
    private val api: RedditAPIService,
    private val votableDao: VotableDao
) {
    fun insert(things: List<VotableData>) {
        for (thing in things) {
            votableDao.insert(thing.toEntity())
        }
    }

    fun get(name: Fullname): Flow<PostData?> =
        votableDao.get(name).map { it?.asVotableData() as PostData }
        .distinctUntilChanged()

    fun update(name: Fullname, data: VotableData) {
        votableDao.update(name, data.toEntity().data)
    }

    suspend fun delete(name: Fullname) {
        val res = api.delete(name)
        if (res.isSuccessful) {
            votableDao.delete(name)
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
        val post: VotableData? = get(name).first()
        if (post == null) {
            Log.e("PostRepository", "Post not found in cache")
            return Result.failure(Exception("Post not found"))
        }
        val newLikes = if (post.relationship.liked == upvote) null else upvote
        val dir = when (newLikes) {
            null -> 0
            true -> 1
            false -> -1
        }
        val oldLikes = post.relationship.liked
        val relationship = post.relationship.copy(liked = newLikes)
        val newPost = post.copy(
            relationship = relationship,
        ).updateScore(oldLikes, newLikes)
        votableDao.update(name, newPost.toEntity().data)
        val res = api.vote(post.name, dir)
        if (res.isSuccessful) {
            return Result.success(Unit)
        } else {
            votableDao.update(name, post.toEntity().data)
            Log.e("PostRepository", "Error upvoting post: ${res.errorBody()}")
            return Result.failure(Exception("Error upvoting post"))
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
        val post: VotableData? = get(fullname).first()
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = true)
        votableDao.update(fullname, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }

    suspend fun unhide(fullname: Fullname): Result<Unit> {
        val res = api.unhide(fullname)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error unhiding post"))
        }
        val post: VotableData? = get(fullname).first()
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = false)
        votableDao.update(fullname, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }


    suspend fun saveHelper(name: Fullname, target: Boolean): Result<Unit> {
        val post: VotableData? = get(name).first()
        if (post == null) return Result.failure(Exception("Post not found"))
        val relationship = post.relationship.copy(saved = target)
        val newPost = post.copy(relationship = relationship)
        votableDao.update(name, newPost.toEntity().data)
        val res = if (target) api.save(post.name) else api.unsave(post.name)
        if (res.isSuccessful) {
            return Result.success(Unit)
        } else {
            // restore old value on failure
            votableDao.update(name, post.toEntity().data)
            Log.e("PostRepository", "Error saving post: ${res.errorBody()}")
            return Result.failure(Exception("Error saving post"))
        }
    }

    suspend fun save(name: Fullname): Result<Unit> {
        return saveHelper(name, true)
    }

    suspend fun unsave(name: Fullname): Result<Unit> {
        return saveHelper(name, false)
    }
}