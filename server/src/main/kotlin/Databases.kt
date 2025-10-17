package com.nexusaquarium

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases() {
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val fishService = FishService(dbConnection)
    val aquariumService = AquariumService(dbConnection)
    val userService = UserService(dbConnection)
    val authService = AuthService()
    
    routing {
        // Fish endpoints
        route("/api/v1") {
            // List all fish
            get("/fish") {
                val fishList = fishService.listAll()
                call.respond(HttpStatusCode.OK, fishList)
            }
            
            // Get fish by ID
            get("/fish/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                try {
                    val fish = fishService.read(id)
                    call.respond(HttpStatusCode.OK, fish)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            
            // Create new fish
            post("/fish") {
                val fish = call.receive<Fish>()
                val id = fishService.create(fish)
                call.respond(HttpStatusCode.Created, id)
            }
            
            // Update fish
            put("/fish/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val fish = call.receive<Fish>()
                fishService.update(id, fish)
                call.respond(HttpStatusCode.OK)
            }
            
            // Delete fish
            delete("/fish/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                fishService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
            
            // Aquarium endpoints
            // List all aquariums
            get("/aquariums") {
                val aquariumList = aquariumService.listAll()
                call.respond(HttpStatusCode.OK, aquariumList)
            }
            
            // Get aquarium by ID
            get("/aquariums/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                try {
                    val aquarium = aquariumService.read(id)
                    call.respond(HttpStatusCode.OK, aquarium)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            
            // Create new aquarium
            post("/aquariums") {
                val aquarium = call.receive<Aquarium>()
                val id = aquariumService.create(aquarium)
                call.respond(HttpStatusCode.Created, id)
            }
            
            // Update aquarium
            put("/aquariums/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val aquarium = call.receive<Aquarium>()
                aquariumService.update(id, aquarium)
                call.respond(HttpStatusCode.OK)
            }
            
            // Delete aquarium
            delete("/aquariums/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                aquariumService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
        
        // Add authentication routes
        configureAuthRoutes(userService, authService)
    }
}
/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        log.info("Using embedded H2 database for testing; replace this flag to use postgres")
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        log.info("Connecting to postgres database at $url")
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
