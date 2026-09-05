package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.CommunityData

interface CommunityRepository<T : CommunityData> {
    suspend fun insert(community: T)

    suspend fun upsert(entity: T)

    /**
     * @param slug the display name of the community
     */
    suspend fun getBySlug(slug: String): T?

    suspend fun deleteAll()
}
