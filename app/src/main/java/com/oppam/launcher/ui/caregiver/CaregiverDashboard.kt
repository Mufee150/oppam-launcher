package com.oppam.launcher.ui.caregiver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oppam.launcher.R
import com.oppam.launcher.data.*
import com.oppam.launcher.ui.components.ElderHeading
import java.time.format.DateTimeFormatter

/**
 * Caregiver Dashboard - Monitoring Mode
 * Shows:
 * - Current risk level
 * - Recent alerts and notifications
 * - Behavior change graph (simplified)
 * - Last activity timestamp
 * - Quick stats
 * 
 * NOTE: All data is simulated for demonstration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverDashboard(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val roleManager = remember { RoleManager.getInstance(context) }
    val repository = remember { FakeBehaviorRepository.getInstance() }
    val dashboardData by remember { mutableStateOf(repository.getDashboardData()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Caregiver Dashboard",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        roleManager.switchToElderlyMode()
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to Launcher",
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Logout/Switch to elderly mode
                    IconButton(onClick = {
                        roleManager.switchToElderlyMode()
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Exit Caregiver Mode",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Monitoring Mode Active",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Viewing parent's health & safety status",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickStatCard(
                        title = "Total Alerts",
                        value = dashboardData.recentAlerts.size.toString(),
                        icon = Icons.Filled.Notifications,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        title = "Risk Level",
                        value = dashboardData.currentRiskLevel.name,
                        icon = Icons.Filled.HealthAndSafety,
                        color = when (dashboardData.currentRiskLevel) {
                            RiskLevel.HIGH -> MaterialTheme.colorScheme.error
                            RiskLevel.MEDIUM -> Color(0xFFFF9800)
                            RiskLevel.LOW -> Color(0xFF4CAF50)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Risk Level Card
            item {
                RiskLevelCard(riskLevel = dashboardData.currentRiskLevel)
            }
            
            // Last Alert Card
            item {
                LastAlertCard(lastAlertTime = dashboardData.lastAlertTime)
            }
            
            // Behavior Graph Card
            item {
                BehaviorGraphCard(behaviorHistory = dashboardData.behaviorHistory)
            }
            
            // Recent Alerts Header
            item {
                Text(
                    text = stringResource(R.string.recent_alerts),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Recent Alerts List
            items(dashboardData.recentAlerts) { alert ->
                AlertCard(alert = alert)
            }
            
            // Disclaimer
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "This is a demo dashboard with simulated data",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Risk Level Card
 */
@Composable
fun RiskLevelCard(riskLevel: RiskLevel) {
    val (color, icon) = when (riskLevel) {
        RiskLevel.LOW -> Pair(Color(0xFF4CAF50), Icons.Filled.CheckCircle)
        RiskLevel.MEDIUM -> Pair(Color(0xFFFFA000), Icons.Filled.Warning)
        RiskLevel.HIGH -> Pair(Color(0xFFD32F2F), Icons.Filled.Error)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = color
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.risk_level),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = riskLevel.name,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

/**
 * Last Alert Card
 */
@Composable
fun LastAlertCard(lastAlertTime: java.time.LocalDateTime?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.last_alert),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lastAlertTime?.format(
                        DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
                    ) ?: "No alerts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * Behavior Graph Card (Simplified visualization)
 */
@Composable
fun BehaviorGraphCard(behaviorHistory: List<BehaviorDataPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ShowChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.behavior_changes),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simplified bar graph
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                behaviorHistory.takeLast(7).forEach { dataPoint ->
                    val barHeight = (dataPoint.riskScore * 100).dp
                    val barColor = when {
                        dataPoint.riskScore >= 0.7f -> Color(0xFFD32F2F)
                        dataPoint.riskScore >= 0.4f -> Color(0xFFFFA000)
                        else -> Color(0xFF4CAF50)
                    }
                    
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(barHeight.coerceAtLeast(8.dp))
                            .background(
                                color = barColor,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Last 7 days risk trend",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Individual Alert Card
 */
@Composable
fun AlertCard(alert: Alert) {
    val (icon, color) = when (alert.severity) {
        Severity.CRITICAL -> Pair(Icons.Filled.Error, Color(0xFFD32F2F))
        Severity.HIGH -> Pair(Icons.Filled.Warning, Color(0xFFE65100))
        Severity.MEDIUM -> Pair(Icons.Filled.Info, Color(0xFFFFA000))
        Severity.LOW -> Pair(Icons.Filled.CheckCircle, Color(0xFF388E3C))
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = alert.timestamp.format(
                        DateTimeFormatter.ofPattern("MMM dd, hh:mm a")
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            // Severity badge
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = alert.severity.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Quick Stat Card Component
 */
@Composable
fun QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
