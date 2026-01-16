package com.oppam.launcher.data

import java.time.LocalDateTime
import kotlin.random.Random

/**
 * Fake repository for simulated behavior data
 * This generates dummy data for the caregiver dashboard
 * 
 * NOTE: This is NOT connected to any real user data
 * All data is simulated for demonstration purposes
 */
class FakeBehaviorRepository {
    
    private val alerts = mutableListOf<Alert>()
    private val behaviorData = mutableListOf<BehaviorDataPoint>()
    
    init {
        generateFakeData()
    }
    
    /**
     * Generate fake alerts and behavior data
     */
    private fun generateFakeData() {
        // Generate some past alerts
        alerts.add(
            Alert(
                id = "1",
                timestamp = LocalDateTime.now().minusHours(2),
                type = AlertType.SCAM_CALL,
                message = "Suspicious call from unknown number detected",
                severity = Severity.HIGH
            )
        )
        
        alerts.add(
            Alert(
                id = "2",
                timestamp = LocalDateTime.now().minusDays(1),
                type = AlertType.SCAM_CALL,
                message = "Blocked potential scam call",
                severity = Severity.MEDIUM
            )
        )
        
        alerts.add(
            Alert(
                id = "3",
                timestamp = LocalDateTime.now().minusDays(3),
                type = AlertType.HEALTH_REMINDER,
                message = "Medicine reminder acknowledged",
                severity = Severity.LOW
            )
        )
        
        // Generate behavior data for the last 7 days
        for (i in 7 downTo 0) {
            val baseRisk = 0.3f
            val variation = Random.nextFloat() * 0.4f
            
            behaviorData.add(
                BehaviorDataPoint(
                    timestamp = LocalDateTime.now().minusDays(i.toLong()),
                    riskScore = (baseRisk + variation).coerceIn(0f, 1f)
                )
            )
        }
    }
    
    /**
     * Add a new alert (when scam is detected)
     */
    fun addAlert(type: AlertType, message: String, severity: Severity) {
        val newAlert = Alert(
            id = (alerts.size + 1).toString(),
            timestamp = LocalDateTime.now(),
            type = type,
            message = message,
            severity = severity
        )
        
        alerts.add(0, newAlert) // Add to beginning
        
        // Also update behavior data
        behaviorData.add(
            BehaviorDataPoint(
                timestamp = LocalDateTime.now(),
                riskScore = when (severity) {
                    Severity.CRITICAL -> 0.9f
                    Severity.HIGH -> 0.7f
                    Severity.MEDIUM -> 0.5f
                    Severity.LOW -> 0.3f
                }
            )
        )
    }
    
    /**
     * Get all alerts
     */
    fun getAlerts(): List<Alert> = alerts.toList()
    
    /**
     * Get recent alerts (last 5)
     */
    fun getRecentAlerts(): List<Alert> = alerts.take(5)
    
    /**
     * Get behavior history
     */
    fun getBehaviorHistory(): List<BehaviorDataPoint> = behaviorData.toList()
    
    /**
     * Calculate current risk level based on recent behavior
     */
    fun getCurrentRiskLevel(): RiskLevel {
        val recentScores = behaviorData.takeLast(3).map { it.riskScore }
        val avgRisk = recentScores.average().toFloat()
        
        return when {
            avgRisk >= 0.7f -> RiskLevel.HIGH
            avgRisk >= 0.4f -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
    
    /**
     * Get complete dashboard data
     */
    fun getDashboardData(): DashboardData {
        return DashboardData(
            currentRiskLevel = getCurrentRiskLevel(),
            recentAlerts = getRecentAlerts(),
            behaviorHistory = getBehaviorHistory(),
            lastAlertTime = alerts.firstOrNull()?.timestamp
        )
    }
    
    /**
     * Simulate a scam call detection
     */
    fun simulateScamDetection() {
        addAlert(
            type = AlertType.SCAM_CALL,
            message = "Scam call detected and blocked",
            severity = Severity.HIGH
        )
    }
}
