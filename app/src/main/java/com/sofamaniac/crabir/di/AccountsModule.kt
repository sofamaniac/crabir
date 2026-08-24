package com.sofamaniac.crabir.di

import android.content.Context
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.AccountsRepositoryImpl
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

//
//import com.sofamaniac.crabir.domain.repository.AccountsRepository
//import com.sofamaniac.crabir.domain.repository.AccountsRepositoryImpl
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import jakarta.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class AccountsModuleAbstract {
//
////    @Binds
////    @Singleton
////    abstract fun bindsAccountsRepository(
////        accountsRepository: AccountsRepositoryImplRoom
////    ): AccountsRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindsAccountsRepository(
//        accountsRepository: AccountsRepositoryImpl
//    ): AccountsRepository
//}

@Module
@ComponentScan
@Configuration
class AccountsModule {
    @Single
    fun provideAccountsRepository(context: Context): AccountsRepository {
        return AccountsRepositoryImpl(context)
    }
}