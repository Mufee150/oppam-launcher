package com.oppam.launcher.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Large text component for elderly users
 * Features:
 * - Extra large font size (default 32sp)
 * - High contrast
 * - Bold weight for better readability
 */
@Composable
fun ElderText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign,
        lineHeight = fontSize * 1.2f
    )
}

/**
 * Extra large heading text for main titles
 */
@Composable
fun ElderHeading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    ElderText(
        text = text,
        modifier = modifier,
        fontSize = 48.sp,
        fontWeight = FontWeight.ExtraBold,
        color = color,
        textAlign = TextAlign.Center
    )
}

/**
 * Medium text for descriptions and body content
 */
@Composable
fun ElderBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        color = color,
        lineHeight = 32.sp
    )
}
