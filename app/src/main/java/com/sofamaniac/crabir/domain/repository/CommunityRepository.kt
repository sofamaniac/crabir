package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.CommunityData
import com.sofamaniac.crabir.domain.model.Fullname

interface CommunityRepository<T : CommunityData> {
    suspend fun insert(community: T)

    suspend fun upsert(entity: T)

    suspend fun getByName(name: Fullname): T?

    suspend fun deleteAll()
}