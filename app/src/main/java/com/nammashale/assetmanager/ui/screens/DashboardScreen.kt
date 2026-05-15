package com.nammashale.assetmanager.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.ui.theme.ConditionBroken
import com.nammashale.assetmanager.ui.theme.ConditionNeedsRepair
import com.nammashale.assetmanager.ui.theme.ConditionWorking
import com.nammashale.assetmanager.ui.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToAssetList: () -> Unit,
    onNavigateToIssues: () -> Unit,
    onNavigateToReports: () -> Unit,
    assetViewModel: AssetViewModel = viewModel()
) {
    val totalCount by assetViewModel.totalCount.collectAsStateWithLifecycle()
    val workingCount by assetViewModel.workingCount.collectAsStateWithLifecycle()
    val needsRepairCount by assetViewModel.needsRepairCount.collectAsStateWithLifecycle()
    val brokenCount by assetViewModel.brokenCount.collectAsStateWithLifecycle()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Namma Shaale", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Asset Management Dashboard", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToRegister,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Asset") },
                text = { Text("Register Asset") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { -40 }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardStatCard(Modifier.weight(1f), "Total Assets", totalCount.toString(), Icons.Default.Inventory2,
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer))
                        DashboardStatCard(Modifier.weight(1f), "Working", workingCount.toString(), Icons.Default.CheckCircle,
                            listOf(ConditionWorking, ConditionWorking.copy(alpha = 0.6f)))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardStatCard(Modifier.weight(1f), "Needs Repair", needsRepairCount.toString(), Icons.Default.Build,
                            listOf(ConditionNeedsRepair, ConditionNeedsRepair.copy(alpha = 0.6f)))
                        DashboardStatCard(Modifier.weight(1f), "Broken", brokenCount.toString(), Icons.Default.Warning,
                            listOf(ConditionBroken, ConditionBroken.copy(alpha = 0.6f)))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 40 }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    QuickActionButton("View All Assets", Icons.Default.List, onNavigateToAssetList)
                    QuickActionButton("Reported Issues", Icons.Default.Warning, onNavigateToIssues)
                    QuickActionButton("View Reports", Icons.Default.Inventory2, onNavigateToReports)
                }
            }
        }
    }
}

@Composable
private fun DashboardStatCard(modifier: Modifier, title: String, count: String, icon: ImageVector, gradientColors: List<Color>) {
    Card(modifier = modifier.height(120.dp), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Box(Modifier.fillMaxSize().background(brush = Brush.linearGradient(gradientColors)).padding(16.dp)) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Icon(icon, contentDescription = title, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(28.dp))
                Column {
                    Text(count, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

@Composable
private fun QuickActionButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp))
            Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }
    }
}
