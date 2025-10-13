package com.nexusaquarium.config

object AppConfig {
    // Production server configuration
    const val API_BASE_URL = "http://pappstest.com:4301"
    
    // Alternative configurations for different environments
    // const val API_BASE_URL = "http://localhost:8080"  // Local development
    // const val API_BASE_URL = "http://10.0.2.2:8080"   // Android emulator
    // const val API_BASE_URL = "http://192.168.1.100:8080"  // Local network
    
    // API endpoints
    const val FISH_ENDPOINT = "/api/fish"
    const val HEALTH_ENDPOINT = "/health"
    
    // Request timeout (in milliseconds)
    const val REQUEST_TIMEOUT = 30000L
    
    // Retry configuration
    const val MAX_RETRY_ATTEMPTS = 3
}
