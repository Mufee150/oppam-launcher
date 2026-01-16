package com.oppam.launcher.ui.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oppam.launcher.R
import com.oppam.launcher.ui.components.ElderHeading
import com.oppam.launcher.ui.components.ElderText
import com.oppam.launcher.ui.components.LargeButton
import com.oppam.launcher.ui.voice.VoiceAssistant

/**
 * Main Launcher Screen
 * Features:
 * - 3 large buttons for primary functions
 * - Voice greeting
 * - Simple, high-contrast UI
 * - Elderly-friendly design
 */
@Composable
fun LauncherScreen(
    onNavigateToCallFamily: () -> Unit,
    onNavigateToHealthLog: () -> Unit,
    onNavigateToProtection: () -> Unit,
    onNavigateToCaregiver: () -> Unit,
    viewModel: LauncherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Initialize voice assistant
    val voiceAssistant = remember {
        VoiceAssistant(context).apply {
            initialize { success ->
                if (success) {
                    // Greet user when voice is ready
                    sayWelcome()
                }
            }
        }
    }
    
    // Cleanup voice assistant when leaving
    DisposableEffect(Unit) {
        onDispose {
            voiceAssistant.shutdown()
        }
    }
    
    // Main UI
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
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Header section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // App title
                ElderHeading(
                    text = stringResource(R.string.launcher_title),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Greeting
                ElderText(
                    text = uiState.greeting,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Main buttons section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Call Family Button
                LargeButton(
                    text = stringResource(R.string.btn_call_family),
                    onClick = {
                        viewModel.onCallFamilyClick()
                        voiceAssistant.speak("Call Family")
                        onNavigateToCallFamily()
                    },
                    icon = Icons.Filled.Phone,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
                // Health Log Button
                LargeButton(
                    text = stringResource(R.string.btn_health_log),
                    onClick = {
                        viewModel.onHealthLogClick()
                        voiceAssistant.speak("Health Log")
                        onNavigateToHealthLog()
                    },
                    icon = Icons.Filled.Favorite,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
                // Protection Mode Button
                LargeButton(
                    text = stringResource(R.string.btn_protection_mode),
                    onClick = {
                        viewModel.onProtectionModeClick()
                        voiceAssistant.speak("Protection Mode")
                        onNavigateToProtection()
                    },
                    icon = Icons.Filled.Shield,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            
            // Footer - Settings/Caregiver access
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Discrete caregiver access button
                TextButton(
                    onClick = { onNavigateToCaregiver() },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = "Caregiver Access",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Caregiver",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
