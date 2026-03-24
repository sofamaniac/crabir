package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.AccountsRepositoryImplRoom
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {
//
//    @Provides
//    @Singleton
//    fun providesAccountsRepository(
//        accountsDao: AccountsDao
//    ): AccountsRepository {
//        return AccountsRepositoryImplRoom(accountsDao)
//
//    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountsModuleAbstract {

    @Binds
    @Singleton
    abstract fun bindsAccountsRepository(
        accountsRepository: AccountsRepositoryImplRoom
    ): AccountsRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
