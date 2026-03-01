package com.sofamaniac.reboost.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofamaniac.reboost.data.local.entities.VisitedCommunityEntity

@Dao
interface VisitedCommunityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(community: VisitedCommunityEntity)

    @Query("SELECT * FROM visitedCommunity WHERE id = :id")
    fun getCommunity(id: String): VisitedCommunityEntity?
}
