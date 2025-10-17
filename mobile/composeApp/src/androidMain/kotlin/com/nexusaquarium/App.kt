package com.nexusaquarium

import android.app.Application
import android.content.Context

// Renamed to avoid name collision with the Composable function `App()` in commonMain
class NexusApplication : Application() {
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
