package com.oppam.launcher.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Data models for the Oppam app
 */

/**
 * Alert data model
 */
data class Alert(
    val id: String,
    val timestamp: LocalDateTime,
    val type: AlertType,
    val message: String,
    val severity: Severity
)

enum class AlertType {
    SCAM_CALL,
    SUSPICIOUS_BEHAVIOR,
    HEALTH_REMINDER,
    FAMILY_NOTIFICATION
}

enum class Severity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Behavior data point for graphs
 */
data class BehaviorDataPoint(
    val timestamp: LocalDateTime,
    val riskScore: Float // 0.0 to 1.0
)

/**
 * Risk level assessment
 */
enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Dashboard data model
 */
data class DashboardData(
    val currentRiskLevel: RiskLevel,
    val recentAlerts: List<Alert>,
    val behaviorHistory: List<BehaviorDataPoint>,
    val lastAlertTime: LocalDateTime?
)

/**
 * Health Log data model
 */
data class HealthLog(
    val id: String,
    val timestamp: LocalDateTime,
    val bloodPressureSystolic: Int?,  // e.g., 120
    val bloodPressureDiastolic: Int?,  // e.g., 80
    val bloodSugar: Int?,              // mg/dL
    val temperature: Float?,           // Celsius
    val heartRate: Int?,               // bpm
    val notes: String?
) {
    fun getFormattedDate(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
    }
}

/**
 * Medication Reminder data model
 */
data class MedicationReminder(
    val id: String,
    val name: String,
    val dosage: String,
    val frequency: String,  // e.g., "Twice daily", "Once at night"
    val time: String,       // e.g., "8:00 AM"
    val isTaken: Boolean
)

/**
 * Health Vital for quick input
 */
enum class HealthVital {
    BLOOD_PRESSURE,
    BLOOD_SUGAR,
    TEMPERATURE,
    HEART_RATE
}
