package com.oppam.launcher.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Large button component optimized for elderly users
 * Features:
 * - Extra large size (minimum 120dp height)
 * - High contrast colors
 * - Large text (28sp)
 * - Optional icon
 * - Prominent visual feedback
 */
@Composable
fun LargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            disabledElevation = 0.dp
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            // Icon if provided
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Button text - extra large and bold
            Text(
                text = text,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                lineHeight = 32.sp
            )
        }
    }
}

/**
 * Outlined large button variant
 * For secondary actions
 */
@Composable
fun LargeOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 3.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )
        }
    }
}
