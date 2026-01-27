package com.sofamaniac.reboost.data.repository

import com.sofamaniac.reboost.domain.model.RedditAccount
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.AuthState

interface AccountsRepository {
    val accounts: StateFlow<List<RedditAccount>>
    val activeAccount: StateFlow<RedditAccount>
    val activeAccountId: StateFlow<Int>

    suspend fun addAccount(account: RedditAccount)
    suspend fun setActiveAccount(accountId: Int)
    suspend fun deleteAccount(accountId: Int)

    //suspend fun refreshToken(accountId: Int)
    suspend fun updateAuthState(accountId: Int, authState: AuthState)
    suspend fun clearAll()
}