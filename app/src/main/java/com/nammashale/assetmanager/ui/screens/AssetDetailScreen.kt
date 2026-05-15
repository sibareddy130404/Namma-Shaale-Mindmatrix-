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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.data.model.Issue
import com.nammashale.assetmanager.ui.components.ConditionChip
import com.nammashale.assetmanager.ui.theme.ConditionBroken
import com.nammashale.assetmanager.ui.theme.ConditionNeedsRepair
import com.nammashale.assetmanager.ui.theme.ConditionWorking
import com.nammashale.assetmanager.ui.viewmodel.AssetViewModel
import com.nammashale.assetmanager.ui.viewmodel.IssueViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    assetId: Long,
    onNavigateBack: () -> Unit,
    assetViewModel: AssetViewModel = viewModel(),
    issueViewModel: IssueViewModel = viewModel()
) {
    val context = LocalContext.current
    val asset by assetViewModel.getAssetById(assetId).collectAsStateWithLifecycle(initialValue = null)
    val issues by issueViewModel.getIssuesForAsset(assetId).collectAsStateWithLifecycle(initialValue = emptyList())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showConditionMenu by remember { mutableStateOf(false) }
    var showAddIssueDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    // Listen for events
    LaunchedEffect(Unit) {
        assetViewModel.saveEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message == "Asset deleted") onNavigateBack()
        }
    }
    LaunchedEffect(Unit) {
        issueViewModel.issueEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && asset != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Delete Asset?") },
            text = { Text("This will permanently delete \"${asset!!.name}\" and all associated issues. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = { assetViewModel.deleteAsset(asset!!); showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }

    // Add issue dialog
    if (showAddIssueDialog) {
        var issueDescription by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddIssueDialog = false },
            icon = { Icon(Icons.Default.BugReport, contentDescription = null) },
            title = { Text("Log Issue") },
            text = {
                OutlinedTextField(
                    value = issueDescription,
                    onValueChange = { issueDescription = it },
                    label = { Text("Describe the issue") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (issueDescription.isNotBlank()) {
                            issueViewModel.addIssue(assetId, issueDescription)
                            showAddIssueDialog = false
                        }
                    }
                ) { Text("Log Issue") }
            },
            dismissButton = { TextButton(onClick = { showAddIssueDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asset Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddIssueDialog = true },
                icon = { Icon(Icons.Default.BugReport, contentDescription = null) },
                text = { Text("Log Issue") },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        }
    ) { padding ->
        if (asset == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 30 }) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Asset header card
                    item {
                        AssetHeaderCard(asset = asset!!)
                    }

                    // Condition section
                    item {
                        ConditionSection(
                            asset = asset!!,
                            showConditionMenu = showConditionMenu,
                            onToggleMenu = { showConditionMenu = !showConditionMenu },
                            onConditionSelect = { condition ->
                                assetViewModel.updateCondition(asset!!, condition)
                                showConditionMenu = false
                            }
                        )
                    }

                    // Timestamps
                    item {
                        TimestampCard(asset = asset!!)
                    }

                    // Issues section header
                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Issues (${issues.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (issues.isEmpty()) {
                        item {
                            Card(
                                Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary)
                                        Spacer(Modifier.height(8.dp))
                                        Text("No issues logged", style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    } else {
                        items(issues, key = { it.id }) { issue ->
                            IssueCard(
                                issue = issue,
                                onResolve = { issueViewModel.resolveIssue(issue) },
                                onDelete = { issueViewModel.deleteIssue(issue) }
                            )
                        }
                    }

                    // Bottom spacer for FAB
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun AssetHeaderCard(asset: Asset) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    asset.name.take(2).uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(asset.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("SN: ${asset.serialNo}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(asset.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun ConditionSection(
    asset: Asset,
    showConditionMenu: Boolean,
    onToggleMenu: () -> Unit,
    onConditionSelect: (String) -> Unit
) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Current Condition", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                ConditionChip(condition = asset.condition)
            }
            Box {
                FilledTonalButton(onClick = onToggleMenu) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Change")
                }
                DropdownMenu(expanded = showConditionMenu, onDismissRequest = onToggleMenu) {
                    listOf("Working", "Needs Repair", "Broken").forEach { condition ->
                        DropdownMenuItem(
                            text = { Text(condition) },
                            onClick = { onConditionSelect(condition) },
                            leadingIcon = {
                                Box(
                                    Modifier.size(12.dp).clip(CircleShape).background(
                                        when (condition) {
                                            "Working" -> ConditionWorking
                                            "Needs Repair" -> ConditionNeedsRepair
                                            "Broken" -> ConditionBroken
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimestampCard(asset: Asset) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(8.dp))
                Text("Registered: ${dateFormat.format(Date(asset.createdAt))}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Update, contentDescription = null, modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(8.dp))
                Text("Last updated: ${dateFormat.format(Date(asset.updatedAt))}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun IssueCard(issue: Issue, onResolve: () -> Unit, onDelete: () -> Unit) {
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
                Text(
                    dateFormat.format(Date(issue.date)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (issue.resolved) {
                    Surface(shape = RoundedCornerShape(6.dp), color = ConditionWorking.copy(alpha = 0.15f)) {
                        Text("Resolved", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, color = ConditionWorking, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Surface(shape = RoundedCornerShape(6.dp), color = ConditionBroken.copy(alpha = 0.15f)) {
                        Text("Open", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, color = ConditionBroken, fontWeight = FontWeight.SemiBold)
                    }
                }
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
