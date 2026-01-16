package com.oppam.launcher.ui.healthlog

import androidx.lifecycle.ViewModel
import com.oppam.launcher.data.FakeHealthRepository
import com.oppam.launcher.data.HealthLog
import com.oppam.launcher.data.MedicationReminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for Health Log Screen
 * Manages health logs and medication reminders
 */
class HealthLogViewModel : ViewModel() {
    
    private val repository = FakeHealthRepository.getInstance()
    
    private val _uiState = MutableStateFlow(HealthLogUiState())
    val uiState: StateFlow<HealthLogUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    /**
     * Load health logs and medications
     */
    private fun loadData() {
        val logs = repository.getRecentHealthLogs(7)
        val medications = repository.getMedications()
        
        _uiState.value = _uiState.value.copy(
            recentLogs = logs,
            medications = medications
        )
    }
    
    /**
     * Add a new health log entry
     */
    fun addHealthLog(
        bloodPressureSystolic: Int?,
        bloodPressureDiastolic: Int?,
        bloodSugar: Int?,
        temperature: Float?,
        heartRate: Int?,
        notes: String?
    ) {
        repository.addHealthLog(
            bloodPressureSystolic = bloodPressureSystolic,
            bloodPressureDiastolic = bloodPressureDiastolic,
            bloodSugar = bloodSugar,
            temperature = temperature,
            heartRate = heartRate,
            notes = notes
        )
        loadData()
        
        // Show success message
        _uiState.value = _uiState.value.copy(
            showSuccessMessage = true
        )
    }
    
    /**
     * Mark medication as taken/not taken
     */
    fun toggleMedicationTaken(medicationId: String, taken: Boolean) {
        repository.markMedicationTaken(medicationId, taken)
        loadData()
    }
    
    /**
     * Dismiss success message
     */
    fun dismissSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            showSuccessMessage = false
        )
    }
    
    /**
     * Show/hide add log dialog
     */
    fun setShowAddDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showAddDialog = show
        )
    }
}

/**
 * UI State for Health Log Screen
 */
data class HealthLogUiState(
    val recentLogs: List<HealthLog> = emptyList(),
    val medications: List<MedicationReminder> = emptyList(),
    val showAddDialog: Boolean = false,
    val showSuccessMessage: Boolean = false
)
