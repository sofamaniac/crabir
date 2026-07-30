package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inbox: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inbox: List<Message>)

    @Query("SELECT * FROM inboxTable WHERE name = :name LIMIT 1")
    fun get(name: Fullname): Flow<Message?>

    @Query("SELECT * FROM inboxTable WHERE name = :name LIMIT 1")
    suspend fun getValue(name: Fullname): Message?


    @Query("DELETE FROM inboxTable WHERE name = :name")
    fun delete(name: Fullname)

    @Upsert
    fun update(message: Message)

    @Upsert
    fun update(messages: List<Message>)

    @Query("SELECT * FROM inboxTable")
    suspend fun getAll(): List<Message>

    @Transaction
    suspend fun markAllRead() {
        val messages = getAll()
        update(messages = messages.map { it.copy(new = false) })
    }
}
