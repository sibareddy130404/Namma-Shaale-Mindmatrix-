package com.nammashale.assetmanager.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.ui.viewmodel.AssetViewModel
import com.nammashale.assetmanager.ui.viewmodel.IssueViewModel
import com.nammashale.assetmanager.util.ReportGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onNavigateBack: () -> Unit,
    assetViewModel: AssetViewModel = viewModel(),
    issueViewModel: IssueViewModel = viewModel()
) {
    val context = LocalContext.current
    val assets by assetViewModel.allAssets.collectAsStateWithLifecycle()
    val issues by issueViewModel.allIssues.collectAsStateWithLifecycle()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val reportText = remember(assets, issues) {
        ReportGenerator.generateTextReport(assets, issues)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, reportText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share Report")
                    }
                }
            )
        }
    ) { padding ->
        AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 30 }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = reportText,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
