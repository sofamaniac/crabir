package com.sofamaniac.reboost

import androidx.lifecycle.ViewModel
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val repository: AccountsRepository
) : ViewModel() {

    val activeAccount = repository.activeAccount
    val accounts = repository.accounts

    suspend fun addAccount(account: RedditAccount) {
        repository.addAccount(account)
    }
    suspend fun setActiveAccount(accountId: Int) {
        repository.setActiveAccount(accountId)
    }

}