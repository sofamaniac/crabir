package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.domain.model.Fullname
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface CommunityViewRepository {
    suspend fun insert(community: CommunityViewEntity)

    suspend fun getByName(name: Fullname): CommunityViewEntity?

    fun getCommunityFlow(name: Fullname): Flow<CommunityViewEntity?>

    suspend fun upsert(entity: CommunityViewEntity)

    suspend fun update(entity: CommunityViewEntity)
}

class RoomRepository @Inject constructor(private val communityViewDao: CommunityViewDao) :
    CommunityViewRepository {
    override suspend fun insert(community: CommunityViewEntity) {
        communityViewDao.insert(community)
    }

    override suspend fun getByName(name: Fullname): CommunityViewEntity? {
        return communityViewDao.getByName(name)
    }

    override fun getCommunityFlow(name: Fullname): Flow<CommunityViewEntity?> {
        return communityViewDao.getCommunityFlow(name)
    }

    override suspend fun upsert(entity: CommunityViewEntity) {
        communityViewDao.upsert(entity)
    }

    override suspend fun update(entity: CommunityViewEntity) {
        communityViewDao.update(entity)
    }
}