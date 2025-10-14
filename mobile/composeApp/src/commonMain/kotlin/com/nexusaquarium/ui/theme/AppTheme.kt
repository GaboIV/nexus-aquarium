package com.nexusaquarium.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

/**
 * Custom color scheme for the application
 */
object AppColors {
    // Primary colors
    val Primary = Color(0xFF2196F3)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFBBDEFB)
    val OnPrimaryContainer = Color(0xFF0D47A1)
    
    // Secondary colors
    val Secondary = Color(0xFF4CAF50)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFC8E6C9)
    val OnSecondaryContainer = Color(0xFF1B5E20)
    
    // Tertiary colors (aquarium theme)
    val Tertiary = Color(0xFF00BCD4)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFFB2EBF2)
    val OnTertiaryContainer = Color(0xFF004D40)
    
    // Error colors
    val Error = Color(0xFFD32F2F)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFCDD2)
    val OnErrorContainer = Color(0xFFB71C1C)
    
    // Surface colors
    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF212121)
    val SurfaceVariant = Color(0xFFF5F5F5)
    val OnSurfaceVariant = Color(0xFF757575)
    val SurfaceContainer = Color(0xFFFAFAFA)
    val SurfaceContainerLow = Color(0xFFF0F0F0)
    val SurfaceContainerLowest = Color(0xFFFFFFFF)
    
    // Background colors
    val Background = Color(0xFFFFFFFF)
    val OnBackground = Color(0xFF212121)
    
    // Outline colors
    val Outline = Color(0xFFBDBDBD)
    val OutlineVariant = Color(0xFFE0E0E0)
}

/**
 * Dark theme colors
 */
object AppDarkColors {
    // Primary colors
    val Primary = Color(0xFF64B5F6)
    val OnPrimary = Color(0xFF0D47A1)
    val PrimaryContainer = Color(0xFF1976D2)
    val OnPrimaryContainer = Color(0xFFBBDEFB)
    
    // Secondary colors
    val Secondary = Color(0xFF81C784)
    val OnSecondary = Color(0xFF1B5E20)
    val SecondaryContainer = Color(0xFF388E3C)
    val OnSecondaryContainer = Color(0xFFC8E6C9)
    
    // Tertiary colors
    val Tertiary = Color(0xFF4DD0E1)
    val OnTertiary = Color(0xFF004D40)
    val TertiaryContainer = Color(0xFF00695C)
    val OnTertiaryContainer = Color(0xFFB2EBF2)
    
    // Error colors
    val Error = Color(0xFFFF5252)
    val OnError = Color(0xFFB71C1C)
    val ErrorContainer = Color(0xFFD32F2F)
    val OnErrorContainer = Color(0xFFFFCDD2)
    
    // Surface colors
    val Surface = Color(0xFF121212)
    val OnSurface = Color(0xFFE0E0E0)
    val SurfaceVariant = Color(0xFF2C2C2C)
    val OnSurfaceVariant = Color(0xFFBDBDBD)
    val SurfaceContainer = Color(0xFF1E1E1E)
    val SurfaceContainerLow = Color(0xFF2A2A2A)
    val SurfaceContainerLowest = Color(0xFF0F0F0F)
    
    // Background colors
    val Background = Color(0xFF0F0F0F)
    val OnBackground = Color(0xFFE0E0E0)
    
    // Outline colors
    val Outline = Color(0xFF616161)
    val OutlineVariant = Color(0xFF424242)
}

/**
 * Light theme color scheme
 */
val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.OnPrimaryContainer,
    
    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.OnSecondaryContainer,
    
    tertiary = AppColors.Tertiary,
    onTertiary = AppColors.OnTertiary,
    tertiaryContainer = AppColors.TertiaryContainer,
    onTertiaryContainer = AppColors.OnTertiaryContainer,
    
    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.OnErrorContainer,
    
    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.OnSurfaceVariant,
    surfaceContainer = AppColors.SurfaceContainer,
    surfaceContainerLow = AppColors.SurfaceContainerLow,
    surfaceContainerLowest = AppColors.SurfaceContainerLowest,
    
    background = AppColors.Background,
    onBackground = AppColors.OnBackground,
    
    outline = AppColors.Outline,
    outlineVariant = AppColors.OutlineVariant
)

/**
 * Dark theme color scheme
 */
val DarkColorScheme = darkColorScheme(
    primary = AppDarkColors.Primary,
    onPrimary = AppDarkColors.OnPrimary,
    primaryContainer = AppDarkColors.PrimaryContainer,
    onPrimaryContainer = AppDarkColors.OnPrimaryContainer,
    
    secondary = AppDarkColors.Secondary,
    onSecondary = AppDarkColors.OnSecondary,
    secondaryContainer = AppDarkColors.SecondaryContainer,
    onSecondaryContainer = AppDarkColors.OnSecondaryContainer,
    
    tertiary = AppDarkColors.Tertiary,
    onTertiary = AppDarkColors.OnTertiary,
    tertiaryContainer = AppDarkColors.TertiaryContainer,
    onTertiaryContainer = AppDarkColors.OnTertiaryContainer,
    
    error = AppDarkColors.Error,
    onError = AppDarkColors.OnError,
    errorContainer = AppDarkColors.ErrorContainer,
    onErrorContainer = AppDarkColors.OnErrorContainer,
    
    surface = AppDarkColors.Surface,
    onSurface = AppDarkColors.OnSurface,
    surfaceVariant = AppDarkColors.SurfaceVariant,
    onSurfaceVariant = AppDarkColors.OnSurfaceVariant,
    surfaceContainer = AppDarkColors.SurfaceContainer,
    surfaceContainerLow = AppDarkColors.SurfaceContainerLow,
    surfaceContainerLowest = AppDarkColors.SurfaceContainerLowest,
    
    background = AppDarkColors.Background,
    onBackground = AppDarkColors.OnBackground,
    
    outline = AppDarkColors.Outline,
    outlineVariant = AppDarkColors.OutlineVariant
)

/**
 * Main theme composable that applies the appropriate color scheme
 */
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }
    
    val colorScheme = if (isDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Typography configuration for the application
 */
val Typography = Typography()
