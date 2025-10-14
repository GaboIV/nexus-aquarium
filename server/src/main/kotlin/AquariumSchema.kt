package com.nexusaquarium

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement

// Aquarium model for database operations
@Serializable
data class Aquarium(
    val id: Int,
    
    // Basic Information
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageGallery: List<String>? = null,
    
    // Dimensions (in centimeters)
    val lengthCm: Double,
    val heightCm: Double,
    val depthCm: Double,
    
    // Calculated volumes
    val totalVolumeLiters: Double, // Calculated from dimensions
    val realVolumeLiters: Double,  // Real volume considering substrate, decorations, etc.
    val volumeReductionPercentage: Double = 15.0, // Default 15% reduction for substrate/decorations
    
    // Aquarium Type and Setup
    val aquariumType: String, // 'freshwater', 'saltwater', 'brackish', 'planted', 'reef'
    val setupDate: String? = null, // ISO date string
    val isActive: Boolean = true,
    
    // Water Parameters (current/desired)
    val currentTemperature: Double? = null,
    val desiredTemperature: Double? = null,
    val currentPh: Double? = null,
    val desiredPh: Double? = null,
    val currentGh: Int? = null,
    val desiredGh: Int? = null,
    
    // Equipment
    val hasHeater: Boolean = false,
    val hasFilter: Boolean = false,
    val hasLighting: Boolean = false,
    val hasCo2: Boolean = false,
    val hasAirPump: Boolean = false,
    
    // Substrate and Decorations
    val substrateType: String? = null, // 'gravel', 'sand', 'soil', 'bare_bottom'
    val substrateDepthCm: Double? = null,
    val hasPlants: Boolean = false,
    val hasDecorations: Boolean = false,
    val decorationDescription: String? = null,
    
    // Maintenance
    val lastWaterChange: String? = null, // ISO date string
    val waterChangeFrequency: Int? = null, // days between changes
    val lastCleaning: String? = null, // ISO date string
    
    // Notes and Observations
    val notes: String? = null,
    val observations: String? = null
)

