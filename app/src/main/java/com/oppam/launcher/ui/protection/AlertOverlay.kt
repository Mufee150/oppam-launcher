package com.oppam.launcher.ui.protection

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oppam.launcher.ui.theme.ScamAlertRed

/**
 * Alert Overlay Component
 * Can be shown on top of any screen to indicate a scam detection
 * Features pulsing animation to grab attention
 */
@Composable
fun AlertOverlay(
    message: String,
    malayalamMessage: String = "",
    modifier: Modifier = Modifier
) {
    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScamAlertRed.copy(alpha = alpha)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Warning icon with scale animation
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Alert",
                modifier = Modifier.size((100 * scale).dp),
                tint = Color.White
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // English message
            Text(
                text = message,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp
            )
            
            // Malayalam message if provided
            if (malayalamMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = malayalamMessage,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
        }
    }
}
