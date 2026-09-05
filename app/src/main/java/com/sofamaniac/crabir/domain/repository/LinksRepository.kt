package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.into
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Singleton

interface LinksRepository : VotableRepository<PostData> {
    override fun VotableEntity?.transform(): PostData? {
        return this?.into()
    }

    suspend fun markNSFW(name: Fullname): Result<Unit>
    suspend fun unmarkNSFW(name: Fullname): Result<Unit>
    suspend fun markSpoiler(name: Fullname): Result<Unit>
    suspend fun unmarkSpoiler(name: Fullname): Result<Unit>
    suspend fun editFlair(name: Fullname, flairId: String, text: String?): Result<Unit>
    suspend fun getFlairs(name: Fullname): Result<List<FlairInfo>>
    suspend fun setInboxReplies(name: Fullname, enabled: Boolean): Result<Unit>
    suspend fun hide(name: Fullname): Result<Unit>
    suspend fun unhide(name: Fullname): Result<Unit>

}

class PostNotFoundException(name: Fullname) : Exception("Post $name not found")

@Singleton
class LinksRepositoryImpl(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) :
    LinksRepository {
    override suspend fun markNSFW(name: Fullname): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val res = api.markNSFW(name)
        if (res.isSuccess) {
            update(name, post.copy(over18 = true))
        }
        return res
    }

    override suspend fun unmarkNSFW(name: Fullname): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val res = api.unmarkNSFW(name)
        if (res.isSuccess) {
            update(name, post.copy(over18 = false))
        }
        return res
    }

    override suspend fun unmarkSpoiler(name: Fullname): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val res = api.unspoiler(name)
        if (res.isSuccess) {
            update(name, post.copy(spoiler = false))
        }
        return res
    }

    override suspend fun markSpoiler(name: Fullname): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val res = api.spoiler(name)
        if (res.isSuccess) {
            update(name, post.copy(spoiler = true))
        }
        return res
    }

    override suspend fun editFlair(name: Fullname, flairId: String, text: String?): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val subreddit = post.subreddit.name
        val res = api.selectFlair("r/$subreddit", name, flairId, text ?: "")
        if (res.isSuccess) {
            val oldFlair = post.linkFlair
            update(name, post.copy(linkFlair = oldFlair.copy(text = text ?: oldFlair.text)))
        }
        return res
    }

    override suspend fun getFlairs(name: Fullname): Result<List<FlairInfo>> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val subreddit = post.subreddit.name
        val res = api.getPostFlair("r/$subreddit")
        return res
    }

    override suspend fun setInboxReplies(name: Fullname, enabled: Boolean): Result<Unit> {
        val post = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val res = api.setSendReplies(name, enabled)
        if (res.isSuccess) {
            update(name, post.copy(sendReplies = enabled))
        }
        return res
    }

    override suspend fun hide(name: Fullname): Result<Unit> {
        val res = api.hide(name)
        if (res.isFailure) {
            return res
        }
        val post: VotableData? = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val relationship = post.relationship.copy(hidden = true)
        votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }

    override suspend fun unhide(name: Fullname): Result<Unit> {
        val res = api.unhide(name)
        if (res.isFailure) {
            return res
        }
        val post: VotableData? = get(name).first()
        if (post == null) return Result.failure(PostNotFoundException(name))
        val relationship = post.relationship.copy(hidden = false)
        votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
        return Result.success(Unit)
    }
}

object DummyLinksRepository : LinksRepository {
    override suspend fun markNSFW(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun unmarkNSFW(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun markSpoiler(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun unmarkSpoiler(name: Fullname): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun editFlair(
        name: Fullname,
        flairId: String,
        text: String?,
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getFlairs(name: Fullname): Result<List<FlairInfo>> {
        return Result.success(emptyList())
    }

    override suspend fun setInboxReplies(
        name: Fullname,
        enabled: Boolean,
    ): Result<Unit> {
        return Result.success(Unit)
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
