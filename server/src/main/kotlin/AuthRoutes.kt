package com.nexusaquarium

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthRoutes(userService: UserService, authService: AuthService) {
    routing {
        route("/api/v1/auth") {
            // Register new user
            post("/register") {
                try {
                    val request = call.receive<RegisterRequest>()
                    
                    // Validate email format
                    if (!isValidEmail(request.email)) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                        return@post
                    }
                    
                    // Validate password strength
                    if (!isValidPassword(request.password)) {
                        call.respond(HttpStatusCode.BadRequest, "Password must be at least 8 characters with at least one letter and one number")
                        return@post
                    }
                    
                    val user = userService.register(request)
                    val token = authService.generateToken(user.id, user.email)
                    
                    call.respond(HttpStatusCode.Created, AuthResponse(token))
                } catch (e: Exception) {
                    when {
                        e.message?.contains("already exists") == true -> {
                            call.respond(HttpStatusCode.Conflict, "User with this email already exists")
                        }
                        else -> {
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "Registration failed")
                        }
                    }
                }
            }
            
            // Login user
            post("/login") {
                try {
                    val request = call.receive<LoginRequest>()
                    val user = userService.authenticate(request)
                    val token = authService.generateToken(user.id, user.email)
                    
                    call.respond(HttpStatusCode.OK, AuthResponse(token))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            }
        }
        
        // Protected routes
        authenticate("jwt") {
            route("/api/v1/users") {
                // Get current user profile
                get("/me") {
                    try {
                        val userId = call.getUserId()
                        val user = userService.getUserById(userId)
                        val profile = UserProfile(
                            id = user.id,
                            email = user.email,
                            displayName = user.displayName
                        )
                        call.respond(HttpStatusCode.OK, profile)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }
                
                // Update user profile
                put("/me") {
                    try {
                        val userId = call.getUserId()
                        val request = call.receive<Map<String, String>>()
                        
                        // For now, we only support updating display name
                        // In the future, we can add password change functionality
                        if (request.containsKey("displayName")) {
                            // Update display name logic would go here
                            call.respond(HttpStatusCode.OK, "Profile updated")
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Invalid update request")
                        }
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.message ?: "Update failed")
                    }
                }
                
                // Register device for push notifications
                post("/me/devices") {
                    try {
                        val userId = call.getUserId()
                        val request = call.receive<DeviceRegistrationRequest>()
                        userService.registerDevice(userId, request)
                        call.respond(HttpStatusCode.OK, "Device registered")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.message ?: "Device registration failed")
                    }
                }
                
                // Get user preferences
                get("/me/preferences") {
                    try {
                        val userId = call.getUserId()
                        val preferences = userService.getUserPreferences(userId)
                        call.respond(HttpStatusCode.OK, preferences)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to get preferences")
                    }
                }
                
                // Update user preferences
                put("/me/preferences") {
                    try {
                        val userId = call.getUserId()
                        val preferences = call.receive<UserPreferences>()
                        userService.updateUserPreferences(userId, preferences)
                        call.respond(HttpStatusCode.OK, "Preferences updated")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.message ?: "Failed to update preferences")
                    }
                }
            }
        }
    }
}

// Helper functions for validation
private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(email)
}

private fun isValidPassword(password: String): Boolean {
    if (password.length < 8) return false
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    return hasLetter && hasDigit
}
