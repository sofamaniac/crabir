package com.sofamaniac.reboost.di

import android.content.Context
import androidx.room.Room
import com.sofamaniac.reboost.data.local.dao.AccountsDao
import com.sofamaniac.reboost.data.local.dao.VisitedCommunityDao
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.database.AppDatabase
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
    fun provideAccountsDao(database: AppDatabase): AccountsDao {
        return database.accountsDao()
    }

    @Provides
    fun provideVisitedPostsDao(database: AppDatabase): VisitedPostsDao {
        return database.visitedPostsDao()
    }

    @Provides
    fun provideVisitedCommunitiesDao(database: AppDatabase): VisitedCommunityDao {
        return database.visitedCommunityDao()
    }

}