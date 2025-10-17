package com.nexusaquarium

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

class AuthService {
    companion object {
        // In production, this should be loaded from environment variables
        private const val JWT_SECRET = "nexus-aquarium-secret-key-change-in-production"
        private const val JWT_ISSUER = "nexus-aquarium"
        private const val JWT_AUDIENCE = "nexus-aquarium-users"
        private const val JWT_EXPIRATION_HOURS = 168 // 7 days
        
        private val algorithm = Algorithm.HMAC256(JWT_SECRET)
    }

    // Generate JWT token for user
    fun generateToken(userId: Int, email: String): String {
        val now = Date()
        val expiration = Date(now.time + JWT_EXPIRATION_HOURS * 60 * 60 * 1000L)
        
        return JWT.create()
            .withIssuer(JWT_ISSUER)
            .withAudience(JWT_AUDIENCE)
            .withSubject(userId.toString())
            .withClaim("email", email)
            .withIssuedAt(now)
            .withExpiresAt(expiration)
            .sign(algorithm)
    }

    // Verify JWT token and extract user ID
    fun verifyToken(token: String): Int {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUER)
                .withAudience(JWT_AUDIENCE)
                .build()
            
            val decodedJWT = verifier.verify(token)
            decodedJWT.subject.toInt()
        } catch (e: JWTVerificationException) {
            throw Exception("Invalid token")
        }
    }

    // Extract email from token
    fun getEmailFromToken(token: String): String {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUER)
                .withAudience(JWT_AUDIENCE)
                .build()
            
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim("email").asString()
        } catch (e: JWTVerificationException) {
            throw Exception("Invalid token")
        }
    }
}
