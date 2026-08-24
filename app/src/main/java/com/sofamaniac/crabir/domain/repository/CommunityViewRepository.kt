package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

interface CommunityViewRepository {
    suspend fun insert(community: CommunityViewEntity)

    suspend fun getByName(name: String): CommunityViewEntity?

    fun getCommunityFlow(name: String): Flow<CommunityViewEntity?>

    suspend fun upsert(entity: CommunityViewEntity)

    suspend fun update(entity: CommunityViewEntity)
}

@Singleton(binds = [CommunityViewRepository::class])
class CommunityViewRepositoryImpl(private val communityViewDao: CommunityViewDao) :
    CommunityViewRepository {
    override suspend fun insert(community: CommunityViewEntity) {
        communityViewDao.insert(community)
    }

    override suspend fun getByName(name: String): CommunityViewEntity? {
        return communityViewDao.getByName(name)
    }

    override fun getCommunityFlow(name: String): Flow<CommunityViewEntity?> {
        return communityViewDao.getCommunityFlow(name)
    }

    override suspend fun upsert(entity: CommunityViewEntity) {
        communityViewDao.upsert(entity)
    }

    override suspend fun update(entity: CommunityViewEntity) {
        communityViewDao.update(entity)
    }
}