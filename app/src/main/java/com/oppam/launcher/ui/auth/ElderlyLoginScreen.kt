package com.oppam.launcher.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oppam.launcher.R
import com.oppam.launcher.data.RoleManager
import com.oppam.launcher.ui.components.ElderHeading
import com.oppam.launcher.ui.components.LargeButton
import com.oppam.launcher.ui.voice.VoiceAssistant

/**
 * Elderly User Login Screen
 * Simple 4-digit PIN entry for elderly users
 */
@Composable
fun ElderlyLoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val roleManager = remember { RoleManager.getInstance(context) }
    
    var pin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isFirstTime by remember { mutableStateOf(!roleManager.hasElderlyPin()) }
    var confirmPin by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }
    
    // Voice assistant
    val voiceAssistant = remember {
        VoiceAssistant(context).apply {
            initialize { success ->
                if (success) {
                    if (isFirstTime) {
                        speak("Create your PIN")
                    } else {
                        speak("Enter your PIN")
                    }
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            voiceAssistant.shutdown()
        }
    }
    
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
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Elderly,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Title
            ElderHeading(
                text = if (isFirstTime) 
                    stringResource(R.string.create_pin_title)
                else 
                    stringResource(R.string.enter_pin_title),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = if (isFirstTime)
                    stringResource(R.string.create_pin_subtitle)
                else
                    stringResource(R.string.enter_pin_subtitle),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // PIN Input
            OutlinedTextField(
                value = if (showConfirm) confirmPin else pin,
                onValueChange = { 
                    if (it.length <= 4) {
                        if (showConfirm) {
                            confirmPin = it
                        } else {
                            pin = it
                        }
                        showError = false
                    }
                },
                label = { 
                    Text(
                        if (showConfirm) 
                            stringResource(R.string.confirm_pin)
                        else 
                            stringResource(R.string.four_digit_pin), 
                        fontSize = 20.sp
                    ) 
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = showError,
                supportingText = if (showError) {
                    { Text(stringResource(R.string.pin_error), fontSize = 16.sp) }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 8.sp
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Login/Create Button
            LargeButton(
                text = if (isFirstTime) {
                    if (showConfirm) stringResource(R.string.create_account)
                    else stringResource(R.string.btn_next)
                } else {
                    stringResource(R.string.btn_login)
                },
                onClick = {
                    if (isFirstTime) {
                        if (!showConfirm) {
                            // First PIN entry
                            if (pin.length == 4) {
                                showConfirm = true
                                voiceAssistant.speak("Confirm PIN")
                            } else {
                                showError = true
                            }
                        } else {
                            // Confirm PIN
                            if (pin == confirmPin) {
                                roleManager.setElderlyPin(pin)
                                voiceAssistant.speak("PIN created successfully")
                                onLoginSuccess()
                            } else {
                                showError = true
                                confirmPin = ""
                                voiceAssistant.speak("PINs do not match")
                            }
                        }
                    } else {
                        // Login
                        if (roleManager.verifyElderlyPin(pin)) {
                            roleManager.switchToElderlyMode()
                            voiceAssistant.speak("Welcome")
                            onLoginSuccess()
                        } else {
                            showError = true
                            pin = ""
                            voiceAssistant.speak("Incorrect PIN")
                        }
                    }
                },
                icon = Icons.Filled.Login,
                enabled = (if (showConfirm) confirmPin else pin).isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Back Button
            TextButton(
                onClick = {
                    if (showConfirm) {
                        showConfirm = false
                        confirmPin = ""
                    } else {
                        onBack()
                    }
                },
                modifier = Modifier.height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.back),
                    fontSize = 20.sp
                )
            }
        }
    }
}
