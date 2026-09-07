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
    suspend fun getByName(name: String): CommunityViewEntity?

    @Query("SELECT * FROM visitedCommunity WHERE name = :name")
    fun getCommunityFlow(name: String): Flow<CommunityViewEntity?>

    @Upsert
    suspend fun upsert(entity: CommunityViewEntity)

    @Update
    suspend fun update(entity: CommunityViewEntity)
}

@Dao
interface SubredditDao : CommunityDao<SubredditData> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(community: SubredditData)

    @Upsert
    override fun upsert(entity: SubredditData)

    @Query("SELECT * FROM subreddits WHERE id = :id")
    suspend fun getById(id: String): SubredditData?

    @Query("SELECT * FROM subreddits WHERE name = :name LIMIT 1")
    override fun get(name: Fullname): Flow<SubredditData?>

    @Query("SELECT * FROM subreddits WHERE name = :name LIMIT 1")
    override suspend fun getAsync(name: Fullname): SubredditData?


    @Query("SELECT * FROM subreddits WHERE displayNamePrefixed = :slug")
    override suspend fun getBySlug(slug: String): SubredditData?

    @Query("SELECT * FROM subreddits WHERE userIsSubscriber = 1")
    suspend fun getSubscribed(): List<SubredditData>

    //    @Query("SELECT * FROM subreddits WHERE displayName = :displayName")
    //    suspend fun getBySlug(displayName: String): SubredditData?

    @Query("DELETE FROM subreddits WHERE name = :name")
    override suspend fun delete(name: Fullname)

    @Query("DELETE FROM subreddits")
    override fun deleteAll()
}

@Dao
interface MultiDao : CommunityDao<MultiData> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(community: MultiData)

    @Upsert
    override fun upsert(entity: MultiData)


    @Query("SELECT * FROM multireddits WHERE displayNamePrefixed = :slug")
    override suspend fun getBySlug(slug: String): MultiData?

    @Query("SELECT * FROM multireddits WHERE name = :name LIMIT 1")
    override fun get(name: Fullname): Flow<MultiData?>

    @Query("SELECT * FROM multireddits WHERE name = :name LIMIT 1")
    override suspend fun getAsync(name: Fullname): MultiData?


    @Query("SELECT * FROM multireddits")
    suspend fun getAll(): List<MultiData>

    @Query("DELETE FROM multireddits WHERE name = :name")
    override suspend fun delete(name: Fullname)

    @Query("DELETE FROM multireddits")
    override fun deleteAll()
}
