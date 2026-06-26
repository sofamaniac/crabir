package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.AccountsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountsModuleAbstract {

//    @Binds
//    @Singleton
//    abstract fun bindsAccountsRepository(
//        accountsRepository: AccountsRepositoryImplRoom
//    ): AccountsRepository

    @Binds
    @Singleton
    abstract fun bindsAccountsRepository(
        accountsRepository: AccountsRepositoryImpl
    ): AccountsRepository
}
