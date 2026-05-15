package com.nammashale.assetmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammashale.assetmanager.NammaShaaleApp
import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.data.repository.AssetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * AssetViewModel — the BRIDGE between UI and Data.
 *
 * HOW IT WORKS (beginner-friendly):
 * ==================================
 * 1. The ViewModel holds "state" (data the UI needs to display).
 * 2. Compose screens OBSERVE this state via StateFlow.
 * 3. When the user does something (e.g. taps "Save"), the ViewModel
 *    calls a Repository method inside a COROUTINE.
 * 4. A coroutine is like a "pausable task" — it runs the database
 *    operation in the background so the UI doesn't freeze.
 * 5. viewModelScope.launch { } starts a coroutine that automatically
 *    cancels when the screen is destroyed (no memory leaks!).
 * 6. When Room saves/updates data, the Flow automatically emits new
 *    values → StateFlow updates → Compose recomposes → UI shows new data.
 *
 * FLOW DIAGRAM:
 *   User taps Save → ViewModel.saveAsset() → viewModelScope.launch →
 *   repository.insertAsset() → DAO.insert() → SQLite → Done!
 *   Meanwhile: Room Flow detects change → emits new list →
 *   StateFlow updates → Compose re-renders the list
 */
class AssetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AssetRepository =
        (application as NammaShaaleApp).repository

    // ========== STATE FLOWS (UI observes these) ==========

    /** All assets, updated automatically when the database changes. */
    val allAssets: StateFlow<List<Asset>> = repository.allAssets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Total asset count for the dashboard. */
    val totalCount: StateFlow<Int> = repository.totalAssetCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Working assets count. */
    val workingCount: StateFlow<Int> = repository.countByCondition("Working")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Needs Repair count. */
    val needsRepairCount: StateFlow<Int> = repository.countByCondition("Needs Repair")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Broken count. */
    val brokenCount: StateFlow<Int> = repository.countByCondition("Broken")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ========== SEARCH & FILTER ==========

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    /** Filtered/searched asset list. */
    val filteredAssets: StateFlow<List<Asset>> = combine(
        allAssets,
        _searchQuery,
        _selectedFilter
    ) { assets, query, filter ->
        var result = assets
        if (query.isNotBlank()) {
            result = result.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.serialNo.contains(query, ignoreCase = true)
            }
        }
        if (filter != "All") {
            result = result.filter { it.condition == filter }
        }
        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    // ========== ONE-TIME EVENTS ==========

    private val _saveEvent = MutableSharedFlow<String>()
    val saveEvent: SharedFlow<String> = _saveEvent.asSharedFlow()

    // ========== ACTIONS ==========

    /**
     * Save a new asset to the database.
     *
     * viewModelScope.launch starts a coroutine:
     * - Runs on a background thread (doesn't freeze the UI)
     * - Automatically cancels if the ViewModel is destroyed
     */
    fun saveAsset(
        name: String,
        serialNo: String,
        category: String,
        condition: String,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            val asset = Asset(
                name = name,
                serialNo = serialNo,
                category = category,
                condition = condition,
                photoPath = photoPath
            )
            repository.insertAsset(asset)
            _saveEvent.emit("Asset Saved!")
        }
    }

    /** Update an existing asset's condition. */
    fun updateCondition(asset: Asset, newCondition: String) {
        viewModelScope.launch {
            val updated = asset.copy(
                condition = newCondition,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateAsset(updated)
            _saveEvent.emit("Condition updated to $newCondition")
        }
    }

    /** Delete an asset. */
    fun deleteAsset(asset: Asset) {
        viewModelScope.launch {
            repository.deleteAsset(asset)
            _saveEvent.emit("Asset deleted")
        }
    }

    /** Get a single asset by ID. */
    fun getAssetById(id: Long): Flow<Asset?> = repository.getAssetById(id)
}
