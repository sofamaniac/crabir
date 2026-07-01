package com.sofamaniac.crabir.di

import android.content.Context
import androidx.room.Room
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.MultiRepository
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.database.AccountDatabase
import com.sofamaniac.crabir.data.local.database.AppDatabase
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@Configuration
object DatabaseModule {

    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reddit_app_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideAccountDatabase(context: Context): AccountDatabase {
        return Room.databaseBuilder(
            context,
            AccountDatabase::class.java,
            "reddit_account_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideAccountsDao(database: AccountDatabase): AccountsDao {
        return database.accountsDao()
    }

    @Singleton
    fun provideVisitedPostsDao(database: AppDatabase): VisitedPostsDao {
        return database.visitedPostsDao()
    }

    @Singleton
    fun provideVisitedCommunitiesDao(database: AppDatabase): CommunityViewDao {
        return database.visitedCommunityDao()
    }

    @Singleton
    fun provideVotableDao(database: AppDatabase): VotableDao {
        return database.votableDao()
    }

    @Singleton
    fun subredditDao(database: AppDatabase): SubredditRepository {
        return database.subredditDao()
    }

    @Singleton
    fun multiDao(database: AppDatabase): MultiRepository {
        return database.multiDao()
    }

}