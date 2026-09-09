package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow

@Dao
interface VotableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(votable: VotableEntity)

    @Transaction
    suspend fun insert(votable: List<VotableEntity>) {
        for (v in votable) {
            insert(v)
        }
    }

    @Query("SELECT * FROM votableTable WHERE id = :id LIMIT 1")
    fun get(id: Fullname): Flow<VotableEntity?>

    @Query("SELECT * FROM votableTable WHERE id IN (:ids)")
    fun getMany(ids: List<Fullname>): Flow<List<VotableEntity>>

    @Query("SELECT * FROM votableTable WHERE id = :id LIMIT 1")
    suspend fun getValue(id: Fullname): VotableEntity?

    @Query("UPDATE votableTable SET data = :data WHERE id = :id")
    suspend fun update(id: Fullname, data: String)

    @Query("DELETE FROM votableTable WHERE id = :id")
    suspend fun delete(id: Fullname)

    @Query("DELETE FROM votableTable")
    suspend fun clear()
}
