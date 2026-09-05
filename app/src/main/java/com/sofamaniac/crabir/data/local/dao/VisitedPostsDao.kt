package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitedPostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: VisitedPostEntity)

    @Query("SELECT * FROM visitedPosts WHERE id = :id")
    suspend fun getPost(id: Fullname): VisitedPostEntity?

    @Query("SELECT votableTable.* FROM votableTable INNER JOIN visitedPosts ON votableTable.id = visitedPosts.id  WHERE visitedPosts.visitedAt < :before AND visitedBy = :visitedBy ORDER BY visitedPosts.visitedAt DESC LIMIT 100")
    suspend fun getHistory(
        before: Long = System.currentTimeMillis(),
        visitedBy: Int,
    ): List<VotableEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM visitedPosts WHERE id = :id)")
    fun contains(id: Fullname): Flow<Boolean>

    @Query("SELECT votableTable.* FROM votableTable INNER JOIN visitedPosts ON votableTable.id = visitedPosts.id  WHERE visitedAt < :before ORDER BY visitedAt DESC LIMIT 100")
    suspend fun getHistory(before: Long = System.currentTimeMillis()): List<VotableEntity>

    @Query("SELECT votableTable.id FROM votableTable INNER JOIN visitedPosts ON votableTable.id = visitedPosts.id  WHERE visitedAt < :before ORDER BY visitedAt DESC LIMIT 100")
    fun getHistoryFlow(before: Long = System.currentTimeMillis()): Flow<List<Fullname>>

    @Query("SELECT * from visitedPosts WHERE visitedAt < :before ORDER BY visitedAt DESC LIMIT 100")
    suspend fun getHistoryIds(before: Long = System.currentTimeMillis()): List<VisitedPostEntity>

    @Query("DELETE FROM visitedPosts")
    suspend fun clearAll()
}
