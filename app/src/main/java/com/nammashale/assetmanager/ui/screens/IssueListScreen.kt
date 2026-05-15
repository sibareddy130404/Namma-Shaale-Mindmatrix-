package com.nammashale.assetmanager.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.data.model.Issue
import com.nammashale.assetmanager.ui.theme.ConditionBroken
import com.nammashale.assetmanager.ui.theme.ConditionWorking
import com.nammashale.assetmanager.ui.viewmodel.IssueViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueListScreen(
    onNavigateBack: () -> Unit,
    issueViewModel: IssueViewModel = viewModel()
) {
    val context = LocalContext.current
    val allIssues by issueViewModel.allIssues.collectAsStateWithLifecycle()
    val unresolvedCount by issueViewModel.unresolvedCount.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = All, 1 = Open, 2 = Resolved
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }
    LaunchedEffect(Unit) {
        issueViewModel.issueEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val filteredIssues = when (selectedTab) {
        1 -> allIssues.filter { !it.resolved }
        2 -> allIssues.filter { it.resolved }
        else -> allIssues
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Issues", fontWeight = FontWeight.SemiBold)
                        Text("$unresolvedCount unresolved", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 30 }) {
            Column(Modifier.fillMaxSize().padding(padding)) {
                // Tab row for filtering
                TabRow(selectedTabIndex = selectedTab, modifier = Modifier.padding(horizontal = 16.dp),
                    containerColor = MaterialTheme.colorScheme.surface) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                        text = { Text("All (${allIssues.size})") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                        text = { Text("Open (${allIssues.count { !it.resolved }})") })
                    Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 },
                        text = { Text("Resolved (${allIssues.count { it.resolved }})") })
                }

                if (filteredIssues.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(
                                Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Text("No Issues", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                            Text("All clear! No issues have been reported.", style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredIssues, key = { it.id }) { issue ->
                            IssueListItem(
                                issue = issue,
                                onResolve = { issueViewModel.resolveIssue(issue) },
                                onDelete = { issueViewModel.deleteIssue(issue) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IssueListItem(issue: Issue, onResolve: () -> Unit, onDelete: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (issue.resolved)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        Modifier.size(8.dp).clip(CircleShape).background(
                            if (issue.resolved) ConditionWorking else ConditionBroken
                        )
                    )
                    Text("Asset #${issue.assetId}", style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                }
                Text(dateFormat.format(Date(issue.date)), style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Text(issue.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!issue.resolved) {
                    FilledTonalButton(onClick = onResolve, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Resolve", style = MaterialTheme.typography.labelSmall)
                    }
                }
                TextButton(onClick = onDelete, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.width(4.dp))
                    Text("Remove", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
