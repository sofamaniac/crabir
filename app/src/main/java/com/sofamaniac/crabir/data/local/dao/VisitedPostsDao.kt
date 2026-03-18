package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.domain.model.Fullname

@Dao
interface VisitedPostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: VisitedPostEntity)

    @Query("SELECT * FROM visitedPosts WHERE id = :id")
    fun getPost(id: Fullname): VisitedPostEntity?

    @Query("SELECT * FROM visitedPosts WHERE visitedAt < :before ORDER BY visitedAt DESC LIMIT 100")
    suspend fun getHistory(before: Long = System.currentTimeMillis()): List<VisitedPostEntity>
}
