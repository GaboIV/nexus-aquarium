package com.nexusaquarium

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement

// Fish model for database operations (mirrors the shared model)
@Serializable
data class Fish(
    val id: Int,
    val name: String,
    val scientificName: String,
    val imageUrl: String? = null
)

class FishService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_FISH =
            """
            CREATE TABLE IF NOT EXISTS fish (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                scientific_name VARCHAR(255) NOT NULL,
                image_url TEXT
            );
            """
        private const val SELECT_ALL_FISH = "SELECT id, name, scientific_name, image_url FROM fish"
        private const val SELECT_FISH_BY_ID = "SELECT id, name, scientific_name, image_url FROM fish WHERE id = ?"
        private const val INSERT_FISH = "INSERT INTO fish (name, scientific_name, image_url) VALUES (?, ?, ?)"
        private const val UPDATE_FISH = "UPDATE fish SET name = ?, scientific_name = ?, image_url = ? WHERE id = ?"
        private const val DELETE_FISH = "DELETE FROM fish WHERE id = ?"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_FISH)
        
        // Insert some initial data if table is empty
        val checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM fish")
        val resultSet = checkStatement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt(1)
        
        if (count == 0) {
            // Insert hardcoded fish data
            val insertStatement = connection.prepareStatement(INSERT_FISH)
            
            // Fish 1: Neon Tetra
            insertStatement.setString(1, "Neon Tetra")
            insertStatement.setString(2, "Paracheirodon innesi")
            insertStatement.setString(3, null)
            insertStatement.executeUpdate()
            
            // Fish 2: Guppy
            insertStatement.setString(1, "Guppy")
            insertStatement.setString(2, "Poecilia reticulata")
            insertStatement.setString(3, null)
            insertStatement.executeUpdate()
            
            // Fish 3: Corydoras Panda
            insertStatement.setString(1, "Corydoras Panda")
            insertStatement.setString(2, "Corydoras panda")
            insertStatement.setString(3, "https://acdn-us.mitiendanube.com/stores/001/242/404/products/cory-panda1-f01ef7197d8564ae5415930549242542-1024-1024.jpg")
            insertStatement.executeUpdate()
        }
    }

    // List all fish
    suspend fun listAll(): List<Fish> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_FISH)
        val resultSet = statement.executeQuery()
        
        val fishList = mutableListOf<Fish>()
        while (resultSet.next()) {
            val fish = Fish(
                id = resultSet.getInt("id"),
                name = resultSet.getString("name"),
                scientificName = resultSet.getString("scientific_name"),
                imageUrl = resultSet.getString("image_url")
            )
            fishList.add(fish)
        }
        return@withContext fishList
    }

    // Read a single fish by ID
    suspend fun read(id: Int): Fish = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_FISH_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            return@withContext Fish(
                id = resultSet.getInt("id"),
                name = resultSet.getString("name"),
                scientificName = resultSet.getString("scientific_name"),
                imageUrl = resultSet.getString("image_url")
            )
        } else {
            throw Exception("Fish not found")
        }
    }

    // Create new fish
    suspend fun create(fish: Fish): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_FISH, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, fish.name)
        statement.setString(2, fish.scientificName)
        statement.setString(3, fish.imageUrl)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted fish")
        }
    }

    // Update a fish
    suspend fun update(id: Int, fish: Fish) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_FISH)
        statement.setString(1, fish.name)
        statement.setString(2, fish.scientificName)
        statement.setString(3, fish.imageUrl)
        statement.setInt(4, id)
        statement.executeUpdate()
    }

    // Delete a fish
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_FISH)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}

