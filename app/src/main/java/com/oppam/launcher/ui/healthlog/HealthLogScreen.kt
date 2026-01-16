package com.oppam.launcher.ui.healthlog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oppam.launcher.data.HealthLog
import com.oppam.launcher.data.MedicationReminder
import com.oppam.launcher.ui.components.ElderHeading
import com.oppam.launcher.ui.components.ElderText
import com.oppam.launcher.ui.components.LargeButton
import com.oppam.launcher.ui.components.LargeOutlinedButton
import com.oppam.launcher.ui.voice.VoiceAssistant

/**
 * Health Log Screen
 * Features:
 * - Log vital signs (BP, sugar, temperature, heart rate)
 * - Track medications
 * - View history of health logs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthLogScreen(
    onNavigateBack: () -> Unit,
    viewModel: HealthLogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Voice assistant
    val voiceAssistant = remember {
        VoiceAssistant(context).apply {
            initialize { success ->
                if (success) {
                    speak("Health Log")
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            voiceAssistant.shutdown()
        }
    }
    
    // Show success snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            snackbarHostState.showSnackbar("Health log saved successfully!")
            voiceAssistant.speak("Health data saved")
            viewModel.dismissSuccessMessage()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Health Log",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick Log Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ElderText(
                            text = "Quick Log",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        LargeButton(
                            text = "Add Health Data",
                            onClick = {
                                viewModel.setShowAddDialog(true)
                                voiceAssistant.speak("Add health data")
                            },
                            icon = Icons.Filled.Add,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            // Medications Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Medication,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ElderText(
                                text = "Today's Medications",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        if (uiState.medications.isEmpty()) {
                            ElderText(
                                text = "No medications added",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.medications.forEach { medication ->
                                    MedicationItem(
                                        medication = medication,
                                        onToggleTaken = { taken ->
                                            viewModel.toggleMedicationTaken(medication.id, taken)
                                            voiceAssistant.speak(if (taken) "Marked as taken" else "Marked as not taken")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Recent Logs Section
            item {
                ElderText(
                    text = "Recent Health Logs",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            
            if (uiState.recentLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElderText(
                            text = "No health logs yet. Add your first entry!",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            } else {
                items(uiState.recentLogs) { log ->
                    HealthLogCard(log = log)
                }
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // Add Health Log Dialog
    if (uiState.showAddDialog) {
        AddHealthLogDialog(
            onDismiss = { viewModel.setShowAddDialog(false) },
            onSave = { bp1, bp2, sugar, temp, hr, notes ->
                viewModel.addHealthLog(bp1, bp2, sugar, temp, hr, notes)
                viewModel.setShowAddDialog(false)
            }
        )
    }
}

/**
 * Medication Item Component
 */
@Composable
fun MedicationItem(
    medication: MedicationReminder,
    onToggleTaken: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (medication.isTaken)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medication.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${medication.dosage} • ${medication.frequency}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Time: ${medication.time}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Checkbox(
                checked = medication.isTaken,
                onCheckedChange = onToggleTaken,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Health Log Card Component
 */
@Composable
fun HealthLogCard(log: HealthLog) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Timestamp
            Text(
                text = log.getFormattedDate(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Vitals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Blood Pressure
                log.bloodPressureSystolic?.let { sys ->
                    log.bloodPressureDiastolic?.let { dia ->
                        VitalChip(
                            icon = Icons.Filled.MonitorHeart,
                            label = "BP",
                            value = "$sys/$dia mmHg"
                        )
                    }
                }
                
                // Blood Sugar
                log.bloodSugar?.let { sugar ->
                    VitalChip(
                        icon = Icons.Filled.Bloodtype,
                        label = "Sugar",
                        value = "$sugar mg/dL"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Temperature
                log.temperature?.let { temp ->
                    VitalChip(
                        icon = Icons.Filled.Thermostat,
                        label = "Temp",
                        value = "$temp°C"
                    )
                }
                
                // Heart Rate
                log.heartRate?.let { hr ->
                    VitalChip(
                        icon = Icons.Filled.FavoriteBorder,
                        label = "HR",
                        value = "$hr bpm"
                    )
                }
            }
            
            // Notes
            log.notes?.let { notes ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: $notes",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Vital Chip Component
 */
@Composable
fun VitalChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * Add Health Log Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthLogDialog(
    onDismiss: () -> Unit,
    onSave: (Int?, Int?, Int?, Float?, Int?, String?) -> Unit
) {
    var bpSystolic by remember { mutableStateOf("") }
    var bpDiastolic by remember { mutableStateOf("") }
    var bloodSugar by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Health Data",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Blood Pressure
                Text(
                    text = "Blood Pressure",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = bpSystolic,
                        onValueChange = { bpSystolic = it },
                        label = { Text("Systolic", fontSize = 16.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )
                    OutlinedTextField(
                        value = bpDiastolic,
                        onValueChange = { bpDiastolic = it },
                        label = { Text("Diastolic", fontSize = 16.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )
                }
                
                // Blood Sugar
                OutlinedTextField(
                    value = bloodSugar,
                    onValueChange = { bloodSugar = it },
                    label = { Text("Blood Sugar (mg/dL)", fontSize = 16.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                
                // Temperature
                OutlinedTextField(
                    value = temperature,
                    onValueChange = { temperature = it },
                    label = { Text("Temperature (°C)", fontSize = 16.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                
                // Heart Rate
                OutlinedTextField(
                    value = heartRate,
                    onValueChange = { heartRate = it },
                    label = { Text("Heart Rate (bpm)", fontSize = 16.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                
                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)", fontSize = 16.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        bpSystolic.toIntOrNull(),
                        bpDiastolic.toIntOrNull(),
                        bloodSugar.toIntOrNull(),
                        temperature.toFloatOrNull(),
                        heartRate.toIntOrNull(),
                        notes.ifBlank { null }
                    )
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Save", fontSize = 20.sp)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.height(56.dp)
            ) {
                Text("Cancel", fontSize = 20.sp)
            }
        }
    )
}
