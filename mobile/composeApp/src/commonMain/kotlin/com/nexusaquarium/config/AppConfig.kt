package com.nexusaquarium.config

enum class Environment {
    LOCAL,
    DEVELOP,
    QA,
    PROD
}

object AppConfig {
    // Current environment - Change this to switch environments
    val CURRENT_ENVIRONMENT = Environment.LOCAL
    
    // Environment-specific configurations
    private val environmentConfigs = mapOf(
        Environment.LOCAL to EnvironmentConfig(
            baseUrl = "http://localhost:4301", // Android emulator localhost
            timeout = 30000L,
            maxRetries = 3
        ),
        Environment.DEVELOP to EnvironmentConfig(
            baseUrl = "http://pappstest.com:4301", // Local network development
            timeout = 30000L,
            maxRetries = 3
        ),
        Environment.QA to EnvironmentConfig(
            baseUrl = "http://api-qa.nexusaquarium.com:4301", // QA server
            timeout = 30000L,
            maxRetries = 3
        ),
        Environment.PROD to EnvironmentConfig(
            baseUrl = "http://api.nexusaquarium.com:4301", // Production server
            timeout = 30000L,
            maxRetries = 3
        )
    )
    
    // Get current configuration
    private val currentConfig = environmentConfigs[CURRENT_ENVIRONMENT]!!
    
    // Public configuration properties
    val API_BASE_URL = currentConfig.baseUrl
    val REQUEST_TIMEOUT = currentConfig.timeout
    val MAX_RETRY_ATTEMPTS = currentConfig.maxRetries
    
    // API endpoints
    const val FISH_ENDPOINT = "/api/v1/fish"
    const val HEALTH_ENDPOINT = "/health"
    
    // Environment info
    val ENVIRONMENT_NAME = CURRENT_ENVIRONMENT.name
    val IS_LOCAL = CURRENT_ENVIRONMENT == Environment.LOCAL
    val IS_DEVELOP = CURRENT_ENVIRONMENT == Environment.DEVELOP
    val IS_QA = CURRENT_ENVIRONMENT == Environment.QA
    val IS_PROD = CURRENT_ENVIRONMENT == Environment.PROD
}

data class EnvironmentConfig(
    val baseUrl: String,
    val timeout: Long,
    val maxRetries: Int
)
