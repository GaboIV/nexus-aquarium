package com.nexusaquarium

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText(
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Nexus Aquarium API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; }
                        .endpoint { background: #ecf0f1; padding: 10px; margin: 10px 0; border-radius: 4px; }
                        .method { font-weight: bold; color: #27ae60; }
                        .swagger-link { 
                            display: inline-block; 
                            background: #3498db; 
                            color: white; 
                            padding: 12px 24px; 
                            text-decoration: none; 
                            border-radius: 4px; 
                            margin-top: 20px;
                        }
                        .swagger-link:hover { background: #2980b9; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🐠 Nexus Aquarium API</h1>
                        <p>API para gestión de acuarios y peces</p>
                        
                        <h2>Endpoints Disponibles:</h2>
                        
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/fish - Listar todos los peces
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/fish/{id} - Obtener pez por ID
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/v1/fish - Crear nuevo pez
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/v1/fish/{id} - Actualizar pez
                        </div>
                        <div class="endpoint">
                            <span class="method">DELETE</span> /api/v1/fish/{id} - Eliminar pez
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/aquariums - Listar todos los acuarios
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/aquariums/{id} - Obtener acuario por ID
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/v1/aquariums - Crear nuevo acuario
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/v1/aquariums/{id} - Actualizar acuario
                        </div>
                        <div class="endpoint">
                            <span class="method">DELETE</span> /api/v1/aquariums/{id} - Eliminar acuario
                        </div>
                        
                        <h2>🔐 Autenticación:</h2>
                        
                        <div class="endpoint">
                            <span class="method">POST</span> /api/v1/auth/register - Registrar nuevo usuario
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/v1/auth/login - Iniciar sesión
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/users/me - Obtener perfil de usuario (requiere autenticación)
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/v1/users/me - Actualizar perfil de usuario (requiere autenticación)
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/v1/users/me/devices - Registrar dispositivo para notificaciones (requiere autenticación)
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/v1/users/me/preferences - Obtener preferencias de usuario (requiere autenticación)
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/v1/users/me/preferences - Actualizar preferencias de usuario (requiere autenticación)
                        </div>
                        
                        <a href="/swagger" class="swagger-link">📚 Ver Documentación Completa (Swagger UI)</a>
                    </div>
                </body>
                </html>
                """.trimIndent(),
                io.ktor.http.ContentType.Text.Html
            )
        }
    }
}
