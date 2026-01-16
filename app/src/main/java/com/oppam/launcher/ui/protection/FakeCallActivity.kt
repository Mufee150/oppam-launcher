package com.oppam.launcher.ui.protection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oppam.launcher.ui.theme.OppamTheme
import com.oppam.launcher.ui.theme.ScamAlertRed
import com.oppam.launcher.ui.voice.VoiceAssistant

/**
 * Fake Call Activity
 * This activity can be launched to simulate an incoming call
 * Used for testing the scam detection overlay
 * 
 * NOTE: This does NOT intercept real calls
 * It's purely for demonstration purposes
 */
class FakeCallActivity : ComponentActivity() {
    
    private lateinit var voiceAssistant: VoiceAssistant
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize voice assistant
        voiceAssistant = VoiceAssistant(this).apply {
            initialize()
        }
        
        setContent {
            OppamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ScamAlertRed
                ) {
                    // Scam alert overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ScamAlertRed)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Warning",
                                modifier = Modifier.size(120.dp),
                                tint = Color.White
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Text(
                                text = "⚠️ SCAM ALERT ⚠️",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "സൂക്ഷിക്കുക!\nഇത് വഞ്ചന വിളിയാകാം",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 40.sp
                            )
                            
                            Spacer(modifier = Modifier.height(48.dp))
                            
                            Button(
                                onClick = { finish() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = ScamAlertRed
                                )
                            ) {
                                Text(
                                    text = "CLOSE",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Speak warning
        voiceAssistant.sayScamDetected()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        voiceAssistant.shutdown()
    }
}
