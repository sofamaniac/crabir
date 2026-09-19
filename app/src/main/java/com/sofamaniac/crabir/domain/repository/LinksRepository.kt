package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.into
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.map
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
) : LinksRepository {
    private suspend inline fun updatePost(
        name: Fullname,
        update: (PostData) -> Result<Unit>,
    ): Result<Unit> {
        return get(name).first()
            .map(default = { Result.failure(PostNotFoundException(name)) }, map = update)
    }

    override suspend fun markNSFW(name: Fullname): Result<Unit> {
        return updatePost(name) { post ->
            api.markNSFW(name).onSuccess { update(post.copy(over18 = true)) }
        }
    }

    override suspend fun unmarkNSFW(name: Fullname): Result<Unit> {
        return updatePost(name) { post ->
            api.unmarkNSFW(name).onSuccess { update(post.copy(over18 = false)) }
        }
    }

    override suspend fun unmarkSpoiler(name: Fullname): Result<Unit> {
        return updatePost(name) { post ->
            api.unspoiler(name).onSuccess { update(post.copy(spoiler = false)) }
        }
    }

    override suspend fun markSpoiler(name: Fullname): Result<Unit> {
        return updatePost(name) { post ->
            api.spoiler(name).onSuccess { update(post.copy(spoiler = true)) }
        }
    }

    override suspend fun editFlair(name: Fullname, flairId: String, text: String?): Result<Unit> {
        return updatePost(name) { post ->
            val subreddit = post.subreddit.name
            api.selectFlair("r/$subreddit", name, flairId, text ?: "").onSuccess {
                val oldFlair = post.linkFlair
                update(post.copy(linkFlair = oldFlair.copy(text = text ?: oldFlair.text)))
            }
        }
    }

    override suspend fun getFlairs(name: Fullname): Result<List<FlairInfo>> {
        return get(name).first()
            .map(default = { Result.failure(PostNotFoundException(name)) }) { post ->
                val subreddit = post.subreddit.name
                api.getPostFlair("r/$subreddit")
            }
    }

    override suspend fun setInboxReplies(name: Fullname, enabled: Boolean): Result<Unit> {
        return updatePost(name) { post ->
            api.setSendReplies(name, enabled).onSuccess {
                update(post.copy(sendReplies = enabled))
            }
        }
    }

    override suspend fun hide(name: Fullname): Result<Unit> {
        return api.hide(name).onSuccess {
            get(name).first()
                .map(default = { Result.failure<Unit>(PostNotFoundException(name)) }) { post ->
                    val relationship = post.relationship.copy(hidden = true)
                    votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
                    Result.success(Unit)
                }
        }
    }

    override suspend fun unhide(name: Fullname): Result<Unit> {
        return api.unhide(name).onSuccess {
            get(name).first()
                .map(default = { Result.failure<Unit>(PostNotFoundException(name)) }) { post ->
                    val relationship = post.relationship.copy(hidden = false)
                    votableDao.update(name, post.copy(relationship = relationship).toEntity().data)
                    Result.success(Unit)
                }
        }
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
