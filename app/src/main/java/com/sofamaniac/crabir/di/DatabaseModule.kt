package com.sofamaniac.crabir.di

import android.content.Context
import androidx.room.Room
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.database.AccountDatabase
import com.sofamaniac.crabir.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reddit_app_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountDatabase(@ApplicationContext context: Context): AccountDatabase {
        return Room.databaseBuilder(
            context,
            AccountDatabase::class.java,
            "reddit_account_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideAccountsDao(database: AccountDatabase): AccountsDao {
        return database.accountsDao()
    }

    @Provides
    fun provideVisitedPostsDao(database: AppDatabase): VisitedPostsDao {
        return database.visitedPostsDao()
    }

    @Provides
    fun provideVisitedCommunitiesDao(database: AppDatabase): CommunityViewDao {
        return database.visitedCommunityDao()
    }

    @Provides
    fun provideVotableDao(database: AppDatabase): VotableDao {
        return database.votableDao()
    }

    @Provides
    fun subredditDao(database: AppDatabase): SubredditDao {
        return database.subredditDao()
    }

    @Provides
    fun multiDao(database: AppDatabase): MultiDao {
        return database.multiDao()
    }

}