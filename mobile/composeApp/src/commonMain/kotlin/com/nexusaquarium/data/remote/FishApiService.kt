package com.nexusaquarium.data.remote

import com.nexusaquarium.data.model.Fish
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FishApiService(private val client: HttpClient) {
    companion object {
        // Using HttpClientProvider for dynamic URL configuration
        private fun getBaseUrl(): String = HttpClientProvider.getBaseUrl()
        private fun getFishEndpoint(): String = HttpClientProvider.getFishEndpoint()
    }

    // Get all fish
    suspend fun getAllFish(): Result<List<Fish>> {
        return try {
            val response: HttpResponse = client.get(getFishEndpoint())
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get fish by ID
    suspend fun getFishById(id: Int): Result<Fish> {
        return try {
            val response: HttpResponse = client.get("${getFishEndpoint()}/$id")
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Create new fish
    suspend fun createFish(fish: Fish): Result<String> {
        return try {
            val response: HttpResponse = client.post(getFishEndpoint()) {
                contentType(ContentType.Application.Json)
                setBody(fish)
            }
            if (response.status == HttpStatusCode.Created) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update fish
    suspend fun updateFish(id: Int, fish: Fish): Result<Unit> {
        return try {
            val response: HttpResponse = client.put("${getFishEndpoint()}/$id") {
                contentType(ContentType.Application.Json)
                setBody(fish)
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete fish
    suspend fun deleteFish(id: Int): Result<Unit> {
        return try {
            val response: HttpResponse = client.delete("${getFishEndpoint()}/$id")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

