package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(community: CommunityViewEntity)

    @Query("SELECT * FROM visitedCommunity WHERE name = :name")
    suspend fun getByName(name: Fullname): CommunityViewEntity?

    @Query("SELECT * FROM visitedCommunity WHERE name = :name")
    fun getCommunityFlow(name: Fullname): Flow<CommunityViewEntity?>

    @Upsert
    suspend fun upsert(entity: CommunityViewEntity)

    @Update
    suspend fun update(entity: CommunityViewEntity)
}

interface CommunityDao<T> {
    suspend fun insert(community: T)

    suspend fun upsert(entity: T)

    suspend fun getByName(name: Fullname): T?

    suspend fun deleteAll()
}

@Dao
interface SubredditDao : CommunityDao<SubredditData> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(community: SubredditData)

    @Upsert
    override suspend fun upsert(entity: SubredditData)

    @Query("SELECT * FROM subreddits WHERE id = :id")
    suspend fun getById(id: String): SubredditData?


    @Query("SELECT * FROM subreddits WHERE name = :name")
    override suspend fun getByName(name: Fullname): SubredditData?

    @Query("SELECT * FROM subreddits WHERE userIsSubscriber = 1")
    suspend fun getSubscribed(): List<SubredditData>

    @Query("DELETE FROM subreddits")
    override suspend fun deleteAll()
}

@Dao
interface MultiDao : CommunityDao<MultiData> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(community: MultiData)

    @Upsert
    override suspend fun upsert(entity: MultiData)


    @Query("SELECT * FROM multireddits WHERE name = :name")
    override suspend fun getByName(name: Fullname): MultiData?

    @Query("SELECT * FROM multireddits")
    suspend fun getAll(): List<MultiData>

    @Query("DELETE FROM multireddits")
    override suspend fun deleteAll()
}
