package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.remote.api.FlairInfo
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LinksRepository @Inject constructor(
    override val api: RedditAPIService,
    override val votableDao: VotableDao
) :
    VotableRepository<PostData> {
    suspend fun markNSFW(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.markNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = true))
        }
    }

    suspend fun unmarkNSFW(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.unmarkNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = false))
        }
    }

    suspend fun unmarkSpoiler(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.unspoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = false))
        }
    }

    suspend fun markSpoiler(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.spoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = true))
        }
    }

    suspend fun editFlair(name: Fullname, flairId: String, text: String?) {
        val post = get(name).first()
        if (post == null) return
        val subreddit = post.subreddit.name
        val res = api.selectFlair(subreddit, name, flairId, text ?: "")
        if (res.isSuccessful) {
            val oldFlair = post.linkFlair
            update(name, post.copy(linkFlair = oldFlair.copy(text = text ?: oldFlair.text)))
        }
    }

    suspend fun getFlairs(name: Fullname): List<FlairInfo> {
        val post = get(name).first()
        if (post == null) return emptyList()
        val subreddit = post.subreddit.name
        val res = api.getPostFlair(subreddit)
        return res.body() ?: emptyList()
    }

    suspend fun setInboxReplies(name: Fullname, enabled: Boolean) {
        val post = get(name).first()
        if (post == null) return
        val res = api.setSendReplies(name, enabled)
        if (res.isSuccessful) {
            update(name, post.copy(sendReplies = enabled))
        }
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
}