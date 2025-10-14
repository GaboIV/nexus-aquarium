package com.nexusaquarium.data.preferences

import com.nexusaquarium.ui.theme.ThemeMode

/**
 * Interface for managing theme preferences
 */
interface ThemePreferences {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(themeMode: ThemeMode)
}

/**
 * In-memory implementation of ThemePreferences
 * In a real app, this would use SharedPreferences, DataStore, or similar
 */
class InMemoryThemePreferences : ThemePreferences {
    private var currentTheme: ThemeMode = ThemeMode.SYSTEM
    
    override fun getThemeMode(): ThemeMode {
        return currentTheme
    }
    
    override fun setThemeMode(themeMode: ThemeMode) {
        currentTheme = themeMode
    }
}
