package com.nammashale.assetmanager.data.repository

import com.nammashale.assetmanager.data.dao.AssetDao
import com.nammashale.assetmanager.data.dao.IssueDao
import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.data.model.Issue
import kotlinx.coroutines.flow.Flow

/**
 * Repository — the SINGLE SOURCE OF TRUTH for all data operations.
 *
 * WHY DO WE USE THE REPOSITORY PATTERN?
 * =======================================
 * 1. SEPARATION OF CONCERNS: The ViewModel doesn't need to know *how* data
 *    is stored (Room? API? File?). It just calls repository methods.
 *
 * 2. EASY TO SWAP: If you later add a cloud database or REST API, you only
 *    change this class — not every ViewModel in the app.
 *
 * 3. TESTABILITY: In tests, you can create a fake repository that returns
 *    test data without needing a real database.
 *
 * 4. SINGLE ENTRY POINT: All data flows through here, making it easy to
 *    add caching, logging, or data transformation in one place.
 *
 * SIMPLE FLOW:
 *   UI (Compose) → ViewModel → Repository → DAO → Room (SQLite)
 *   UI ← ViewModel ← Repository ← DAO ← Room (Flow auto-updates)
 */
class AssetRepository(
    private val assetDao: AssetDao,
    private val issueDao: IssueDao
) {
    // ========== ASSET OPERATIONS ==========

    /** Get all assets as a live Flow. */
    val allAssets: Flow<List<Asset>> = assetDao.getAllAssets()

    /** Get total asset count. */
    val totalAssetCount: Flow<Int> = assetDao.getTotalCount()

    /** Get count by condition. */
    fun countByCondition(condition: String): Flow<Int> = assetDao.getCountByCondition(condition)

    /** Get a single asset by ID. */
    fun getAssetById(id: Long): Flow<Asset?> = assetDao.getAssetById(id)

    /** Search assets by name or serial number. */
    fun searchAssets(query: String): Flow<List<Asset>> = assetDao.searchAssets(query)

    /** Filter assets by condition. */
    fun getAssetsByCondition(condition: String): Flow<List<Asset>> =
        assetDao.getAssetsByCondition(condition)

    /** Insert a new asset. Returns the generated ID. */
    suspend fun insertAsset(asset: Asset): Long = assetDao.insert(asset)

    /** Update an existing asset. */
    suspend fun updateAsset(asset: Asset) = assetDao.update(asset)

    /** Delete an asset. */
    suspend fun deleteAsset(asset: Asset) = assetDao.delete(asset)

    // ========== ISSUE OPERATIONS ==========

    /** Get all issues for a specific asset. */
    fun getIssuesForAsset(assetId: Long): Flow<List<Issue>> = issueDao.getIssuesForAsset(assetId)

    /** Get all unresolved issues. */
    val unresolvedIssues: Flow<List<Issue>> = issueDao.getUnresolvedIssues()

    /** Get all issues. */
    val allIssues: Flow<List<Issue>> = issueDao.getAllIssues()

    /** Count of unresolved issues. */
    val unresolvedIssueCount: Flow<Int> = issueDao.getUnresolvedCount()

    /** Insert a new issue. */
    suspend fun insertIssue(issue: Issue): Long = issueDao.insert(issue)

    /** Update an issue. */
    suspend fun updateIssue(issue: Issue) = issueDao.update(issue)

    /** Delete an issue. */
    suspend fun deleteIssue(issue: Issue) = issueDao.delete(issue)
}
