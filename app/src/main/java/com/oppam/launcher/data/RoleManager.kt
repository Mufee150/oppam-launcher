package com.oppam.launcher.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * User Roles in the app
 */
enum class UserRole {
    ELDERLY,    // Default launcher mode for elderly users
    CAREGIVER   // Monitoring mode for family/caregivers
}

/**
 * Role Manager - Handles user role switching and authentication
 * Uses SharedPreferences to persist caregiver PIN
 */
class RoleManager private constructor(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "oppam_role_prefs",
        Context.MODE_PRIVATE
    )
    
    private val _currentRole = MutableStateFlow(UserRole.ELDERLY)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()
    
    companion object {
        @Volatile
        private var instance: RoleManager? = null
        
        private const val KEY_CAREGIVER_PIN = "caregiver_pin"
        private const val KEY_ELDERLY_PIN = "elderly_pin"
        private const val DEFAULT_PIN = "1234" // Default PIN for demo
        
        fun getInstance(context: Context): RoleManager {
            return instance ?: synchronized(this) {
                instance ?: RoleManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    init {
        // Set default caregiver PIN if not set
        if (!prefs.contains(KEY_CAREGIVER_PIN)) {
            prefs.edit().putString(KEY_CAREGIVER_PIN, DEFAULT_PIN).apply()
        }
    }
    
    /**
     * Get current user role
     */
    fun getCurrentRole(): UserRole {
        return _currentRole.value
    }
    
    /**
     * Switch to elderly mode (launcher)
     */
    fun switchToElderlyMode() {
        _currentRole.value = UserRole.ELDERLY
    }
    
    /**
     * Attempt to switch to caregiver mode with PIN
     * @return true if PIN is correct, false otherwise
     */
    fun switchToCaregiverMode(pin: String): Boolean {
        val savedPin = prefs.getString(KEY_CAREGIVER_PIN, DEFAULT_PIN)
        return if (pin == savedPin) {
            _currentRole.value = UserRole.CAREGIVER
            true
        } else {
            false
        }
    }
    
    /**
     * Check if current role is caregiver
     */
    fun isCaregiverMode(): Boolean {
        return _currentRole.value == UserRole.CAREGIVER
    }
    
    /**
     * Change caregiver PIN
     * Requires old PIN for verification
     */
    fun changePin(oldPin: String, newPin: String): Boolean {
        val savedPin = prefs.getString(KEY_CAREGIVER_PIN, DEFAULT_PIN)
        return if (oldPin == savedPin) {
            prefs.edit().putString(KEY_CAREGIVER_PIN, newPin).apply()
            true
        } else {
            false
        }
    }
    
    /**
     * Get the default/current caregiver PIN (for demo purposes)
     * In production, this should NOT be exposed
     */
    fun getDefaultPin(): String {
        return DEFAULT_PIN
    }
    
    /**
     * Verify PIN without switching role
     */
    fun verifyPin(pin: String): Boolean {
        val savedPin = prefs.getString(KEY_CAREGIVER_PIN, DEFAULT_PIN)
        return pin == savedPin
    }
    
    /**
     * Check if elderly PIN is set
     */
    fun hasElderlyPin(): Boolean {
        return prefs.contains(KEY_ELDERLY_PIN)
    }
    
    /**
     * Set elderly user PIN (first time setup)
     */
    fun setElderlyPin(pin: String) {
        prefs.edit().putString(KEY_ELDERLY_PIN, pin).apply()
    }
    
    /**
     * Verify elderly PIN
     */
    fun verifyElderlyPin(pin: String): Boolean {
        val savedPin = prefs.getString(KEY_ELDERLY_PIN, null) ?: return false
        return pin == savedPin
    }
    
    /**
     * Clear all stored data (for testing/reset)
     */
    fun clearAll() {
        prefs.edit().clear().apply()
        _currentRole.value = UserRole.ELDERLY
    }
}
