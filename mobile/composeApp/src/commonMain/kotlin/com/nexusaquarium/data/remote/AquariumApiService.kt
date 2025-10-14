package com.nexusaquarium.data.remote

import com.nexusaquarium.data.model.Aquarium
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AquariumApiService(private val httpClient: HttpClient) {
    
    suspend fun getAllAquariums(): List<Aquarium> {
        return httpClient.get("/api/v1/aquariums").body()
    }
    
    suspend fun getAquariumById(id: Int): Aquarium {
        return httpClient.get("/api/v1/aquariums/$id").body()
    }
    
    suspend fun createAquarium(aquarium: Aquarium): Int {
        return httpClient.post("/api/v1/aquariums") {
            contentType(ContentType.Application.Json)
            setBody(aquarium)
        }.body()
    }
    
    suspend fun updateAquarium(id: Int, aquarium: Aquarium) {
        httpClient.put("/api/v1/aquariums/$id") {
            contentType(ContentType.Application.Json)
            setBody(aquarium)
        }
    }
    
    suspend fun deleteAquarium(id: Int) {
        httpClient.delete("/api/v1/aquariums/$id")
    }
}
