package com.oppam.launcher.ui.protection

import android.content.Context
import android.widget.Toast
import com.oppam.launcher.data.Alert
import com.oppam.launcher.data.AlertType
import com.oppam.launcher.data.FakeBehaviorRepository
import com.oppam.launcher.data.Severity
import java.time.LocalDateTime

/**
 * Notification Helper for Alert Simulation
 * Simulates sending notifications to caregivers when alerts are triggered
 * 
 * NOTE: This is a simulation. Real implementation would use:
 * - Firebase Cloud Messaging (FCM)
 * - Android Notification Manager
 * - Background services
 */
class AlertNotificationHelper(private val context: Context) {
    
    private val repository = FakeBehaviorRepository.getInstance()
    
    /**
     * Trigger a scam alert notification
     * This simulates:
     * 1. Adding alert to dashboard
     * 2. Sending notification to caregiver's device
     * 3. Showing toast confirmation
     */
    fun triggerScamAlert(phoneNumber: String? = null) {
        val alert = Alert(
            id = System.currentTimeMillis().toString(),
            timestamp = LocalDateTime.now(),
            type = AlertType.SCAM_CALL,
            message = if (phoneNumber != null) {
                "Potential scam call detected from $phoneNumber"
            } else {
                "Potential scam call detected and blocked"
            },
            severity = Severity.HIGH
        )
        
        // Add to repository (for dashboard)
        repository.addAlert(alert)
        
        // Simulate sending notification to caregiver
        simulateNotification(alert)
        
        // Show confirmation toast
        Toast.makeText(
            context,
            "⚠️ Alert sent to caregiver",
            Toast.LENGTH_LONG
        ).show()
    }
    
    /**
     * Trigger a health reminder alert
     */
    fun triggerHealthReminder(message: String) {
        val alert = Alert(
            id = System.currentTimeMillis().toString(),
            timestamp = LocalDateTime.now(),
            type = AlertType.HEALTH_REMINDER,
            message = message,
            severity = Severity.MEDIUM
        )
        
        repository.addAlert(alert)
        simulateNotification(alert)
    }
    
    /**
     * Trigger a suspicious behavior alert
     */
    fun triggerSuspiciousBehaviorAlert(message: String) {
        val alert = Alert(
            id = System.currentTimeMillis().toString(),
            timestamp = LocalDateTime.now(),
            type = AlertType.SUSPICIOUS_BEHAVIOR,
            message = message,
            severity = Severity.MEDIUM
        )
        
        repository.addAlert(alert)
        simulateNotification(alert)
    }
    
    /**
     * Simulate sending a push notification to caregiver
     * In production, this would use FCM or Android Notification Manager
     */
    private fun simulateNotification(alert: Alert) {
        // In a real app, this would:
        // 1. Create a notification channel
        // 2. Build a notification with:
        //    - Title: "Oppam Alert"
        //    - Body: alert.message
        //    - Action: Open caregiver dashboard
        // 3. Send via NotificationManager
        
        // For simulation, we just log
        println("📱 SIMULATED NOTIFICATION TO CAREGIVER:")
        println("   Type: ${alert.type}")
        println("   Severity: ${alert.severity}")
        println("   Message: ${alert.message}")
        println("   Time: ${alert.timestamp}")
    }
    
    /**
     * Get pending alert count
     */
    fun getPendingAlertCount(): Int {
        return repository.getDashboardData().recentAlerts.size
    }
    
    companion object {
        @Volatile
        private var instance: AlertNotificationHelper? = null
        
        fun getInstance(context: Context): AlertNotificationHelper {
            return instance ?: synchronized(this) {
                instance ?: AlertNotificationHelper(context.applicationContext)
                    .also { instance = it }
            }
        }
    }
}
