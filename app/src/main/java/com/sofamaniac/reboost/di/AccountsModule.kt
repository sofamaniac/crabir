package com.sofamaniac.reboost.di

import android.content.Context
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.AccountsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {

    @Provides
    @Singleton
    fun providesAccountsRepository(
        @ApplicationContext context: Context,
        @ApplicationScope coroutineScope: CoroutineScope,
    ): AccountsRepositoryImpl {
        return AccountsRepositoryImpl(
            context, coroutineScope
        )

    }

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
        accountsRepositoryImpl: AccountsRepositoryImpl
    ): AccountsRepository
}

annotation class ApplicationScope
