package com.oppam.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oppam.launcher.ui.callfamily.CallFamilyScreen
import com.oppam.launcher.ui.caregiver.CaregiverDashboard
import com.oppam.launcher.ui.healthlog.HealthLogScreen
import com.oppam.launcher.ui.launcher.LauncherScreen
import com.oppam.launcher.ui.protection.RakshaShieldScreen
import com.oppam.launcher.ui.theme.OppamTheme

/**
 * MainActivity - Entry point for Oppam Launcher
 * Sets up navigation between different screens using Jetpack Compose Navigation
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            OppamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation setup
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "launcher"
                    ) {
                        // Main launcher screen
                        composable("launcher") {
                            LauncherScreen(
                                onNavigateToCallFamily = {
                                    navController.navigate("callfamily")
                                },
                                onNavigateToHealthLog = {
                                    navController.navigate("healthlog")
                                },
                                onNavigateToProtection = {
                                    navController.navigate("protection")
                                },
                                onNavigateToCaregiver = {
                                    navController.navigate("caregiver")
                                }
                            )
                        }
                        
                        // Call Family screen
                        composable("callfamily") {
                            CallFamilyScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        // Health Log screen
                        composable("healthlog") {
                            HealthLogScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        // Protection mode screen
                        composable("protection") {
                            RakshaShieldScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        // Caregiver dashboard
                        composable("caregiver") {
                            CaregiverDashboard(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Handle back button press
     * Prevents exiting the launcher accidentally
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // For launcher, we might want to disable back button
        // or show a confirmation dialog
        // For now, we'll allow normal back navigation
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }
}
