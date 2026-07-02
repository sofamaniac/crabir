package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Singleton

interface LinksRepository : VotableRepository<PostData> {
    suspend fun markNSFW(name: Fullname)
    suspend fun unmarkNSFW(name: Fullname)
    suspend fun markSpoiler(name: Fullname)
    suspend fun unmarkSpoiler(name: Fullname)
    suspend fun editFlair(name: Fullname, flairId: String, text: String?)
    suspend fun getFlairs(name: Fullname): List<FlairInfo>
    suspend fun setInboxReplies(name: Fullname, enabled: Boolean)
    suspend fun hide(name: Fullname): Result<Unit>
    suspend fun unhide(name: Fullname): Result<Unit>

}

@Singleton
class LinksRepositoryImpl(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) :
    LinksRepository {
    override suspend fun markNSFW(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.markNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = true))
        }
    }

    override suspend fun unmarkNSFW(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.unmarkNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = false))
        }
    }

    override suspend fun unmarkSpoiler(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.unspoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = false))
        }
    }

    override suspend fun markSpoiler(name: Fullname) {
        val post = get(name).first()
        if (post == null) return
        val res = api.spoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = true))
        }
    }

    override suspend fun editFlair(name: Fullname, flairId: String, text: String?) {
        val post = get(name).first()
        if (post == null) return
        val subreddit = post.subreddit.name
        val res = api.selectFlair(subreddit, name, flairId, text ?: "")
        if (res.isSuccessful) {
            val oldFlair = post.linkFlair
            update(name, post.copy(linkFlair = oldFlair.copy(text = text ?: oldFlair.text)))
        }
    }

    override suspend fun getFlairs(name: Fullname): List<FlairInfo> {
        val post = get(name).first()
        if (post == null) return emptyList()
        val subreddit = post.subreddit.name
        val res = api.getPostFlair(subreddit)
        return res.body() ?: emptyList()
    }

    override suspend fun setInboxReplies(name: Fullname, enabled: Boolean) {
        val post = get(name).first()
        if (post == null) return
        val res = api.setSendReplies(name, enabled)
        if (res.isSuccessful) {
            update(name, post.copy(sendReplies = enabled))
        }
    }

    override suspend fun hide(name: Fullname): Result<Unit> {
        val res = api.hide(name)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error hiding post"))
        }
        val post: VotableData? = get(name).first()
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = true)
        votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }

    override suspend fun unhide(name: Fullname): Result<Unit> {
        val res = api.unhide(name)
        if (!res.isSuccessful) {
            return Result.failure(Exception("Error unhiding post"))
        }
        val post: VotableData? = get(name).first()
        if (post == null) return Result.success(Unit)
        val relationship = post.relationship.copy(hidden = false)
        votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }
}

object DummyLinksRepository : LinksRepository {
    override suspend fun markNSFW(name: Fullname) {
    }

    override suspend fun unmarkNSFW(name: Fullname) {
    }

    override suspend fun markSpoiler(name: Fullname) {
    }

    override suspend fun unmarkSpoiler(name: Fullname) {
    }

    override suspend fun editFlair(
        name: Fullname,
        flairId: String,
        text: String?,
    ) {
    }

    override suspend fun getFlairs(name: Fullname): List<FlairInfo> {
        return emptyList()
    }

    override suspend fun setInboxReplies(
        name: Fullname,
        enabled: Boolean,
    ) {
    }

    override suspend fun hide(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun unhide(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override val api: RedditAPIService
        get() = throw NotImplementedError()
    override val votableDao: VotableDao
        get() = throw NotImplementedError()
}