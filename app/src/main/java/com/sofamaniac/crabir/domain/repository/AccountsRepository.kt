package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.AuthState

interface AccountsRepository {
    val accounts: Flow<List<RedditAccount>>
    val activeAccount: Flow<RedditAccount>
    val activeAccountId: Flow<Int>

    suspend fun addAccount(account: RedditAccount)
    suspend fun setActiveAccount(accountId: Int)

    /** Remove account from the list. Also remove all uninitialized accounts. */
    suspend fun deleteAccount(accountId: Int)

    suspend fun updateAccount(accountId: Int, account: RedditAccount)

    //suspend fun refreshToken(accountId: Int)
    suspend fun updateAuthState(accountId: Int, authState: AuthState)
    suspend fun clearAll()
}