package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

class AccountsRepositoryPreview : AccountsRepository {
    override val accounts: Flow<List<RedditAccount>> = flowOf(listOf(RedditAccount.anonymous()))
    override val activeAccount: Flow<RedditAccount> = flowOf(RedditAccount.anonymous())
    override val activeAccountId: Flow<Int> = flowOf(-1)

    override suspend fun addAccount(account: RedditAccount) {
    }

    override suspend fun setActiveAccount(accountId: Int) {
    }

    override suspend fun deleteAccount(accountId: Int) {
    }

    override suspend fun updateAccount(
        accountId: Int,
        account: RedditAccount,
    ) {
    }

    override suspend fun updateAuthState(
        accountId: Int,
        authState: AuthState,
    ) {
    }

    override suspend fun clearAll() {
    }
}