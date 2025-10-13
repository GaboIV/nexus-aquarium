package com.nexusaquarium

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement

// Fish model for database operations (mirrors the shared model)
@Serializable
data class Fish(
    val id: Int,
    
    // Basic Information
    val commonName: String,
    val scientificName: String,
    val imageUrl: String? = null,
    val imageGallery: List<String>? = null,
    
    // Water Parameters
    val tempMinC: Double,
    val tempMaxC: Double,
    val phMin: Double,
    val phMax: Double,
    val ghMin: Int? = null,  // General Hardness (optional)
    val ghMax: Int? = null,
    
    // Size and Tank Requirements
    val maxSizeCm: Double,
    val minTankSizeLiters: Int,
    
    // Behavior and Compatibility
    val temperament: String,  // 'peaceful', 'semi_aggressive', 'aggressive'
    val socialBehavior: String,  // 'schooling', 'shoaling', 'pairs', 'solitary'
    val minGroupSize: Int? = null,
    val tankLevel: String,  // 'bottom', 'mid', 'top', 'all'
    val isPredator: Boolean = false,
    val isFinNipper: Boolean = false,
    
    // Overview Information
    val origin: String,
    val lifeExpectancyYears: Int,
    val difficultyLevel: String,  // 'beginner', 'intermediate', 'advanced'
    
    // Tank Setup
    val tankSetupDescription: String? = null,
    
    // Detailed Behavior
    val behaviorDescription: String? = null,
    val idealTankMates: String? = null,
    
    // Diet
    val dietType: String,  // 'omnivore', 'carnivore', 'herbivore'
    val recommendedFoods: String? = null,
    
    // Breeding
    val sexualDimorphism: String? = null,
    val breedingDifficulty: String? = null,  // 'easy', 'moderate', 'difficult'
    val breedingMethod: String? = null,
    
    // Variants (for species with multiple color morphs)
    val hasVariants: Boolean = false,
    val variantsDescription: String? = null
)

