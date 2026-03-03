package com.sofamaniac.reboost.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        ).addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3
        )
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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE posts_new (
                id TEXT PRIMARY KEY NOT NULL,
                post TEXT NOT NULL
            )
        """
        )
        db.execSQL("""DROP TABLE visitedPosts""")
        db.execSQL("""ALTER TABLE posts_new RENAME TO visitedPosts""")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `visitedCommunity` (
                `id` TEXT NOT NULL,
                `sort` TEXT NOT NULL,
                `timeframe` TEXT,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
    }
}