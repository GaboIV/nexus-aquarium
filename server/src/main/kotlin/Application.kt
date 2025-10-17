package com.nexusaquarium

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureAuthentication()
    configureDatabases()
    configureRouting()
    configureSwagger()
}
