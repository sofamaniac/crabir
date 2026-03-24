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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.util.fastFirstOrNull
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.entities.toDomainModel
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.domain.model.RedditAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream
import java.util.Collections.emptyList
import javax.inject.Inject
import javax.inject.Singleton

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
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }

}

@Composable
fun rememberCurrentAccount(viewModel: CurrentAccountViewModel = hiltViewModel()): RedditAccount {
    val account by viewModel.account.collectAsState(RedditAccount.anonymous())
    return account
}

@HiltViewModel
class CurrentAccountViewModel @Inject constructor(accountsDao: AccountsDao) : ViewModel() {
    val account =
        accountsDao.getActiveAccount().map { it?.toDomainModel() ?: RedditAccount.anonymous() }
}

@Singleton
class AccountsRepositoryImplRoom @Inject constructor(
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
        account: RedditAccount
    ) {
        val newEntity = account.toEntity()
        accountsDao.updateAccount(accountId, newEntity)
    }

    override suspend fun updateAuthState(
        accountId: Int,
        authState: AuthState
    ) {
        val authState = Json.encodeToString(authState)
        accountsDao.updateAuthState(accountId, authState)
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

}

//@Singleton
class AccountsRepositoryImpl(
    context: Context,
    coroutineScope: CoroutineScope
) : AccountsRepository {
    private val dataStore: DataStore<Accounts> = context.accountsDataStore
    private val accountsData: StateFlow<Accounts> = dataStore.data.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = Accounts(emptyList(), -1)
    )

    override val accounts: StateFlow<List<RedditAccount>> = accountsData.map {
        Log.d("AccountsRepositoryImpl", "accounts: ${it.accounts}")
        it.accounts
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    override val activeAccountId: StateFlow<Int> = accountsData.map { accounts ->
        Log.d("AccountsRepositoryImpl", "activeId: ${accounts.activeId}")
        accounts.activeId
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = -1
    )


    override val activeAccount: StateFlow<RedditAccount> =
        accounts.combine(activeAccountId) { accounts, activeId ->
            accounts.firstOrNull { account -> account.id == activeId }
                ?: RedditAccount.anonymous()
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = RedditAccount.anonymous()
        )

    override suspend fun addAccount(account: RedditAccount) {
        Log.d("AccountsRepositoryImpl", "addAccount: $account")
        dataStore.updateData { accounts ->
            if (accounts.accounts.any { (it.info?.name ?: "") == account.info?.name }) {
                Log.e("AccountsRepositoryImpl", "Account already exists: $account")
                accounts
            } else {
                accounts.copy(accounts = accounts.accounts + account)
            }
        }
    }

    override suspend fun setActiveAccount(accountId: Int) {
        Log.d("AccountsRepositoryImpl", "setActiveAccount: $accountId")
        dataStore.updateData { accounts ->
            accounts.copy(activeId = accountId)
        }
    }

    override suspend fun deleteAccount(accountId: Int) {
        if (accountId == -1) {
            return
        }
        Log.d("AccountsRepositoryImpl", "deleteAccount: $accountId")
        dataStore.updateData { accounts ->
            accounts.copy(
                activeId = -1,
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
            Accounts(emptyList(), -1)
        }
    }
}

