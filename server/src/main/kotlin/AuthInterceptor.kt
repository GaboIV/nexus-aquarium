package com.nexusaquarium

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("jwt") {
            realm = "nexus-aquarium"
            verifier(
                com.auth0.jwt.JWT.require(com.auth0.jwt.algorithms.Algorithm.HMAC256("nexus-aquarium-secret-key-change-in-production"))
                    .withIssuer("nexus-aquarium")
                    .withAudience("nexus-aquarium-users")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

// Extension function to get user ID from JWT principal
fun ApplicationCall.getUserId(): Int {
    val principal = this.authentication.principal<JWTPrincipal>()
    return principal?.payload?.subject?.toInt() ?: throw Exception("User not authenticated")
}
