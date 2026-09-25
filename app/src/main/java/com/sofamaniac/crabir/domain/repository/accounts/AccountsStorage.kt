package com.sofamaniac.crabir.domain.repository.accounts

import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import net.openid.appauth.AuthState

interface AccountsStorage {
    val accounts: Flow<List<RedditAccount>>
    val activeAccount: Flow<RedditAccount>

    /** Negative until there is an active account */
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

class AccountsStoragePreview : AccountsStorage {
    override val accounts: Flow<List<RedditAccount>> = flowOf(listOf(RedditAccount.anonymous()))
    override val activeAccount: Flow<RedditAccount> = flowOf(RedditAccount.anonymous())
    override val activeAccountId: Flow<Int> = flowOf(-1)

    override suspend fun addAccount(account: RedditAccount) = Unit

    override suspend fun setActiveAccount(accountId: Int) = Unit

    override suspend fun deleteAccount(accountId: Int) = Unit

    override suspend fun updateAccount(
        accountId: Int,
        account: RedditAccount,
    ) = Unit

    override suspend fun updateAuthState(
        accountId: Int,
        authState: AuthState,
    ) = Unit

    override suspend fun clearAll() = Unit
}
