package com.nammashale.assetmanager.data.dao

import androidx.room.*
import com.nammashale.assetmanager.data.model.Issue
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Issue entity.
 */
@Dao
interface IssueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(issue: Issue): Long

    @Update
    suspend fun update(issue: Issue)

    @Delete
    suspend fun delete(issue: Issue)

    /** Get all issues for a specific asset. */
    @Query("SELECT * FROM issues WHERE assetId = :assetId ORDER BY date DESC")
    fun getIssuesForAsset(assetId: Long): Flow<List<Issue>>

    /** Get all unresolved issues across all assets. */
    @Query("SELECT * FROM issues WHERE resolved = 0 ORDER BY date DESC")
    fun getUnresolvedIssues(): Flow<List<Issue>>

    /** Get all issues. */
    @Query("SELECT * FROM issues ORDER BY date DESC")
    fun getAllIssues(): Flow<List<Issue>>

    /** Count unresolved issues. */
    @Query("SELECT COUNT(*) FROM issues WHERE resolved = 0")
    fun getUnresolvedCount(): Flow<Int>
}
