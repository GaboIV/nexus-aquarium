// Script de verificación de conectividad para Nexus Aquarium
// Este script puede ser usado para probar la conexión a la API

import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import com.nexusaquarium.config.AppConfig

// Función para probar la conectividad
suspend fun testConnection() {
    val client = HttpClient()
    
    try {
        println("🔍 Probando conectividad...")
        println("Entorno: ${AppConfig.ENVIRONMENT_NAME}")
        println("URL Base: ${AppConfig.API_BASE_URL}")
        println("Endpoint Peces: ${AppConfig.FISH_ENDPOINT}")
        println("URL Completa: ${AppConfig.API_BASE_URL}${AppConfig.FISH_ENDPOINT}")
        
        // Probar endpoint de peces
        val response = client.get("${AppConfig.API_BASE_URL}${AppConfig.FISH_ENDPOINT}")
        val statusCode = response.status.value
        
        if (statusCode == 200) {
            println("✅ Conexión exitosa!")
            println("Status: $statusCode")
            
            // Probar que devuelve datos
            val fishData = response.bodyAsText()
            if (fishData.contains("commonName")) {
                println("✅ Datos de peces recibidos correctamente")
                println("Tamaño de respuesta: ${fishData.length} caracteres")
            } else {
                println("⚠️ Respuesta recibida pero sin datos de peces")
            }
        } else {
            println("❌ Error en la conexión")
            println("Status: $statusCode")
        }
        
    } catch (e: Exception) {
        println("❌ Error de conectividad:")
        println("Tipo: ${e.javaClass.simpleName}")
        println("Mensaje: ${e.message}")
        
        // Sugerencias según el error
        when {
            e.message?.contains("Permission denied") == true -> {
                println("\n💡 Sugerencias:")
                println("1. Verificar permisos de internet en AndroidManifest.xml")
                println("2. Verificar que el servidor esté corriendo")
                println("3. Verificar la URL en AppConfig.kt")
            }
            e.message?.contains("No address associated with hostname") == true -> {
                println("\n💡 Sugerencias:")
                println("1. Verificar la IP del servidor")
                println("2. Para emulador: usar 10.0.2.2")
                println("3. Para dispositivo físico: usar IP real de la computadora")
            }
            e.message?.contains("Connection refused") == true -> {
                println("\n💡 Sugerencias:")
                println("1. Verificar que el servidor esté corriendo")
                println("2. Verificar el puerto (4301)")
                println("3. Verificar firewall")
            }
        }
    } finally {
        client.close()
    }
}

// Función para probar todos los entornos
suspend fun testAllEnvironments() {
    val environments = listOf(
        "LOCAL" to "http://10.0.2.2:4301",
        "DEVELOP" to "http://192.168.1.100:4301", 
        "QA" to "http://qa.nexusaquarium.com:4301",
        "PROD" to "http://pappstest.com:4301"
    )
    
    println("🔍 Probando todos los entornos...")
    println("=" * 50)
    
    environments.forEach { (env, url) ->
        println("\n🌍 Probando entorno: $env")
        println("URL: $url")
        
        val client = HttpClient()
        try {
            val response = client.get("$url/api/v1/fish")
            val statusCode = response.status.value
            
            if (statusCode == 200) {
                println("✅ $env: Conectado")
            } else {
                println("❌ $env: Error $statusCode")
            }
        } catch (e: Exception) {
            println("❌ $env: ${e.message}")
        } finally {
            client.close()
        }
    }
}

// Función principal
fun main() {
    runBlocking {
        println("🐠 Nexus Aquarium - Test de Conectividad")
        println("=" * 50)
        
        // Probar entorno actual
        testConnection()
        
        println("\n" + "=" * 50)
        println("¿Quieres probar todos los entornos? (y/n)")
        // En una app real, esto sería una entrada del usuario
        // Por ahora, comentamos la prueba de todos los entornos
        // testAllEnvironments()
    }
}