class AquariumService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_AQUARIUM =
            """
            CREATE TABLE IF NOT EXISTS aquariums (
                id SERIAL PRIMARY KEY,
                
                -- Basic Information
                name VARCHAR(255) NOT NULL,
                description TEXT,
                image_url TEXT,
                image_gallery TEXT,  -- JSON array of image URLs
                
                -- Dimensions (in centimeters)
                length_cm DECIMAL(8,2) NOT NULL,
                height_cm DECIMAL(8,2) NOT NULL,
                depth_cm DECIMAL(8,2) NOT NULL,
                
                -- Calculated volumes
                total_volume_liters DECIMAL(10,2) NOT NULL,
                real_volume_liters DECIMAL(10,2) NOT NULL,
                volume_reduction_percentage DECIMAL(5,2) DEFAULT 15.0,
                
                -- Aquarium Type and Setup
                aquarium_type VARCHAR(50) NOT NULL,
                setup_date DATE,
                is_active BOOLEAN DEFAULT TRUE,
                
                -- Water Parameters (current/desired)
                current_temperature DECIMAL(4,2),
                desired_temperature DECIMAL(4,2),
                current_ph DECIMAL(3,1),
                desired_ph DECIMAL(3,1),
                current_gh INTEGER,
                desired_gh INTEGER,
                
                -- Equipment
                has_heater BOOLEAN DEFAULT FALSE,
                has_filter BOOLEAN DEFAULT FALSE,
                has_lighting BOOLEAN DEFAULT FALSE,
                has_co2 BOOLEAN DEFAULT FALSE,
                has_air_pump BOOLEAN DEFAULT FALSE,
                
                -- Substrate and Decorations
                substrate_type VARCHAR(50),
                substrate_depth_cm DECIMAL(5,2),
                has_plants BOOLEAN DEFAULT FALSE,
                has_decorations BOOLEAN DEFAULT FALSE,
                decoration_description TEXT,
                
                -- Maintenance
                last_water_change DATE,
                water_change_frequency INTEGER,
                last_cleaning DATE,
                
                -- Notes and Observations
                notes TEXT,
                observations TEXT
            );
            """
        private const val SELECT_ALL_AQUARIUMS = """
            SELECT id, name, description, image_url, image_gallery,
                   length_cm, height_cm, depth_cm,
                   total_volume_liters, real_volume_liters, volume_reduction_percentage,
                   aquarium_type, setup_date, is_active,
                   current_temperature, desired_temperature, current_ph, desired_ph, current_gh, desired_gh,
                   has_heater, has_filter, has_lighting, has_co2, has_air_pump,
                   substrate_type, substrate_depth_cm, has_plants, has_decorations, decoration_description,
                   last_water_change, water_change_frequency, last_cleaning,
                   notes, observations
            FROM aquariums
        """
        private const val SELECT_AQUARIUM_BY_ID = """
            SELECT id, name, description, image_url, image_gallery,
                   length_cm, height_cm, depth_cm,
                   total_volume_liters, real_volume_liters, volume_reduction_percentage,
                   aquarium_type, setup_date, is_active,
                   current_temperature, desired_temperature, current_ph, desired_ph, current_gh, desired_gh,
                   has_heater, has_filter, has_lighting, has_co2, has_air_pump,
                   substrate_type, substrate_depth_cm, has_plants, has_decorations, decoration_description,
                   last_water_change, water_change_frequency, last_cleaning,
                   notes, observations
            FROM aquariums WHERE id = ?
        """
        private const val INSERT_AQUARIUM = """
            INSERT INTO aquariums (
                name, description, image_url, image_gallery,
                length_cm, height_cm, depth_cm,
                total_volume_liters, real_volume_liters, volume_reduction_percentage,
                aquarium_type, setup_date, is_active,
                current_temperature, desired_temperature, current_ph, desired_ph, current_gh, desired_gh,
                has_heater, has_filter, has_lighting, has_co2, has_air_pump,
                substrate_type, substrate_depth_cm, has_plants, has_decorations, decoration_description,
                last_water_change, water_change_frequency, last_cleaning,
                notes, observations
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
        private const val UPDATE_AQUARIUM = """
            UPDATE aquariums SET
                name = ?, description = ?, image_url = ?, image_gallery = ?,
                length_cm = ?, height_cm = ?, depth_cm = ?,
                total_volume_liters = ?, real_volume_liters = ?, volume_reduction_percentage = ?,
                aquarium_type = ?, setup_date = ?, is_active = ?,
                current_temperature = ?, desired_temperature = ?, current_ph = ?, desired_ph = ?, current_gh = ?, desired_gh = ?,
                has_heater = ?, has_filter = ?, has_lighting = ?, has_co2 = ?, has_air_pump = ?,
                substrate_type = ?, substrate_depth_cm = ?, has_plants = ?, has_decorations = ?, decoration_description = ?,
                last_water_change = ?, water_change_frequency = ?, last_cleaning = ?,
                notes = ?, observations = ?
            WHERE id = ?
        """
        private const val DELETE_AQUARIUM = "DELETE FROM aquariums WHERE id = ?"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_AQUARIUM)
        
        // Insert some initial data if table is empty
        val checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM aquariums")
        val resultSet = checkStatement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt(1)
        
        if (count == 0) {
            // Insert sample aquarium data
            runBlocking {
                insertSampleAquariums()
            }
        }
    }
    
    private suspend fun insertSampleAquariums() {
        // Sample Freshwater Aquarium
        create(Aquarium(
            id = 0,
            name = "Acuario Comunitario",
            description = "Acuario de agua dulce con peces tropicales",
            imageUrl = null,
            imageGallery = null,
            lengthCm = 60.0,
            heightCm = 30.0,
            depthCm = 30.0,
            totalVolumeLiters = 54.0,
            realVolumeLiters = 45.9,
            volumeReductionPercentage = 15.0,
            aquariumType = "freshwater",
            setupDate = "2024-01-15",
            isActive = true,
            currentTemperature = 24.0,
            desiredTemperature = 25.0,
            currentPh = 7.0,
            desiredPh = 7.2,
            currentGh = 8,
            desiredGh = 10,
            hasHeater = true,
            hasFilter = true,
            hasLighting = true,
            hasCo2 = false,
            hasAirPump = true,
            substrateType = "gravel",
            substrateDepthCm = 3.0,
            hasPlants = true,
            hasDecorations = true,
            decorationDescription = "Troncos, rocas y plantas naturales",
            lastWaterChange = "2024-01-20",
            waterChangeFrequency = 7,
            lastCleaning = "2024-01-18",
            notes = "Acuario estable, peces saludables",
            observations = "Necesita más plantas flotantes"
        ))
        
        // Sample Nano Aquarium
        create(Aquarium(
            id = 0,
            name = "Nano Plantado",
            description = "Acuario nano densamente plantado",
            imageUrl = null,
            imageGallery = null,
            lengthCm = 30.0,
            heightCm = 20.0,
            depthCm = 20.0,
            totalVolumeLiters = 12.0,
            realVolumeLiters = 9.6,
            volumeReductionPercentage = 20.0,
            aquariumType = "planted",
            setupDate = "2024-01-10",
            isActive = true,
            currentTemperature = 23.0,
            desiredTemperature = 24.0,
            currentPh = 6.5,
            desiredPh = 6.8,
            currentGh = 4,
            desiredGh = 6,
            hasHeater = true,
            hasFilter = true,
            hasLighting = true,
            hasCo2 = true,
            hasAirPump = false,
            substrateType = "soil",
            substrateDepthCm = 4.0,
            hasPlants = true,
            hasDecorations = true,
            decorationDescription = "Sustrato nutritivo, plantas de crecimiento rápido",
            lastWaterChange = "2024-01-19",
            waterChangeFrequency = 3,
            lastCleaning = "2024-01-17",
            notes = "Requiere mantenimiento frecuente",
            observations = "CO2 funcionando bien, plantas creciendo rápido"
        ))
    }

    // Helper function to map ResultSet to Aquarium
    private fun mapResultSetToAquarium(resultSet: java.sql.ResultSet): Aquarium {
        return Aquarium(
            id = resultSet.getInt("id"),
            name = resultSet.getString("name"),
            description = resultSet.getString("description"),
            imageUrl = resultSet.getString("image_url"),
            imageGallery = resultSet.getString("image_gallery")?.let { 
                kotlinx.serialization.json.Json.decodeFromString(it) 
            },
            lengthCm = resultSet.getDouble("length_cm"),
            heightCm = resultSet.getDouble("height_cm"),
            depthCm = resultSet.getDouble("depth_cm"),
            totalVolumeLiters = resultSet.getDouble("total_volume_liters"),
            realVolumeLiters = resultSet.getDouble("real_volume_liters"),
            volumeReductionPercentage = resultSet.getDouble("volume_reduction_percentage"),
            aquariumType = resultSet.getString("aquarium_type"),
            setupDate = resultSet.getDate("setup_date")?.toString(),
            isActive = resultSet.getBoolean("is_active"),
            currentTemperature = resultSet.getObject("current_temperature") as Double?,
            desiredTemperature = resultSet.getObject("desired_temperature") as Double?,
            currentPh = resultSet.getObject("current_ph") as Double?,
            desiredPh = resultSet.getObject("desired_ph") as Double?,
            currentGh = resultSet.getObject("current_gh") as Int?,
            desiredGh = resultSet.getObject("desired_gh") as Int?,
            hasHeater = resultSet.getBoolean("has_heater"),
            hasFilter = resultSet.getBoolean("has_filter"),
            hasLighting = resultSet.getBoolean("has_lighting"),
            hasCo2 = resultSet.getBoolean("has_co2"),
            hasAirPump = resultSet.getBoolean("has_air_pump"),
            substrateType = resultSet.getString("substrate_type"),
            substrateDepthCm = resultSet.getObject("substrate_depth_cm") as Double?,
            hasPlants = resultSet.getBoolean("has_plants"),
            hasDecorations = resultSet.getBoolean("has_decorations"),
            decorationDescription = resultSet.getString("decoration_description"),
            lastWaterChange = resultSet.getDate("last_water_change")?.toString(),
            waterChangeFrequency = resultSet.getObject("water_change_frequency") as Int?,
            lastCleaning = resultSet.getDate("last_cleaning")?.toString(),
            notes = resultSet.getString("notes"),
            observations = resultSet.getString("observations")
        )
    }

    // List all aquariums
    suspend fun listAll(): List<Aquarium> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_AQUARIUMS)
        val resultSet = statement.executeQuery()
        
        val aquariumList = mutableListOf<Aquarium>()
        while (resultSet.next()) {
            aquariumList.add(mapResultSetToAquarium(resultSet))
        }
        return@withContext aquariumList
    }

    // Read a single aquarium by ID
    suspend fun read(id: Int): Aquarium = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_AQUARIUM_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            return@withContext mapResultSetToAquarium(resultSet)
        } else {
            throw Exception("Aquarium not found")
        }
    }

    // Create new aquarium
    suspend fun create(aquarium: Aquarium): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_AQUARIUM, Statement.RETURN_GENERATED_KEYS)
        var idx = 1
        statement.setString(idx++, aquarium.name)
        statement.setString(idx++, aquarium.description)
        statement.setString(idx++, aquarium.imageUrl)
        statement.setString(idx++, aquarium.imageGallery?.let { kotlinx.serialization.json.Json.encodeToString(kotlinx.serialization.serializer(), it) })
        statement.setDouble(idx++, aquarium.lengthCm)
        statement.setDouble(idx++, aquarium.heightCm)
        statement.setDouble(idx++, aquarium.depthCm)
        statement.setDouble(idx++, aquarium.totalVolumeLiters)
        statement.setDouble(idx++, aquarium.realVolumeLiters)
        statement.setDouble(idx++, aquarium.volumeReductionPercentage)
        statement.setString(idx++, aquarium.aquariumType)
        statement.setDate(idx++, aquarium.setupDate?.let { java.sql.Date.valueOf(it) })
        statement.setBoolean(idx++, aquarium.isActive)
        statement.setObject(idx++, aquarium.currentTemperature)
        statement.setObject(idx++, aquarium.desiredTemperature)
        statement.setObject(idx++, aquarium.currentPh)
        statement.setObject(idx++, aquarium.desiredPh)
        statement.setObject(idx++, aquarium.currentGh)
        statement.setObject(idx++, aquarium.desiredGh)
        statement.setBoolean(idx++, aquarium.hasHeater)
        statement.setBoolean(idx++, aquarium.hasFilter)
        statement.setBoolean(idx++, aquarium.hasLighting)
        statement.setBoolean(idx++, aquarium.hasCo2)
        statement.setBoolean(idx++, aquarium.hasAirPump)
        statement.setString(idx++, aquarium.substrateType)
        statement.setObject(idx++, aquarium.substrateDepthCm)
        statement.setBoolean(idx++, aquarium.hasPlants)
        statement.setBoolean(idx++, aquarium.hasDecorations)
        statement.setString(idx++, aquarium.decorationDescription)
        statement.setDate(idx++, aquarium.lastWaterChange?.let { java.sql.Date.valueOf(it) })
        statement.setObject(idx++, aquarium.waterChangeFrequency)
        statement.setDate(idx++, aquarium.lastCleaning?.let { java.sql.Date.valueOf(it) })
        statement.setString(idx++, aquarium.notes)
        statement.setString(idx++, aquarium.observations)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted aquarium")
        }
    }

    // Update an aquarium
    suspend fun update(id: Int, aquarium: Aquarium) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_AQUARIUM)
        var idx = 1
        statement.setString(idx++, aquarium.name)
        statement.setString(idx++, aquarium.description)
        statement.setString(idx++, aquarium.imageUrl)
        statement.setString(idx++, aquarium.imageGallery?.let { kotlinx.serialization.json.Json.encodeToString(kotlinx.serialization.serializer(), it) })
        statement.setDouble(idx++, aquarium.lengthCm)
        statement.setDouble(idx++, aquarium.heightCm)
        statement.setDouble(idx++, aquarium.depthCm)
        statement.setDouble(idx++, aquarium.totalVolumeLiters)
        statement.setDouble(idx++, aquarium.realVolumeLiters)
        statement.setDouble(idx++, aquarium.volumeReductionPercentage)
        statement.setString(idx++, aquarium.aquariumType)
        statement.setDate(idx++, aquarium.setupDate?.let { java.sql.Date.valueOf(it) })
        statement.setBoolean(idx++, aquarium.isActive)
        statement.setObject(idx++, aquarium.currentTemperature)
        statement.setObject(idx++, aquarium.desiredTemperature)
        statement.setObject(idx++, aquarium.currentPh)
        statement.setObject(idx++, aquarium.desiredPh)
        statement.setObject(idx++, aquarium.currentGh)
        statement.setObject(idx++, aquarium.desiredGh)
        statement.setBoolean(idx++, aquarium.hasHeater)
        statement.setBoolean(idx++, aquarium.hasFilter)
        statement.setBoolean(idx++, aquarium.hasLighting)
        statement.setBoolean(idx++, aquarium.hasCo2)
        statement.setBoolean(idx++, aquarium.hasAirPump)
        statement.setString(idx++, aquarium.substrateType)
        statement.setObject(idx++, aquarium.substrateDepthCm)
        statement.setBoolean(idx++, aquarium.hasPlants)
        statement.setBoolean(idx++, aquarium.hasDecorations)
        statement.setString(idx++, aquarium.decorationDescription)
        statement.setDate(idx++, aquarium.lastWaterChange?.let { java.sql.Date.valueOf(it) })
        statement.setObject(idx++, aquarium.waterChangeFrequency)
        statement.setDate(idx++, aquarium.lastCleaning?.let { java.sql.Date.valueOf(it) })
        statement.setString(idx++, aquarium.notes)
        statement.setString(idx++, aquarium.observations)
        statement.setInt(idx, id)
        statement.executeUpdate()
    }

    // Delete an aquarium
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_AQUARIUM)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}
