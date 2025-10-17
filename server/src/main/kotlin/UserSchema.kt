package com.nexusaquarium

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement
import org.mindrot.jbcrypt.BCrypt

// User model for database operations
@Serializable
data class User(
    val id: Int,
    val email: String,
    val displayName: String?,
    val createdAt: String
)

// User registration request
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String? = null
)

// User login request
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

// User profile response
@Serializable
data class UserProfile(
    val id: Int,
    val email: String,
    val displayName: String?
)

// Auth response with JWT token
@Serializable
data class AuthResponse(
    val token: String
)

// Device registration request
@Serializable
data class DeviceRegistrationRequest(
    val deviceToken: String,
    val deviceOs: String
)

// User preferences
@Serializable
data class UserPreferences(
    val enableTaskReminders: Boolean = true,
    val enableParameterAlerts: Boolean = true
)

class UserService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_USERS =
            """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                email VARCHAR(255) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                display_name VARCHAR(50),
                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
            );
            """
        
        private const val CREATE_TABLE_USER_DEVICES =
            """
            CREATE TABLE IF NOT EXISTS user_devices (
                id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                device_token TEXT NOT NULL,
                device_os VARCHAR(10) NOT NULL,
                last_login TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(user_id, device_token)
            );
            """
        
        private const val CREATE_TABLE_USER_PREFERENCES =
            """
            CREATE TABLE IF NOT EXISTS user_preferences (
                user_id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                enable_task_reminders BOOLEAN DEFAULT TRUE,
                enable_parameter_alerts BOOLEAN DEFAULT TRUE
            );
            """
        
        private const val SELECT_USER_BY_EMAIL = """
            SELECT id, email, password_hash, display_name, created_at
            FROM users WHERE email = ?
        """
        
        private const val SELECT_USER_BY_ID = """
            SELECT id, email, password_hash, display_name, created_at
            FROM users WHERE id = ?
        """
        
        private const val INSERT_USER = """
            INSERT INTO users (email, password_hash, display_name)
            VALUES (?, ?, ?)
        """
        
        private const val INSERT_USER_DEVICE = """
            INSERT INTO user_devices (user_id, device_token, device_os)
            VALUES (?, ?, ?)
            ON CONFLICT (user_id, device_token) 
            DO UPDATE SET last_login = CURRENT_TIMESTAMP
        """
        
        private const val INSERT_USER_PREFERENCES = """
            INSERT INTO user_preferences (user_id, enable_task_reminders, enable_parameter_alerts)
            VALUES (?, ?, ?)
            ON CONFLICT (user_id) DO NOTHING
        """
        
        private const val SELECT_USER_PREFERENCES = """
            SELECT enable_task_reminders, enable_parameter_alerts
            FROM user_preferences WHERE user_id = ?
        """
        
        private const val UPDATE_USER_PREFERENCES = """
            UPDATE user_preferences 
            SET enable_task_reminders = ?, enable_parameter_alerts = ?
            WHERE user_id = ?
        """
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_USERS)
        statement.executeUpdate(CREATE_TABLE_USER_DEVICES)
        statement.executeUpdate(CREATE_TABLE_USER_PREFERENCES)
    }

    // Helper function to map ResultSet to User
    private fun mapResultSetToUser(resultSet: java.sql.ResultSet): User {
        return User(
            id = resultSet.getInt("id"),
            email = resultSet.getString("email"),
            displayName = resultSet.getString("display_name"),
            createdAt = resultSet.getTimestamp("created_at").toString()
        )
    }

    // Register a new user
    suspend fun register(request: RegisterRequest): User = withContext(Dispatchers.IO) {
        // Check if user already exists
        val checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")
        checkStatement.setString(1, request.email)
        val resultSet = checkStatement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt(1)
        
        if (count > 0) {
            throw Exception("User with this email already exists")
        }
        
        // Hash password
        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())
        
        // Insert user
        val statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, request.email)
        statement.setString(2, hashedPassword)
        statement.setString(3, request.displayName)
        statement.executeUpdate()
        
        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            val userId = generatedKeys.getInt(1)
            
            // Create default preferences
            val preferencesStatement = connection.prepareStatement(INSERT_USER_PREFERENCES)
            preferencesStatement.setInt(1, userId)
            preferencesStatement.setBoolean(2, true)
            preferencesStatement.setBoolean(3, true)
            preferencesStatement.executeUpdate()
            
            // Return user
            return@withContext User(
                id = userId,
                email = request.email,
                displayName = request.displayName,
                createdAt = java.time.Instant.now().toString()
            )
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted user")
        }
    }

    // Authenticate user
    suspend fun authenticate(request: LoginRequest): User = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_USER_BY_EMAIL)
        statement.setString(1, request.email)
        val resultSet = statement.executeQuery()
        
        if (resultSet.next()) {
            val hashedPassword = resultSet.getString("password_hash")
            if (BCrypt.checkpw(request.password, hashedPassword)) {
                return@withContext mapResultSetToUser(resultSet)
            } else {
                throw Exception("Invalid credentials")
            }
        } else {
            throw Exception("Invalid credentials")
        }
    }

    // Get user by ID
    suspend fun getUserById(id: Int): User = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_USER_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        
        if (resultSet.next()) {
            return@withContext mapResultSetToUser(resultSet)
        } else {
            throw Exception("User not found")
        }
    }

    // Register device for push notifications
    suspend fun registerDevice(userId: Int, request: DeviceRegistrationRequest) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_USER_DEVICE)
        statement.setInt(1, userId)
        statement.setString(2, request.deviceToken)
        statement.setString(3, request.deviceOs)
        statement.executeUpdate()
    }

    // Get user preferences
    suspend fun getUserPreferences(userId: Int): UserPreferences = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_USER_PREFERENCES)
        statement.setInt(1, userId)
        val resultSet = statement.executeQuery()
        
        if (resultSet.next()) {
            return@withContext UserPreferences(
                enableTaskReminders = resultSet.getBoolean("enable_task_reminders"),
                enableParameterAlerts = resultSet.getBoolean("enable_parameter_alerts")
            )
        } else {
            // Return default preferences if none exist
            return@withContext UserPreferences()
        }
    }

    // Update user preferences
    suspend fun updateUserPreferences(userId: Int, preferences: UserPreferences) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_USER_PREFERENCES)
        statement.setBoolean(1, preferences.enableTaskReminders)
        statement.setBoolean(2, preferences.enableParameterAlerts)
        statement.setInt(3, userId)
        statement.executeUpdate()
    }
}
