package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sofamaniac.crabir.data.local.entities.VisitedCommunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitedCommunityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(community: VisitedCommunityEntity)

    @Query("SELECT * FROM visitedCommunity WHERE id = :id")
    fun getCommunity(id: String): VisitedCommunityEntity?


    @Query("SELECT * FROM visitedCommunity WHERE id = :id")
    fun getCommunityFlow(id: String): Flow<VisitedCommunityEntity?>

    @Upsert
    suspend fun upsert(entity: VisitedCommunityEntity)
}
