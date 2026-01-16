package com.oppam.launcher.data

import java.time.LocalDateTime
import java.util.UUID

/**
 * Fake Health Repository for demonstration purposes
 * Provides mock health logs and medication reminders
 */
class FakeHealthRepository {
    
    private val healthLogs = mutableListOf<HealthLog>()
    private val medications = mutableListOf<MedicationReminder>()
    
    init {
        // Initialize with some sample data
        generateSampleHealthLogs()
        generateSampleMedications()
    }
    
    /**
     * Get all health logs
     */
    fun getHealthLogs(): List<HealthLog> {
        return healthLogs.sortedByDescending { it.timestamp }
    }
    
    /**
     * Get recent health logs (last 7 days)
     */
    fun getRecentHealthLogs(days: Int = 7): List<HealthLog> {
        val cutoffDate = LocalDateTime.now().minusDays(days.toLong())
        return healthLogs
            .filter { it.timestamp.isAfter(cutoffDate) }
            .sortedByDescending { it.timestamp }
    }
    
    /**
     * Add a new health log
     */
    fun addHealthLog(
        bloodPressureSystolic: Int?,
        bloodPressureDiastolic: Int?,
        bloodSugar: Int?,
        temperature: Float?,
        heartRate: Int?,
        notes: String?
    ): HealthLog {
        val log = HealthLog(
            id = UUID.randomUUID().toString(),
            timestamp = LocalDateTime.now(),
            bloodPressureSystolic = bloodPressureSystolic,
            bloodPressureDiastolic = bloodPressureDiastolic,
            bloodSugar = bloodSugar,
            temperature = temperature,
            heartRate = heartRate,
            notes = notes
        )
        healthLogs.add(0, log)
        return log
    }
    
    /**
     * Get all medications
     */
    fun getMedications(): List<MedicationReminder> {
        return medications
    }
    
    /**
     * Mark medication as taken
     */
    fun markMedicationTaken(medicationId: String, taken: Boolean) {
        val index = medications.indexOfFirst { it.id == medicationId }
        if (index >= 0) {
            medications[index] = medications[index].copy(isTaken = taken)
        }
    }
    
    /**
     * Add a new medication
     */
    fun addMedication(name: String, dosage: String, frequency: String, time: String): MedicationReminder {
        val medication = MedicationReminder(
            id = UUID.randomUUID().toString(),
            name = name,
            dosage = dosage,
            frequency = frequency,
            time = time,
            isTaken = false
        )
        medications.add(medication)
        return medication
    }
    
    /**
     * Generate sample health logs for demonstration
     */
    private fun generateSampleHealthLogs() {
        val now = LocalDateTime.now()
        
        healthLogs.add(
            HealthLog(
                id = UUID.randomUUID().toString(),
                timestamp = now.minusHours(2),
                bloodPressureSystolic = 125,
                bloodPressureDiastolic = 82,
                bloodSugar = 110,
                temperature = 36.8f,
                heartRate = 72,
                notes = "Feeling good after morning walk"
            )
        )
        
        healthLogs.add(
            HealthLog(
                id = UUID.randomUUID().toString(),
                timestamp = now.minusDays(1),
                bloodPressureSystolic = 130,
                bloodPressureDiastolic = 85,
                bloodSugar = 105,
                temperature = 36.9f,
                heartRate = 75,
                notes = "Evening check"
            )
        )
        
        healthLogs.add(
            HealthLog(
                id = UUID.randomUUID().toString(),
                timestamp = now.minusDays(2),
                bloodPressureSystolic = 122,
                bloodPressureDiastolic = 80,
                bloodSugar = 98,
                temperature = 36.7f,
                heartRate = 70,
                notes = null
            )
        )
        
        healthLogs.add(
            HealthLog(
                id = UUID.randomUUID().toString(),
                timestamp = now.minusDays(3),
                bloodPressureSystolic = 128,
                bloodPressureDiastolic = 84,
                bloodSugar = 115,
                temperature = 37.0f,
                heartRate = 78,
                notes = "Had lunch, checked after"
            )
        )
    }
    
    /**
     * Generate sample medications for demonstration
     */
    private fun generateSampleMedications() {
        medications.add(
            MedicationReminder(
                id = "1",
                name = "Blood Pressure Med",
                dosage = "5mg",
                frequency = "Once daily",
                time = "8:00 AM",
                isTaken = true
            )
        )
        
        medications.add(
            MedicationReminder(
                id = "2",
                name = "Diabetes Med",
                dosage = "500mg",
                frequency = "Twice daily",
                time = "8:00 AM, 8:00 PM",
                isTaken = false
            )
        )
        
        medications.add(
            MedicationReminder(
                id = "3",
                name = "Vitamin D",
                dosage = "1000 IU",
                frequency = "Once daily",
                time = "9:00 AM",
                isTaken = false
            )
        )
    }
    
    companion object {
        // Singleton instance
        @Volatile
        private var instance: FakeHealthRepository? = null
        
        fun getInstance(): FakeHealthRepository {
            return instance ?: synchronized(this) {
                instance ?: FakeHealthRepository().also { instance = it }
            }
        }
    }
}
