package com.nexusaquarium.config

/**
 * Centralized application information configuration
 * This file contains all the app metadata that should be consistent across platforms
 */
object AppInfo {
    // Application basic information
    const val APP_NAME = "Nexus Aquarium"
    const val APP_PACKAGE_NAME = "com.nexusaquarium"
    
    // Version information
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1
    
    // Build information
    const val BUILD_TYPE = "debug" // Will be overridden by build variants
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 34
    const val COMPILE_SDK_VERSION = 36
    
    // App description
    const val APP_DESCRIPTION = "Nexus Aquarium - Fish Management System"
    
    // Icon configuration
    const val ICON_NAME = "ic_launcher"
    const val ROUND_ICON_NAME = "ic_launcher_round"
    
    // Display information
    const val DISPLAY_NAME = "Nexus Aquarium"
    const val SHORT_NAME = "Nexus"
    
    // Company/Developer information
    const val DEVELOPER_NAME = "Nexus Aquarium Team"
    const val COMPANY_NAME = "Nexus Aquarium"
    
    // Contact information
    const val SUPPORT_EMAIL = "support@nexusaquarium.com"
    const val WEBSITE_URL = "https://nexusaquarium.com"
    
    // App store information (for future use)
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=$APP_PACKAGE_NAME"
    const val APP_STORE_URL = "https://apps.apple.com/app/nexus-aquarium/id$APP_PACKAGE_NAME"
    
    // Feature flags and capabilities
    const val SUPPORTS_MULTIPLE_LANGUAGES = true
    const val SUPPORTS_DARK_MODE = true
    const val REQUIRES_INTERNET = true
    
    // Performance settings
    const val DEFAULT_TIMEOUT_MS = 30000L
    const val MAX_RETRY_ATTEMPTS = 3
    const val CACHE_SIZE_MB = 50L
}
