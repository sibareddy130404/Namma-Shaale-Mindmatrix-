package com.nammashale.assetmanager.data.dao

import androidx.room.*
import com.nammashale.assetmanager.data.model.Asset
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Asset entity.
 *
 * All queries that return Flow are automatically observed —
 * when the database changes, the Flow emits the new data
 * and the UI updates automatically.
 *
 * Suspend functions run on a background thread via coroutines,
 * so the main (UI) thread never blocks.
 */
@Dao
interface AssetDao {

    /** Insert a new asset. Returns the generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: Asset): Long

    /** Update an existing asset. */
    @Update
    suspend fun update(asset: Asset)

    /** Delete an asset. */
    @Delete
    suspend fun delete(asset: Asset)

    /** Get all assets, ordered by newest first. Returns a Flow for live updates. */
    @Query("SELECT * FROM assets ORDER BY createdAt DESC")
    fun getAllAssets(): Flow<List<Asset>>

    /** Get a single asset by ID. */
    @Query("SELECT * FROM assets WHERE id = :assetId")
    fun getAssetById(assetId: Long): Flow<Asset?>

    /** Search assets by name or serial number. */
    @Query("SELECT * FROM assets WHERE name LIKE '%' || :query || '%' OR serialNo LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchAssets(query: String): Flow<List<Asset>>

    /** Filter assets by condition. */
    @Query("SELECT * FROM assets WHERE condition = :condition ORDER BY createdAt DESC")
    fun getAssetsByCondition(condition: String): Flow<List<Asset>>

    /** Count total assets. */
    @Query("SELECT COUNT(*) FROM assets")
    fun getTotalCount(): Flow<Int>

    /** Count assets by condition. */
    @Query("SELECT COUNT(*) FROM assets WHERE condition = :condition")
    fun getCountByCondition(condition: String): Flow<Int>
}
