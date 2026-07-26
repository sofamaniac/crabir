/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:45 PM
 *
 */

package com.sofamaniac.crabir.domain.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.util.fastFirstOrNull
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.entities.toDomainModel
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.domain.model.AuthStateSerializer
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthState
import org.koin.core.annotation.Singleton
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class Accounts(
    val accounts: List<RedditAccount>,
    val activeId: Int,
) {
    fun getCurrent(): RedditAccount {
        return if (activeId < 0) {
            RedditAccount.anonymous()
        } else {
            accounts.firstOrNull { it.id == activeId }
                ?: accounts.lastOrNull()
                ?: RedditAccount.anonymous()
        }
    }
}


val Context.accountsDataStore: DataStore<Accounts> by dataStore(
    fileName = "accounts.json",
    AccountsSerializer
)

object AccountsSerializer : Serializer<Accounts> {
    override val defaultValue: Accounts
        get() = Accounts(listOf(RedditAccount.anonymous()), -1)

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun readFrom(input: InputStream): Accounts {
        try {
            return json.decodeFromString<Accounts>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            Log.e("AccountsSerializer", "Error reading Settings", serialization)
            return defaultValue
        }
    }

    override suspend fun writeTo(
        t: Accounts,
        output: OutputStream,
    ) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }

}


@Singleton
class AccountsRepositoryImplRoom(
    private val accountsDao: AccountsDao,
) : AccountsRepository {
    override val accounts: Flow<List<RedditAccount>> =
        accountsDao.getAll().map { it.map { account -> account.toDomainModel() } }
    override val activeAccount: Flow<RedditAccount> =
        accountsDao.getActiveAccount().map { it?.toDomainModel() ?: RedditAccount.anonymous() }
    override val activeAccountId: Flow<Int> = accountsDao.getActiveAccount().map {
        it?.id ?: -1
    }

    override suspend fun addAccount(account: RedditAccount) {
        val entity = account.toEntity()
        accountsDao.insert(entity)
    }

    override suspend fun setActiveAccount(accountId: Int) {
        accountsDao.setActiveAccount(accountId)
    }

    override suspend fun deleteAccount(accountId: Int) {
        accountsDao.delete(accountId)
    }

    override suspend fun updateAccount(
        accountId: Int,
        account: RedditAccount,
    ) {
        val newEntity = account.toEntity()
        accountsDao.updateAccount(accountId, newEntity)
    }

    override suspend fun updateAuthState(
        accountId: Int,
        authState: AuthState,
    ) {
        val authState = Json.encodeToString(AuthStateSerializer, authState)
        accountsDao.updateAuthState(accountId, authState)
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

}

@Singleton(binds = [AccountsRepository::class])
class AccountsRepositoryImpl(
    context: Context,
) : AccountsRepository {


    private val dataStore: DataStore<Accounts> = context.accountsDataStore
    private val accountsData: Flow<Accounts> = dataStore.data

    override val accounts: Flow<List<RedditAccount>> = accountsData.map {
        Log.d("AccountsRepositoryImpl", "accounts: ${it.accounts}")
        it.accounts
    }

    override val activeAccountId: Flow<Int> = accountsData.map { accounts ->
        Log.d("AccountsRepositoryImpl", "activeId: ${accounts.activeId}")
        accounts.activeId
    }


    override val activeAccount: Flow<RedditAccount> =
        accounts.combine(activeAccountId) { accounts, activeId ->
            accounts.firstOrNull { account -> account.id == activeId }
                ?: RedditAccount.anonymous()
        }

    override suspend fun addAccount(account: RedditAccount) {
        Log.d("AccountsRepositoryImpl", "addAccount: $account")
        dataStore.updateData { accounts ->
            if (accounts.accounts.any {
                    (it.info?.name ?: "") == account.info?.name || it.id == account.id
                }) {
                Log.w("AccountsRepositoryImpl", "Account already exists: $account")
                val i = accounts.accounts.indexOfFirst {
                    (it.info?.name ?: "") == account.info?.name || it.id == account.id
                }
                val accountsList = accounts.accounts.toMutableList()
                accountsList[i] = account
                accounts.copy(accounts = accountsList)
            } else {
                accounts.copy(accounts = accounts.accounts + account)
            }
        }
    }

    override suspend fun setActiveAccount(accountId: Int) {
        if (activeAccountId.first() == accountId) return
        Log.d("AccountsRepositoryImpl", "setActiveAccount: $accountId")
        dataStore.updateData { accounts ->
            accounts.copy(activeId = accountId)
        }
    }

    override suspend fun deleteAccount(accountId: Int) {
        Log.d("AccountsRepositoryImpl", "deleteAccount: $accountId")
        dataStore.updateData { accounts ->
            accounts.copy(
                activeId = if (accountId == accounts.activeId) -1 else accounts.activeId,
                accounts = accounts.accounts.filter { it.id != accountId }
            )
        }
    }

    override suspend fun updateAccount(accountId: Int, account: RedditAccount) {
        dataStore.updateData { accounts ->
            val duplicateIndex =
                accounts.accounts.indexOfFirst { it.info?.username == account.info?.username }
            if (duplicateIndex != -1 && duplicateIndex != accountId) {
                Log.e("AccountsRepositoryImpl", "Account already exists: $account")
                val accountsList = accounts.accounts.toMutableList()
                accountsList[duplicateIndex] = account

                return@updateData accounts.copy(
                    accounts = accountsList.filter { it.id != accountId }
                )
            }
            val accountIndex = accounts.accounts.indexOfFirst { it.id == accountId }
            if (accountIndex != -1) {
                val accountsList = accounts.accounts.toMutableList()
                accountsList[accountIndex] = account
                accounts.copy(accounts = accountsList)
            } else {
                accounts
            }
        }
    }

    override suspend fun updateAuthState(accountId: Int, authState: AuthState) {
        Log.d("AccountsRepositoryImpl", "updateAuthState: $accountId")
        dataStore.updateData { accounts ->
            var account = accounts.accounts.fastFirstOrNull { it.id == accountId }
            if (account != null) {
                val accountIndex = accounts.accounts.indexOf(account)
                account = account.copy(auth = authState)
                val accountsList = accounts.accounts.toMutableList()
                accountsList[accountIndex] = account
                accounts.copy(accounts = accountsList)
            } else {
                accounts
            }
        }

    }

    override suspend fun clearAll() {
        dataStore.updateData {
            AccountsSerializer.defaultValue
        }
    }
}

