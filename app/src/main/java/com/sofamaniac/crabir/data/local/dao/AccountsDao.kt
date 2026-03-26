package com.sofamaniac.crabir.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sofamaniac.crabir.data.local.entities.RedditAccountEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountsDao {
    @Query("SELECT * FROM accounts")
    fun getAll(): Flow<List<RedditAccountEntity>>

    @Query("SELECT * FROM accounts WHERE isActive = 1 LIMIT 1")
    fun getActiveAccount(): Flow<RedditAccountEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: RedditAccountEntity)

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    fun get(accountId: Int): RedditAccountEntity?


    @Query("UPDATE accounts SET isActive = 0 WHERE isActive = 1")
    fun deactivateAll()

    @Query("UPDATE accounts SET isActive = 1 WHERE id = :accountId")
    fun activate(accountId: Int)

    @Transaction
    fun setActiveAccount(accountId: Int) {
        deactivateAll()
        activate(accountId)
    }

    @Query("SELECT * FROM accounts WHERE name = :name LIMIT 1")
    fun getByName(name: String): RedditAccountEntity?


    @Transaction
    fun updateAccount(accountId: Int, account: RedditAccountEntity) {
        //  If there is already an account with the same name, delete it
        val candidate = getByName(account.name)
        if (candidate?.id != null && candidate.id != accountId) {
            delete(candidate.id)
        }
        updateName(accountId, account.name)
        updateInfo(accountId, account.info)
        updateAuthState(accountId, account.authState)
    }

    @Query("UPDATE accounts SET name = :name WHERE id = :accountId")
    fun updateName(accountId: Int, name: String)

    @Query("UPDATE accounts SET info = :info WHERE id = :accountId")
    fun updateInfo(accountId: Int, info: String)

    @Query("UPDATE accounts SET authState = :authState WHERE id = :accountId")
    fun updateAuthState(accountId: Int, authState: String)

    @Query("DELETE FROM accounts WHERE id = :accountId")
    fun delete(accountId: Int)


    @Delete
    fun delete(account: RedditAccountEntity)
}