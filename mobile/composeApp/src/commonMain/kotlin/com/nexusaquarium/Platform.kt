package com.nexusaquarium

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform