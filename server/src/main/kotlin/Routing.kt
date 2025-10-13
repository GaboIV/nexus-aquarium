package com.nexusaquarium

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Nexus Aquarium API - Visit /api/v1/fish to see fish list")
        }
    }
}