class FishService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_FISH =
            """
            CREATE TABLE IF NOT EXISTS fish (
                id SERIAL PRIMARY KEY,
                
                -- Basic Information
                common_name VARCHAR(255) NOT NULL,
                scientific_name VARCHAR(255) NOT NULL,
                image_url TEXT,
                image_gallery TEXT,  -- JSON array of image URLs
                
                -- Water Parameters
                temp_min_c DECIMAL(4,2) NOT NULL,
                temp_max_c DECIMAL(4,2) NOT NULL,
                ph_min DECIMAL(3,1) NOT NULL,
                ph_max DECIMAL(3,1) NOT NULL,
                gh_min INTEGER,
                gh_max INTEGER,
                
                -- Size and Tank Requirements
                max_size_cm DECIMAL(5,2) NOT NULL,
                min_tank_size_liters INTEGER NOT NULL,
                
                -- Behavior and Compatibility
                temperament VARCHAR(50) NOT NULL,
                social_behavior VARCHAR(50) NOT NULL,
                min_group_size INTEGER,
                tank_level VARCHAR(20) NOT NULL,
                is_predator BOOLEAN DEFAULT FALSE,
                is_fin_nipper BOOLEAN DEFAULT FALSE,
                
                -- Overview Information
                origin TEXT NOT NULL,
                life_expectancy_years INTEGER NOT NULL,
                difficulty_level VARCHAR(50) NOT NULL,
                
                -- Tank Setup
                tank_setup_description TEXT,
                
                -- Detailed Behavior
                behavior_description TEXT,
                ideal_tank_mates TEXT,
                
                -- Diet
                diet_type VARCHAR(50) NOT NULL,
                recommended_foods TEXT,
                
                -- Breeding
                sexual_dimorphism TEXT,
                breeding_difficulty VARCHAR(50),
                breeding_method TEXT,
                
                -- Variants
                has_variants BOOLEAN DEFAULT FALSE,
                variants_description TEXT
            );
            """
        private const val SELECT_ALL_FISH = """
            SELECT id, common_name, scientific_name, image_url, image_gallery,
                   temp_min_c, temp_max_c, ph_min, ph_max, gh_min, gh_max,
                   max_size_cm, min_tank_size_liters,
                   temperament, social_behavior, min_group_size, tank_level, is_predator, is_fin_nipper,
                   origin, life_expectancy_years, difficulty_level,
                   tank_setup_description, behavior_description, ideal_tank_mates,
                   diet_type, recommended_foods,
                   sexual_dimorphism, breeding_difficulty, breeding_method,
                   has_variants, variants_description
            FROM fish
        """
        private const val SELECT_FISH_BY_ID = """
            SELECT id, common_name, scientific_name, image_url, image_gallery,
                   temp_min_c, temp_max_c, ph_min, ph_max, gh_min, gh_max,
                   max_size_cm, min_tank_size_liters,
                   temperament, social_behavior, min_group_size, tank_level, is_predator, is_fin_nipper,
                   origin, life_expectancy_years, difficulty_level,
                   tank_setup_description, behavior_description, ideal_tank_mates,
                   diet_type, recommended_foods,
                   sexual_dimorphism, breeding_difficulty, breeding_method,
                   has_variants, variants_description
            FROM fish WHERE id = ?
        """
        private const val INSERT_FISH = """
            INSERT INTO fish (
                common_name, scientific_name, image_url, image_gallery,
                temp_min_c, temp_max_c, ph_min, ph_max, gh_min, gh_max,
                max_size_cm, min_tank_size_liters,
                temperament, social_behavior, min_group_size, tank_level, is_predator, is_fin_nipper,
                origin, life_expectancy_years, difficulty_level,
                tank_setup_description, behavior_description, ideal_tank_mates,
                diet_type, recommended_foods,
                sexual_dimorphism, breeding_difficulty, breeding_method,
                has_variants, variants_description
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
        private const val UPDATE_FISH = """
            UPDATE fish SET
                common_name = ?, scientific_name = ?, image_url = ?, image_gallery = ?,
                temp_min_c = ?, temp_max_c = ?, ph_min = ?, ph_max = ?, gh_min = ?, gh_max = ?,
                max_size_cm = ?, min_tank_size_liters = ?,
                temperament = ?, social_behavior = ?, min_group_size = ?, tank_level = ?, is_predator = ?, is_fin_nipper = ?,
                origin = ?, life_expectancy_years = ?, difficulty_level = ?,
                tank_setup_description = ?, behavior_description = ?, ideal_tank_mates = ?,
                diet_type = ?, recommended_foods = ?,
                sexual_dimorphism = ?, breeding_difficulty = ?, breeding_method = ?,
                has_variants = ?, variants_description = ?
            WHERE id = ?
        """
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
            // Insert detailed fish data
            runBlocking {
                insertSampleFish()
            }
        }
    }
    
    private suspend fun insertSampleFish() {
        // Neon Tetra
        create(Fish(
            id = 0,
            commonName = "Pez Neón",
            scientificName = "Paracheirodon innesi",
            imageUrl = "https://acuariosplantados.com.ar/wp-content/uploads/2022/02/p-2288-tetra_neon_cardenal.jpg",
            imageGallery = null,
            tempMinC = 22.0,
            tempMaxC = 26.0,
            phMin = 6.0,
            phMax = 7.5,
            ghMin = 1,
            ghMax = 10,
            maxSizeCm = 4.0,
            minTankSizeLiters = 40,
            temperament = "peaceful",
            socialBehavior = "schooling",
            minGroupSize = 6,
            tankLevel = "mid",
            isPredator = false,
            isFinNipper = false,
            origin = "Sudamérica, cuenca del Amazonas (Colombia, Perú, Brasil)",
            lifeExpectancyYears = 5,
            difficultyLevel = "beginner",
            tankSetupDescription = "Acuario densamente plantado con zonas abiertas para nadar. Prefiere iluminación tenue y agua ligeramente ácida.",
            behaviorDescription = "Extremadamente pacífico y sociable. Ideal para acuarios comunitarios. No debe mantenerse con peces grandes o agresivos.",
            idealTankMates = "Tetras, Rasboras, Corydoras, Otocinclus, peces lápiz, camarones enanos",
            dietType = "omnivore",
            recommendedFoods = "Acepta escamas de alta calidad, alimentos congelados o vivos como artemia, dafnia y larvas de mosquito.",
            sexualDimorphism = "Las hembras son ligeramente más grandes y tienen el vientre más redondeado. Los machos son más esbeltos.",
            breedingDifficulty = "moderate",
            breedingMethod = "Ponedor de huevos dispersor. Requiere agua muy blanda y ácida. Los padres deben ser retirados después del desove.",
            hasVariants = false,
            variantsDescription = null
        ))
        
        // Guppy
        create(Fish(
            id = 0,
            commonName = "Guppy",
            scientificName = "Poecilia reticulata",
            imageUrl = "https://cdn.shopify.com/s/files/1/0311/3149/files/shutterstock_1248008848.jpg?v=1580426748",
            imageGallery = null,
            tempMinC = 22.0,
            tempMaxC = 28.0,
            phMin = 6.8,
            phMax = 8.0,
            ghMin = 8,
            ghMax = 12,
            maxSizeCm = 6.0,
            minTankSizeLiters = 40,
            temperament = "peaceful",
            socialBehavior = "shoaling",
            minGroupSize = 3,
            tankLevel = "top",
            isPredator = false,
            isFinNipper = false,
            origin = "Sudamérica (Venezuela, Barbados, Trinidad)",
            lifeExpectancyYears = 2,
            difficultyLevel = "beginner",
            tankSetupDescription = "Acuario con plantas flotantes y vegetación densa en los bordes. Prefiere agua con movimiento moderado.",
            behaviorDescription = "Muy activo y pacífico. Los machos pueden perseguir a las hembras constantemente. Se recomienda mantener 2-3 hembras por cada macho.",
            idealTankMates = "Platys, Mollys, Tetras pacíficos, Corydoras, Otocinclus",
            dietType = "omnivore",
            recommendedFoods = "Escamas de alta calidad, alimento en gel, artemia, dafnia, espirulina. Requieren variedad en su dieta.",
            sexualDimorphism = "Muy evidente. Los machos son más pequeños, coloridos y tienen gonopodio. Las hembras son más grandes y robustas.",
            breedingDifficulty = "easy",
            breedingMethod = "Vivíparo. Las hembras dan a luz crías completamente formadas cada 28-30 días. Pueden tener 20-100 alevines por parto.",
            hasVariants = true,
            variantsDescription = "Existen cientos de variedades: Endler, Tuxedo, Cobra, Mosaico, Delta, Velo, Albino, etc. Todos comparten los mismos requisitos de cuidado."
        ))
        
        // Corydoras Panda
        create(Fish(
            id = 0,
            commonName = "Corydora Panda",
            scientificName = "Corydoras panda",
            imageUrl = "https://acdn-us.mitiendanube.com/stores/001/242/404/products/cory-panda1-f01ef7197d8564ae5415930549242542-1024-1024.jpg",
            imageGallery = null,
            tempMinC = 20.0,
            tempMaxC = 25.0,
            phMin = 6.0,
            phMax = 7.5,
            ghMin = 2,
            ghMax = 12,
            maxSizeCm = 5.0,
            minTankSizeLiters = 60,
            temperament = "peaceful",
            socialBehavior = "schooling",
            minGroupSize = 6,
            tankLevel = "bottom",
            isPredator = false,
            isFinNipper = false,
            origin = "Sudamérica, Perú (río Ucayali y afluentes)",
            lifeExpectancyYears = 10,
            difficultyLevel = "beginner",
            tankSetupDescription = "Sustrato arenoso suave y fino para proteger sus barbillas. Requiere escondites (cuevas, troncos, plantas).",
            behaviorDescription = "Extremadamente pacífico y gregario. Pasa el tiempo buscando comida en el fondo. Es muy activo y social con otros Corydoras.",
            idealTankMates = "Tetras, Rasboras, Guppys, Platys, Otocinclus, camarones. Cualquier pez pacífico de niveles medio y superior.",
            dietType = "omnivore",
            recommendedFoods = "Pastillas de fondo, obleas de algas, alimento congelado (artemia, larvas de mosquito, bloodworms).",
            sexualDimorphism = "Las hembras son más grandes y tienen el cuerpo más redondeado visto desde arriba. Los machos son más esbeltos.",
            breedingDifficulty = "moderate",
            breedingMethod = "Ponedor de huevos adhesivos. La hembra deposita los huevos en superficies lisas como el cristal o hojas anchas.",
            hasVariants = false,
            variantsDescription = "Existen más de 160 especies de Corydoras con requisitos similares pero pueden variar en temperatura y tamaño."
        ))
        
        // Platy
        create(Fish(
            id = 0,
            commonName = "Platy",
            scientificName = "Xiphophorus maculatus",
            imageUrl = "https://cdn.shopify.com/s/files/1/0311/3149/files/male_and_female_platy.jpg?v=1599842837",
            imageGallery = null,
            tempMinC = 20.0,
            tempMaxC = 26.0,
            phMin = 7.0,
            phMax = 8.2,
            ghMin = 10,
            ghMax = 25,
            maxSizeCm = 6.0,
            minTankSizeLiters = 60,
            temperament = "peaceful",
            socialBehavior = "shoaling",
            minGroupSize = 3,
            tankLevel = "mid",
            isPredator = false,
            isFinNipper = false,
            origin = "Centroamérica (México, Guatemala, Honduras)",
            lifeExpectancyYears = 3,
            difficultyLevel = "beginner",
            tankSetupDescription = "Acuario con plantas y espacio abierto para nadar. Toleran una amplia variedad de condiciones. Prefieren agua dura y alcalina.",
            behaviorDescription = "Muy pacífico y activo. Los machos pueden mostrar comportamiento territorial leve entre ellos. Se recomienda mantener 1 macho por cada 2-3 hembras.",
            idealTankMates = "Guppys, Mollys, Espadas, Tetras pacíficos, Corydoras, Otocinclus, Gouramis enanos",
            dietType = "omnivore",
            recommendedFoods = "Escamas de alta calidad, alimento en gel, espirulina, verduras blanqueadas (calabacín, espinaca). Complementar con artemia y dafnia.",
            sexualDimorphism = "Los machos tienen gonopodio (aleta anal modificada) y son más esbeltos. Las hembras tienen aleta anal en forma de abanico y son más robustas.",
            breedingDifficulty = "easy",
            breedingMethod = "Vivíparo. Las hembras dan a luz crías vivas cada 28-40 días. Pueden tener 20-80 alevines por parto.",
            hasVariants = true,
            variantsDescription = "Variedades populares: Mickey Mouse, Sunset, Tuxedo, Wagtail, Blue, Red Coral. Todos tienen los mismos cuidados."
        ))
    }

    // Helper function to map ResultSet to Fish
    private fun mapResultSetToFish(resultSet: java.sql.ResultSet): Fish {
        return Fish(
            id = resultSet.getInt("id"),
            commonName = resultSet.getString("common_name"),
            scientificName = resultSet.getString("scientific_name"),
            imageUrl = resultSet.getString("image_url"),
            imageGallery = resultSet.getString("image_gallery")?.let { 
                kotlinx.serialization.json.Json.decodeFromString(it) 
            },
            tempMinC = resultSet.getDouble("temp_min_c"),
            tempMaxC = resultSet.getDouble("temp_max_c"),
            phMin = resultSet.getDouble("ph_min"),
            phMax = resultSet.getDouble("ph_max"),
            ghMin = resultSet.getObject("gh_min") as Int?,
            ghMax = resultSet.getObject("gh_max") as Int?,
            maxSizeCm = resultSet.getDouble("max_size_cm"),
            minTankSizeLiters = resultSet.getInt("min_tank_size_liters"),
            temperament = resultSet.getString("temperament"),
            socialBehavior = resultSet.getString("social_behavior"),
            minGroupSize = resultSet.getObject("min_group_size") as Int?,
            tankLevel = resultSet.getString("tank_level"),
            isPredator = resultSet.getBoolean("is_predator"),
            isFinNipper = resultSet.getBoolean("is_fin_nipper"),
            origin = resultSet.getString("origin"),
            lifeExpectancyYears = resultSet.getInt("life_expectancy_years"),
            difficultyLevel = resultSet.getString("difficulty_level"),
            tankSetupDescription = resultSet.getString("tank_setup_description"),
            behaviorDescription = resultSet.getString("behavior_description"),
            idealTankMates = resultSet.getString("ideal_tank_mates"),
            dietType = resultSet.getString("diet_type"),
            recommendedFoods = resultSet.getString("recommended_foods"),
            sexualDimorphism = resultSet.getString("sexual_dimorphism"),
            breedingDifficulty = resultSet.getString("breeding_difficulty"),
            breedingMethod = resultSet.getString("breeding_method"),
            hasVariants = resultSet.getBoolean("has_variants"),
            variantsDescription = resultSet.getString("variants_description")
        )
    }

    // List all fish
    suspend fun listAll(): List<Fish> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_FISH)
        val resultSet = statement.executeQuery()
        
        val fishList = mutableListOf<Fish>()
        while (resultSet.next()) {
            fishList.add(mapResultSetToFish(resultSet))
        }
        return@withContext fishList
    }

    // Read a single fish by ID
    suspend fun read(id: Int): Fish = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_FISH_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            return@withContext mapResultSetToFish(resultSet)
        } else {
            throw Exception("Fish not found")
        }
    }

    // Create new fish
    suspend fun create(fish: Fish): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_FISH, Statement.RETURN_GENERATED_KEYS)
        var idx = 1
        statement.setString(idx++, fish.commonName)
        statement.setString(idx++, fish.scientificName)
        statement.setString(idx++, fish.imageUrl)
        statement.setString(idx++, fish.imageGallery?.let { kotlinx.serialization.json.Json.encodeToString(kotlinx.serialization.serializer(), it) })
        statement.setDouble(idx++, fish.tempMinC)
        statement.setDouble(idx++, fish.tempMaxC)
        statement.setDouble(idx++, fish.phMin)
        statement.setDouble(idx++, fish.phMax)
        statement.setObject(idx++, fish.ghMin)
        statement.setObject(idx++, fish.ghMax)
        statement.setDouble(idx++, fish.maxSizeCm)
        statement.setInt(idx++, fish.minTankSizeLiters)
        statement.setString(idx++, fish.temperament)
        statement.setString(idx++, fish.socialBehavior)
        statement.setObject(idx++, fish.minGroupSize)
        statement.setString(idx++, fish.tankLevel)
        statement.setBoolean(idx++, fish.isPredator)
        statement.setBoolean(idx++, fish.isFinNipper)
        statement.setString(idx++, fish.origin)
        statement.setInt(idx++, fish.lifeExpectancyYears)
        statement.setString(idx++, fish.difficultyLevel)
        statement.setString(idx++, fish.tankSetupDescription)
        statement.setString(idx++, fish.behaviorDescription)
        statement.setString(idx++, fish.idealTankMates)
        statement.setString(idx++, fish.dietType)
        statement.setString(idx++, fish.recommendedFoods)
        statement.setString(idx++, fish.sexualDimorphism)
        statement.setString(idx++, fish.breedingDifficulty)
        statement.setString(idx++, fish.breedingMethod)
        statement.setBoolean(idx++, fish.hasVariants)
        statement.setString(idx, fish.variantsDescription)
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
        var idx = 1
        statement.setString(idx++, fish.commonName)
        statement.setString(idx++, fish.scientificName)
        statement.setString(idx++, fish.imageUrl)
        statement.setString(idx++, fish.imageGallery?.let { kotlinx.serialization.json.Json.encodeToString(kotlinx.serialization.serializer(), it) })
        statement.setDouble(idx++, fish.tempMinC)
        statement.setDouble(idx++, fish.tempMaxC)
        statement.setDouble(idx++, fish.phMin)
        statement.setDouble(idx++, fish.phMax)
        statement.setObject(idx++, fish.ghMin)
        statement.setObject(idx++, fish.ghMax)
        statement.setDouble(idx++, fish.maxSizeCm)
        statement.setInt(idx++, fish.minTankSizeLiters)
        statement.setString(idx++, fish.temperament)
        statement.setString(idx++, fish.socialBehavior)
        statement.setObject(idx++, fish.minGroupSize)
        statement.setString(idx++, fish.tankLevel)
        statement.setBoolean(idx++, fish.isPredator)
        statement.setBoolean(idx++, fish.isFinNipper)
        statement.setString(idx++, fish.origin)
        statement.setInt(idx++, fish.lifeExpectancyYears)
        statement.setString(idx++, fish.difficultyLevel)
        statement.setString(idx++, fish.tankSetupDescription)
        statement.setString(idx++, fish.behaviorDescription)
        statement.setString(idx++, fish.idealTankMates)
        statement.setString(idx++, fish.dietType)
        statement.setString(idx++, fish.recommendedFoods)
        statement.setString(idx++, fish.sexualDimorphism)
        statement.setString(idx++, fish.breedingDifficulty)
        statement.setString(idx++, fish.breedingMethod)
        statement.setBoolean(idx++, fish.hasVariants)
        statement.setString(idx++, fish.variantsDescription)
        statement.setInt(idx, id)
        statement.executeUpdate()
    }

    // Delete a fish
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_FISH)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}
