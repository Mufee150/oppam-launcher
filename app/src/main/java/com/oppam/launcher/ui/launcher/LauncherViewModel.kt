package com.oppam.launcher.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Launcher Screen
 * Manages UI state and handles user interactions
 */
class LauncherViewModel : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()
    
    /**
     * Initialize the launcher
     */
    init {
        // Perform any initialization here
        greetUser()
    }
    
    /**
     * Greet the user based on time of day
     */
    private fun greetUser() {
        viewModelScope.launch {
            val greeting = getGreetingBasedOnTime()
            _uiState.value = _uiState.value.copy(greeting = greeting)
        }
    }
    
    /**
     * Get greeting message based on current time
     */
    private fun getGreetingBasedOnTime(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
    
    /**
     * Handle button clicks
     */
    fun onCallFamilyClick() {
        // Future: Implement call family functionality
        _uiState.value = _uiState.value.copy(lastAction = "Call Family clicked")
    }
    
    fun onHealthLogClick() {
        // Future: Navigate to health log
        _uiState.value = _uiState.value.copy(lastAction = "Health Log clicked")
    }
    
    fun onProtectionModeClick() {
        _uiState.value = _uiState.value.copy(lastAction = "Protection Mode clicked")
    }
}

/**
 * UI State for Launcher Screen
 */
data class LauncherUiState(
    val greeting: String = "Welcome",
    val lastAction: String = "",
    val isLoading: Boolean = false
)
