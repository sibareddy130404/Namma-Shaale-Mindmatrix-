package com.nammashale.assetmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nammashale.assetmanager.data.dao.AssetDao
import com.nammashale.assetmanager.data.dao.IssueDao
import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.data.model.Issue

/**
 * The Room database for Namma Shaale.
 *
 * Uses the Singleton pattern — only ONE instance of the database exists
 * across the entire app. This prevents multiple connections which could
 * cause data corruption.
 *
 * @Database tells Room which entities (tables) to create.
 * version = 1 means this is the first version of our schema.
 */
@Database(
    entities = [Asset::class, Issue::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao
    abstract fun issueDao(): IssueDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Get the singleton database instance.
         *
         * @Volatile + synchronized ensures thread safety:
         * - @Volatile: All threads see the latest value of INSTANCE
         * - synchronized: Only one thread can create the database at a time
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_shaale_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
