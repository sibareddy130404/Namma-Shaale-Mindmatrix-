package com.nammashale.assetmanager.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashale.assetmanager.ui.viewmodel.AssetViewModel
import kotlinx.coroutines.flow.collectLatest

val assetCategories = listOf("Furniture", "Electronics", "Sports Equipment", "Books", "Lab Equipment", "Others")
val conditionOptions = listOf("Working", "Needs Repair", "Broken")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetRegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    photoUri: String? = null,
    assetViewModel: AssetViewModel = viewModel()
) {
    val context = LocalContext.current
    var assetName by remember { mutableStateOf("") }
    var serialNo by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(assetCategories[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCondition by remember { mutableStateOf(conditionOptions[0]) }
    var conditionExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var serialError by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    // Listen for save events to show Toast
    LaunchedEffect(Unit) {
        assetViewModel.saveEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Asset", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 30 }) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Asset Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Fill in the information below to register a new asset.",
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                }

                OutlinedTextField(
                    value = assetName, onValueChange = { assetName = it; nameError = false },
                    label = { Text("Asset Name *") }, placeholder = { Text("e.g. Wooden Chair") },
                    isError = nameError, supportingText = if (nameError) {{ Text("Asset name is required") }} else null,
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true
                )

                OutlinedTextField(
                    value = serialNo, onValueChange = { serialNo = it; serialError = false },
                    label = { Text("Serial Number *") }, placeholder = { Text("e.g. SN-2024-001") },
                    isError = serialError, supportingText = if (serialError) {{ Text("Serial number is required") }} else null,
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true
                )

                ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = !categoryExpanded }) {
                    OutlinedTextField(value = selectedCategory, onValueChange = {}, readOnly = true, label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                        assetCategories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat) }, onClick = { selectedCategory = cat; categoryExpanded = false })
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = conditionExpanded, onExpandedChange = { conditionExpanded = !conditionExpanded }) {
                    OutlinedTextField(value = selectedCondition, onValueChange = {}, readOnly = true, label = { Text("Condition") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = conditionExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = conditionExpanded, onDismissRequest = { conditionExpanded = false }) {
                        conditionOptions.forEach { cond ->
                            DropdownMenuItem(text = { Text(cond) }, onClick = { selectedCondition = cond; conditionExpanded = false })
                        }
                    }
                }

                OutlinedCard(onClick = onNavigateToCamera,
                    modifier = Modifier.fillMaxWidth().height(140.dp), shape = RoundedCornerShape(12.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (photoUri != null) {
                            // Ideally, load image with Coil here. For now, show success text.
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Photo Captured", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.height(8.dp))
                                Text("Photo captured", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Take Photo", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Text("Tap to capture photo", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        nameError = assetName.isBlank()
                        serialError = serialNo.isBlank()
                        if (!nameError && !serialError) {
                            assetViewModel.saveAsset(assetName, serialNo, selectedCategory, selectedCondition, photoUri)
                            assetName = ""; serialNo = ""
                            selectedCategory = assetCategories[0]; selectedCondition = conditionOptions[0]
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Save Asset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
