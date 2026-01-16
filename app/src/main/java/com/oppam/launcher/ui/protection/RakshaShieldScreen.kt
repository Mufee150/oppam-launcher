package com.oppam.launcher.ui.protection

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oppam.launcher.R
import com.oppam.launcher.data.AlertType
import com.oppam.launcher.data.FakeBehaviorRepository
import com.oppam.launcher.data.Severity
import com.oppam.launcher.ui.components.ElderHeading
import com.oppam.launcher.ui.components.ElderText
import com.oppam.launcher.ui.components.LargeButton
import com.oppam.launcher.ui.components.LargeOutlinedButton
import com.oppam.launcher.ui.theme.ScamAlertRed
import com.oppam.launcher.ui.voice.VoiceAssistant
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Protection Mode Screen (Raksha Shield)
 * Features:
 * - Simulate scam call button
 * - Fake incoming call overlay
 * - Scam detection alert
 * - Voice warnings in Malayalam
 */
@Composable
fun RakshaShieldScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { FakeBehaviorRepository.getInstance() }
    val alertHelper = remember { AlertNotificationHelper.getInstance(context) }
    
    // Voice assistant
    val voiceAssistant = remember {
        VoiceAssistant(context).apply {
            initialize()
        }
    }
    
    // State
    var showFakeCall by remember { mutableStateOf(false) }
    var showScamAlert by remember { mutableStateOf(false) }
    var alertSent by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        onDispose {
            voiceAssistant.shutdown()
        }
    }
    
    // Main screen
    if (!showFakeCall) {
        // Protection Mode Main Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ElderHeading(
                        text = stringResource(R.string.protection_title)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ElderText(
                        text = "You are protected from scam calls",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Demo button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Demo Mode",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Test the scam detection feature",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Simulate scam call button
                    LargeButton(
                        text = stringResource(R.string.simulate_scam_call),
                        onClick = {
                            showFakeCall = true
                            voiceAssistant.speak("Simulating call")
                        },
                        icon = Icons.Filled.Warning,
                        containerColor = MaterialTheme.colorScheme.error
                    )
                }
                
                // Back button
                LargeOutlinedButton(
                    text = stringResource(R.string.back),
                    onClick = onNavigateBack,
                    icon = Icons.Filled.ArrowBack
                )
            }
        }
    } else {
        // Show fake incoming call screen
        FakeIncomingCallScreen(
            onDetectScam = {
                showScamAlert = true
                repository.simulateScamDetection()
                
                // Trigger notification to caregiver
                alertHelper.triggerScamAlert("+91 98765 43210")
                
                voiceAssistant.sayScamDetected()
                
                // Auto-dismiss after showing alert
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    voiceAssistant.sayCaregiverAlerted()
                    alertSent = true
                    delay(2000)
                    showFakeCall = false
                    showScamAlert = false
                    alertSent = false
                }
            },
            onDismiss = {
                showFakeCall = false
                showScamAlert = false
            },
            showAlert = showScamAlert,
            alertSent = alertSent
        )
    }
}

/**
 * Fake Incoming Call Screen
 * Simulates a scam call
 */
@Composable
fun FakeIncomingCallScreen(
    onDetectScam: () -> Unit,
    onDismiss: () -> Unit,
    showAlert: Boolean,
    alertSent: Boolean
) {
    // Pulsing animation for scam alert
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (showAlert) ScamAlertRed.copy(alpha = alpha)
                else MaterialTheme.colorScheme.surface
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Caller info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showAlert) {
                    // Scam Alert
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.scam_warning),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "സൂക്ഷിക്കുക! വഞ്ചന വിളി",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Normal incoming call
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.incoming_call),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "+91 98765 43210",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Unknown Number",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            
            // Alert sent message
            if (alertSent) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.alert_sent),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
            
            // Buttons
            if (!showAlert) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Detect scam button (for demo)
                    LargeButton(
                        text = "Detect as Scam",
                        onClick = onDetectScam,
                        icon = Icons.Filled.Block,
                        containerColor = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Reject button
                    LargeOutlinedButton(
                        text = stringResource(R.string.btn_reject),
                        onClick = onDismiss,
                        icon = Icons.Filled.Close
                    )
                }
            }
        }
    }
}
