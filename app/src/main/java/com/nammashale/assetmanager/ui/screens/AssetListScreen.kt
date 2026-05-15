package com.nammashale.assetmanager.ui.screens

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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.data.model.Asset
import com.nammashale.assetmanager.ui.theme.ConditionBroken
import com.nammashale.assetmanager.ui.theme.ConditionNeedsRepair
import com.nammashale.assetmanager.ui.theme.ConditionWorking
import com.nammashale.assetmanager.ui.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListScreen(
    onNavigateBack: () -> Unit,
    onAssetClick: (Long) -> Unit,
    assetViewModel: AssetViewModel = viewModel()
) {
    val assets by assetViewModel.filteredAssets.collectAsStateWithLifecycle()
    val searchQuery by assetViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by assetViewModel.selectedFilter.collectAsStateWithLifecycle()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Assets", fontWeight = FontWeight.SemiBold) },
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { assetViewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search assets...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp), singleLine = true
                )

                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Working", "Needs Repair", "Broken").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { assetViewModel.updateFilter(filter) },
                            label = { Text(filter, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (filter) {
                                    "Working" -> ConditionWorking.copy(alpha = 0.15f)
                                    "Needs Repair" -> ConditionNeedsRepair.copy(alpha = 0.15f)
                                    "Broken" -> ConditionBroken.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            )
                        )
                    }
                }

                if (assets.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Text("No Assets Yet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                            Text("Register your first asset to see it listed here.", style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(assets, key = { it.id }) { asset ->
                            AssetListItem(asset = asset, onClick = { onAssetClick(asset.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetListItem(asset: Asset, onClick: () -> Unit) {
    val conditionColor = when (asset.condition) {
        "Working" -> ConditionWorking
        "Needs Repair" -> ConditionNeedsRepair
        "Broken" -> ConditionBroken
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Text(asset.name.take(2).uppercase(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(asset.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("SN: ${asset.serialNo}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(asset.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = conditionColor.copy(alpha = 0.15f)) {
                Text(asset.condition, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall, color = conditionColor, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
