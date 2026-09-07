package com.sofamaniac.crabir.data.local.dao

import com.sofamaniac.crabir.domain.model.CommunityData
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow

interface CommunityDao<T : CommunityData> {
    fun insert(community: T)

    fun upsert(entity: T)

    /**
     * @param slug the display name of the community
     */
    suspend fun getBySlug(slug: String): T?

    fun get(name: Fullname): Flow<T?>
    suspend fun getAsync(name: Fullname): T?

    suspend fun delete(name: Fullname)

    fun deleteAll()
}
