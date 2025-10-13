package com.nexusaquarium.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*

actual fun createPlatformHttpClient(): HttpClient {
    return HttpClient(CIO)
}

