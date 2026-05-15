package com.nammashale.assetmanager

import android.app.Application
import com.nammashale.assetmanager.data.database.AppDatabase
import com.nammashale.assetmanager.data.repository.AssetRepository

/**
 * Application class — lives for the entire app lifecycle.
 * Initializes the database and repository as lazy singletons.
 */
class NammaShaaleApp : Application() {

    /** Lazy-initialized database singleton. */
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    /** Lazy-initialized repository singleton. */
    val repository: AssetRepository by lazy {
        AssetRepository(database.assetDao(), database.issueDao())
    }

    override fun onCreate() {
        super.onCreate()
    }
}
