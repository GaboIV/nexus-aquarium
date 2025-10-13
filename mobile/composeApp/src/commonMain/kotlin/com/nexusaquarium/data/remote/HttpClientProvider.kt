package com.nexusaquarium.data.remote

import com.nexusaquarium.config.AppConfig
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect fun createPlatformHttpClient(): HttpClient

object HttpClientProvider {
    val client by lazy {
        createPlatformHttpClient().config {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    
    // Función para obtener la URL base
    fun getBaseUrl(): String = AppConfig.API_BASE_URL
    
    // Función para obtener la URL completa del endpoint de peces
    fun getFishEndpoint(): String = "${AppConfig.API_BASE_URL}${AppConfig.FISH_ENDPOINT}"
    
    // Función para obtener la URL de health check
    fun getHealthEndpoint(): String = "${AppConfig.API_BASE_URL}${AppConfig.HEALTH_ENDPOINT}"
}

