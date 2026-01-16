package com.oppam.launcher.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Light color scheme for Oppam
 * High contrast colors for better visibility by elderly users
 */
private val LightColorScheme = lightColorScheme(
    primary = OppamGreen,
    onPrimary = OppamTextLight,
    primaryContainer = OppamGreenLight,
    onPrimaryContainer = OppamTextPrimary,
    
    secondary = OppamOrange,
    onSecondary = OppamTextLight,
    secondaryContainer = OppamOrangeLight,
    onSecondaryContainer = OppamTextPrimary,
    
    tertiary = OppamGreenDark,
    onTertiary = OppamTextLight,
    
    error = OppamError,
    onError = OppamTextLight,
    errorContainer = ScamAlertBackground,
    onErrorContainer = ScamAlertRed,
    
    background = OppamBackground,
    onBackground = OppamTextPrimary,
    
    surface = OppamSurface,
    onSurface = OppamTextPrimary,
    surfaceVariant = OppamBackground,
    onSurfaceVariant = OppamTextSecondary
)

/**
 * Dark color scheme - Currently using light scheme
 * Oppam is designed primarily for light mode (better for elderly users)
 */
private val DarkColorScheme = darkColorScheme(
    primary = OppamGreenLight,
    onPrimary = OppamTextPrimary,
    secondary = OppamOrangeLight,
    onSecondary = OppamTextPrimary,
    background = OppamTextPrimary,
    onBackground = OppamTextLight
)

/**
 * Oppam theme with high contrast and large text sizes
 * Optimized for elderly users with vision impairments
 */
@Composable
fun OppamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For elderly users, we'll always use light theme for better visibility
    val colorScheme = LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
