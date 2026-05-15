package com.nammashale.assetmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammashale.assetmanager.NammaShaaleApp
import com.nammashale.assetmanager.data.model.Issue
import com.nammashale.assetmanager.data.repository.AssetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing issues logged against assets.
 */
class IssueViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AssetRepository =
        (application as NammaShaaleApp).repository

    /** All unresolved issues. */
    val unresolvedIssues: StateFlow<List<Issue>> = repository.unresolvedIssues
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** All issues. */
    val allIssues: StateFlow<List<Issue>> = repository.allIssues
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Unresolved issue count for the dashboard. */
    val unresolvedCount: StateFlow<Int> = repository.unresolvedIssueCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Get issues for a specific asset. */
    fun getIssuesForAsset(assetId: Long): Flow<List<Issue>> =
        repository.getIssuesForAsset(assetId)

    private val _issueEvent = MutableSharedFlow<String>()
    val issueEvent: SharedFlow<String> = _issueEvent.asSharedFlow()

    /** Log a new issue. */
    fun addIssue(assetId: Long, description: String) {
        viewModelScope.launch {
            val issue = Issue(
                assetId = assetId,
                description = description
            )
            repository.insertIssue(issue)
            _issueEvent.emit("Issue logged!")
        }
    }

    /** Mark an issue as resolved. */
    fun resolveIssue(issue: Issue) {
        viewModelScope.launch {
            repository.updateIssue(issue.copy(resolved = true))
            _issueEvent.emit("Issue resolved!")
        }
    }

    /** Delete an issue. */
    fun deleteIssue(issue: Issue) {
        viewModelScope.launch {
            repository.deleteIssue(issue)
            _issueEvent.emit("Issue removed")
        }
    }
}
