package com.nexusaquarium.data.remote

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.*

actual fun createPlatformHttpClient(): HttpClient {
    // Create a trust manager that accepts all certificates
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    // Create OkHttpClient with SSL configuration
    val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true } // Disable hostname verification
        .build()

    return HttpClient(OkHttp) {
        engine {
            preconfigured = okHttpClient
        }
    }
}

