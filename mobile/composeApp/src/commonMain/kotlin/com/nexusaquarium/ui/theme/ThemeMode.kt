package com.nexusaquarium.ui.theme

/**
 * Enum representing the available theme modes
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

/**
 * Extension function to get display name for theme mode
 */
fun ThemeMode.getDisplayName(): String {
    return when (this) {
        ThemeMode.LIGHT -> "Claro"
        ThemeMode.DARK -> "Oscuro"
        ThemeMode.SYSTEM -> "Sistema"
    }
}

/**
 * Extension function to get description for theme mode
 */
fun ThemeMode.getDescription(): String {
    return when (this) {
        ThemeMode.LIGHT -> "Tema claro siempre"
        ThemeMode.DARK -> "Tema oscuro siempre"
        ThemeMode.SYSTEM -> "Sigue la configuración del sistema"
    }
}
