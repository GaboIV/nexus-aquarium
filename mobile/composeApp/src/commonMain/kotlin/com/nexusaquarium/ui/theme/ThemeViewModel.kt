package com.nexusaquarium.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusaquarium.data.preferences.ThemePreferences
import com.nexusaquarium.data.preferences.InMemoryThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing theme state across the application
 */
class ThemeViewModel(
    private val themePreferences: ThemePreferences = InMemoryThemePreferences()
) : ViewModel() {
    
    private val _themeMode = MutableStateFlow(themePreferences.getThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    init {
        // Initialize with saved preference
        updateThemeMode(themePreferences.getThemeMode())
    }
    
    /**
     * Update the current theme mode
     */
    fun updateThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        themePreferences.setThemeMode(mode)
        updateDarkThemeState()
    }
    
    /**
     * Toggle between light and dark theme
     */
    fun toggleTheme() {
        val newMode = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.SYSTEM
            ThemeMode.SYSTEM -> ThemeMode.LIGHT
        }
        updateThemeMode(newMode)
    }
    
    /**
     * Update the dark theme state based on current mode
     */
    private fun updateDarkThemeState() {
        when (_themeMode.value) {
            ThemeMode.LIGHT -> _isDarkTheme.value = false
            ThemeMode.DARK -> _isDarkTheme.value = true
            ThemeMode.SYSTEM -> {
                // For system mode, we'll use a default behavior
                // In a real implementation, you would check the system theme
                _isDarkTheme.value = false // Default to light for now
            }
        }
    }
    
    /**
     * Get the current theme mode as a string for persistence
     */
    fun getThemeModeString(): String {
        return _themeMode.value.name
    }
    
    /**
     * Set theme mode from string (for persistence)
     */
    fun setThemeModeFromString(modeString: String) {
        try {
            val mode = ThemeMode.valueOf(modeString)
            updateThemeMode(mode)
        } catch (e: IllegalArgumentException) {
            // Default to system if invalid string
            updateThemeMode(ThemeMode.SYSTEM)
        }
    }
}
