package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LinksRepository @Inject constructor(private val api: RedditAPIService) :
    VotableRepository(api) {
    suspend fun markNSFW(name: Fullname) {
        val post = cache.value[name] as? PostData?
        if (post == null) return
        val res = api.markNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = true))
        }
    }

    suspend fun unmarkNSFW(name: Fullname) {
        val post = cache.value[name] as? PostData?
        if (post == null) return
        val res = api.unmarkNSFW(name)
        if (res.isSuccessful) {
            update(name, post.copy(over18 = false))
        }
    }

    suspend fun unmarkSpoiler(name: Fullname) {
        val post = cache.value[name] as? PostData?
        if (post == null) return
        val res = api.unspoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = false))
        }
    }

    suspend fun markSpoiler(name: Fullname) {
        val post = cache.value[name] as? PostData?
        if (post == null) return
        val res = api.spoiler(name)
        if (res.isSuccessful) {
            update(name, post.copy(spoiler = true))
        }
    }

    suspend fun setInboxReplies(name: Fullname, enabled: Boolean) {
        val post = cache.value[name] as? PostData?
        if (post == null) return
        val res = api.setSendReplies(name, enabled)
        if (res.isSuccessful) {
            update(name, post.copy(sendReplies = enabled))
        }
    }
}