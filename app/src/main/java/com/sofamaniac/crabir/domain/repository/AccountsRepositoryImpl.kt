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
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.CoroutineScope
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
import javax.inject.Singleton

@Serializable
data class Accounts(
    val accounts: List<RedditAccount>,
    val activeId: Int,
)


val Context.dataStore: DataStore<Accounts> by dataStore(
    fileName = "accounts.json",
    AccountsSerializer
)

object AccountsSerializer : Serializer<Accounts> {
    override val defaultValue: Accounts
        get() = Accounts(listOf(RedditAccount.anonymous()), -1)

    override suspend fun readFrom(input: InputStream): Accounts {
        try {
            return Json.decodeFromString<Accounts>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
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

@Singleton
class AccountsRepositoryImpl(
    context: Context,
    coroutineScope: CoroutineScope
) : AccountsRepository {
    private val dataStore: DataStore<Accounts> = context.dataStore
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
            if (activeId >= accounts.size || activeId < 0) {
                if (activeId >= accounts.size) {
                    Log.e("AccountsRepositoryImpl", "Invalid id: activeId: ${activeId}")
                }
                RedditAccount.anonymous()
            } else {
                Log.d("AccountsRepositoryImpl", "activeId: ${activeId}")
                accounts[activeId]
            }

        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = RedditAccount.anonymous()
        )

    override suspend fun addAccount(account: RedditAccount) {
        Log.d("AccountsRepositoryImpl", "addAccount: $account")
        dataStore.updateData { accounts ->
            if (accounts.accounts.any { it.username == account.username }) {
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
        dataStore.updateData { accounts ->
            accounts.copy(
                activeId = -1,
                accounts = accounts.accounts.filter { it.id != accountId && it.username.isNotEmpty() }
            )
        }
    }

    override suspend fun updateAccount(accountId: Int, account: RedditAccount) {
        dataStore.updateData { accounts ->
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

