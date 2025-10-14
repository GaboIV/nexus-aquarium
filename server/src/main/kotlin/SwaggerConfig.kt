package com.nexusaquarium

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSwagger() {
    routing {
        // Swagger UI endpoint
        get("/swagger") {
            call.respondText(
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Nexus Aquarium API - Swagger UI</title>
                    <link rel="stylesheet" type="text/css" href="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui.css" />
                    <style>
                        html { box-sizing: border-box; overflow: -moz-scrollbars-vertical; overflow-y: scroll; }
                        *, *:before, *:after { box-sizing: inherit; }
                        body { margin:0; background: #fafafa; }
                    </style>
                </head>
                <body>
                    <div id="swagger-ui"></div>
                    <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-bundle.js"></script>
                    <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-standalone-preset.js"></script>
                    <script>
                        window.onload = function() {
                            const ui = SwaggerUIBundle({
                                url: '/swagger.json',
                                dom_id: '#swagger-ui',
                                deepLinking: true,
                                presets: [
                                    SwaggerUIBundle.presets.apis,
                                    SwaggerUIStandalonePreset
                                ],
                                plugins: [
                                    SwaggerUIBundle.plugins.DownloadUrl
                                ],
                                layout: "StandaloneLayout"
                            });
                        };
                    </script>
                </body>
                </html>
                """.trimIndent(),
                io.ktor.http.ContentType.Text.Html
            )
        }
        
        // OpenAPI JSON specification
        get("/swagger.json") {
            call.respondText(
                """
                {
                    "openapi": "3.0.1",
                    "info": {
                        "title": "Nexus Aquarium API",
                        "description": "API para gestión de acuarios y peces",
                        "version": "1.0.0",
                        "contact": {
                            "name": "Nexus Aquarium Team",
                            "email": "support@nexusaquarium.com"
                        }
                    },
                    "servers": [
                        {
                            "url": "http://localhost:8080",
                            "description": "Servidor de desarrollo"
                        }
                    ],
                    "paths": {
                        "/api/v1/fish": {
                            "get": {
                                "tags": ["Fish"],
                                "summary": "Listar todos los peces",
                                "description": "Obtiene una lista de todos los peces disponibles en la base de datos",
                                "responses": {
                                    "200": {
                                        "description": "Lista de peces obtenida exitosamente",
                                        "content": {
                                            "application/json": {
                                                "schema": {
                                                    "type": "array",
                                                    "items": {
                                                        "${'$'}ref": "#/components/schemas/Fish"
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            "post": {
                                "tags": ["Fish"],
                                "summary": "Crear nuevo pez",
                                "description": "Crea un nuevo pez en la base de datos",
                                "requestBody": {
                                    "required": true,
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "${'$'}ref": "#/components/schemas/Fish"
                                            }
                                        }
                                    }
                                },
                                "responses": {
                                    "201": {
                                        "description": "Pez creado exitosamente",
                                        "content": {
                                            "application/json": {
                                                "schema": {
                                                    "type": "integer",
                                                    "description": "ID del pez creado"
                                                }
                                            }
                                        }
                                    },
                                    "400": {
                                        "description": "Datos de entrada inválidos"
                                    }
                                }
                            }
                        },
                        "/api/v1/fish/{id}": {
                            "get": {
                                "tags": ["Fish"],
                                "summary": "Obtener pez por ID",
                                "description": "Obtiene un pez específico por su ID",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del pez"
                                    }
                                ],
                                "responses": {
                                    "200": {
                                        "description": "Pez encontrado",
                                        "content": {
                                            "application/json": {
                                                    "schema": {
                                                "${'$'}ref": "#/components/schemas/Fish"
                                                }
                                            }
                                        }
                                    },
                                    "404": {
                                        "description": "Pez no encontrado"
                                    }
                                }
                            },
                            "put": {
                                "tags": ["Fish"],
                                "summary": "Actualizar pez",
                                "description": "Actualiza un pez existente",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del pez"
                                    }
                                ],
                                "requestBody": {
                                    "required": true,
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                    "${'$'}ref": "#/components/schemas/Fish"
                                            }
                                        }
                                    }
                                },
                                "responses": {
                                    "200": {
                                        "description": "Pez actualizado exitosamente"
                                    },
                                    "404": {
                                        "description": "Pez no encontrado"
                                    }
                                }
                            },
                            "delete": {
                                "tags": ["Fish"],
                                "summary": "Eliminar pez",
                                "description": "Elimina un pez de la base de datos",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del pez"
                                    }
                                ],
                                "responses": {
                                    "200": {
                                        "description": "Pez eliminado exitosamente"
                                    },
                                    "404": {
                                        "description": "Pez no encontrado"
                                    }
                                }
                            }
                        },
                        "/api/v1/aquariums": {
                            "get": {
                                "tags": ["Aquariums"],
                                "summary": "Listar todos los acuarios",
                                "description": "Obtiene una lista de todos los acuarios disponibles",
                                "responses": {
                                    "200": {
                                        "description": "Lista de acuarios obtenida exitosamente",
                                        "content": {
                                            "application/json": {
                                                "schema": {
                                                    "type": "array",
                                                    "items": {
                                                        "${'$'}ref": "#/components/schemas/Aquarium"
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            "post": {
                                "tags": ["Aquariums"],
                                "summary": "Crear nuevo acuario",
                                "description": "Crea un nuevo acuario en la base de datos",
                                "requestBody": {
                                    "required": true,
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "${'$'}ref": "#/components/schemas/Aquarium"
                                            }
                                        }
                                    }
                                },
                                "responses": {
                                    "201": {
                                        "description": "Acuario creado exitosamente",
                                        "content": {
                                            "application/json": {
                                                "schema": {
                                                    "type": "integer",
                                                    "description": "ID del acuario creado"
                                                }
                                            }
                                        }
                                    },
                                    "400": {
                                        "description": "Datos de entrada inválidos"
                                    }
                                }
                            }
                        },
                        "/api/v1/aquariums/{id}": {
                            "get": {
                                "tags": ["Aquariums"],
                                "summary": "Obtener acuario por ID",
                                "description": "Obtiene un acuario específico por su ID",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del acuario"
                                    }
                                ],
                                "responses": {
                                    "200": {
                                        "description": "Acuario encontrado",
                                        "content": {
                                            "application/json": {
                                                "schema": {
                                                    "${'$'}ref": "#/components/schemas/Aquarium"
                                                }
                                            }
                                        }
                                    },
                                    "404": {
                                        "description": "Acuario no encontrado"
                                    }
                                }
                            },
                            "put": {
                                "tags": ["Aquariums"],
                                "summary": "Actualizar acuario",
                                "description": "Actualiza un acuario existente",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del acuario"
                                    }
                                ],
                                "requestBody": {
                                    "required": true,
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "${'$'}ref": "#/components/schemas/Aquarium"
                                            }
                                        }
                                    }
                                },
                                "responses": {
                                    "200": {
                                        "description": "Acuario actualizado exitosamente"
                                    },
                                    "404": {
                                        "description": "Acuario no encontrado"
                                    }
                                }
                            },
                            "delete": {
                                "tags": ["Aquariums"],
                                "summary": "Eliminar acuario",
                                "description": "Elimina un acuario de la base de datos",
                                "parameters": [
                                    {
                                        "name": "id",
                                        "in": "path",
                                        "required": true,
                                        "schema": {
                                            "type": "integer"
                                        },
                                        "description": "ID del acuario"
                                    }
                                ],
                                "responses": {
                                    "200": {
                                        "description": "Acuario eliminado exitosamente"
                                    },
                                    "404": {
                                        "description": "Acuario no encontrado"
                                    }
                                }
                            }
                        }
                    },
                    "components": {
                        "schemas": {
                            "Fish": {
                                "type": "object",
                                "required": ["id", "commonName", "scientificName", "tempMinC", "tempMaxC", "phMin", "phMax", "maxSizeCm", "minTankSizeLiters", "temperament", "socialBehavior", "tankLevel", "origin", "lifeExpectancyYears", "difficultyLevel", "dietType"],
                                "properties": {
                                    "id": {
                                        "type": "integer",
                                        "description": "ID único del pez"
                                    },
                                    "commonName": {
                                        "type": "string",
                                        "description": "Nombre común del pez"
                                    },
                                    "scientificName": {
                                        "type": "string",
                                        "description": "Nombre científico del pez"
                                    },
                                    "imageUrl": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "URL de la imagen del pez"
                                    },
                                    "imageGallery": {
                                        "type": "array",
                                        "items": {
                                            "type": "string"
                                        },
                                        "nullable": true,
                                        "description": "Galería de imágenes del pez"
                                    },
                                    "tempMinC": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Temperatura mínima en grados Celsius"
                                    },
                                    "tempMaxC": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Temperatura máxima en grados Celsius"
                                    },
                                    "phMin": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "pH mínimo"
                                    },
                                    "phMax": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "pH máximo"
                                    },
                                    "ghMin": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Dureza general mínima"
                                    },
                                    "ghMax": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Dureza general máxima"
                                    },
                                    "maxSizeCm": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Tamaño máximo en centímetros"
                                    },
                                    "minTankSizeLiters": {
                                        "type": "integer",
                                        "description": "Tamaño mínimo del acuario en litros"
                                    },
                                    "temperament": {
                                        "type": "string",
                                        "enum": ["peaceful", "semi_aggressive", "aggressive"],
                                        "description": "Temperamento del pez"
                                    },
                                    "socialBehavior": {
                                        "type": "string",
                                        "enum": ["schooling", "shoaling", "pairs", "solitary"],
                                        "description": "Comportamiento social"
                                    },
                                    "minGroupSize": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Tamaño mínimo del grupo"
                                    },
                                    "tankLevel": {
                                        "type": "string",
                                        "enum": ["bottom", "mid", "top", "all"],
                                        "description": "Nivel del acuario donde habita"
                                    },
                                    "isPredator": {
                                        "type": "boolean",
                                        "description": "Si es un pez depredador"
                                    },
                                    "isFinNipper": {
                                        "type": "boolean",
                                        "description": "Si es un pez que muerde aletas"
                                    },
                                    "origin": {
                                        "type": "string",
                                        "description": "Origen geográfico del pez"
                                    },
                                    "lifeExpectancyYears": {
                                        "type": "integer",
                                        "description": "Esperanza de vida en años"
                                    },
                                    "difficultyLevel": {
                                        "type": "string",
                                        "enum": ["beginner", "intermediate", "advanced"],
                                        "description": "Nivel de dificultad de cuidado"
                                    },
                                    "tankSetupDescription": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Descripción del setup del acuario"
                                    },
                                    "behaviorDescription": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Descripción del comportamiento"
                                    },
                                    "idealTankMates": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Compañeros de acuario ideales"
                                    },
                                    "dietType": {
                                        "type": "string",
                                        "enum": ["omnivore", "carnivore", "herbivore"],
                                        "description": "Tipo de dieta"
                                    },
                                    "recommendedFoods": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Alimentos recomendados"
                                    },
                                    "sexualDimorphism": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Dimorfismo sexual"
                                    },
                                    "breedingDifficulty": {
                                        "type": "string",
                                        "enum": ["easy", "moderate", "difficult"],
                                        "nullable": true,
                                        "description": "Dificultad de reproducción"
                                    },
                                    "breedingMethod": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Método de reproducción"
                                    },
                                    "hasVariants": {
                                        "type": "boolean",
                                        "description": "Si tiene variantes"
                                    },
                                    "variantsDescription": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Descripción de las variantes"
                                    }
                                }
                            },
                            "Aquarium": {
                                "type": "object",
                                "required": ["id", "name", "lengthCm", "heightCm", "depthCm", "totalVolumeLiters", "realVolumeLiters", "aquariumType"],
                                "properties": {
                                    "id": {
                                        "type": "integer",
                                        "description": "ID único del acuario"
                                    },
                                    "name": {
                                        "type": "string",
                                        "description": "Nombre del acuario"
                                    },
                                    "description": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Descripción del acuario"
                                    },
                                    "imageUrl": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "URL de la imagen del acuario"
                                    },
                                    "imageGallery": {
                                        "type": "array",
                                        "items": {
                                            "type": "string"
                                        },
                                        "nullable": true,
                                        "description": "Galería de imágenes del acuario"
                                    },
                                    "lengthCm": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Longitud en centímetros"
                                    },
                                    "heightCm": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Altura en centímetros"
                                    },
                                    "depthCm": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Profundidad en centímetros"
                                    },
                                    "totalVolumeLiters": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Volumen total en litros"
                                    },
                                    "realVolumeLiters": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Volumen real en litros"
                                    },
                                    "volumeReductionPercentage": {
                                        "type": "number",
                                        "format": "double",
                                        "description": "Porcentaje de reducción de volumen"
                                    },
                                    "aquariumType": {
                                        "type": "string",
                                        "enum": ["freshwater", "saltwater", "brackish", "planted", "reef"],
                                        "description": "Tipo de acuario"
                                    },
                                    "setupDate": {
                                        "type": "string",
                                        "format": "date",
                                        "nullable": true,
                                        "description": "Fecha de configuración"
                                    },
                                    "isActive": {
                                        "type": "boolean",
                                        "description": "Si el acuario está activo"
                                    },
                                    "currentTemperature": {
                                        "type": "number",
                                        "format": "double",
                                        "nullable": true,
                                        "description": "Temperatura actual"
                                    },
                                    "desiredTemperature": {
                                        "type": "number",
                                        "format": "double",
                                        "nullable": true,
                                        "description": "Temperatura deseada"
                                    },
                                    "currentPh": {
                                        "type": "number",
                                        "format": "double",
                                        "nullable": true,
                                        "description": "pH actual"
                                    },
                                    "desiredPh": {
                                        "type": "number",
                                        "format": "double",
                                        "nullable": true,
                                        "description": "pH deseado"
                                    },
                                    "currentGh": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Dureza general actual"
                                    },
                                    "desiredGh": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Dureza general deseada"
                                    },
                                    "hasHeater": {
                                        "type": "boolean",
                                        "description": "Si tiene calentador"
                                    },
                                    "hasFilter": {
                                        "type": "boolean",
                                        "description": "Si tiene filtro"
                                    },
                                    "hasLighting": {
                                        "type": "boolean",
                                        "description": "Si tiene iluminación"
                                    },
                                    "hasCo2": {
                                        "type": "boolean",
                                        "description": "Si tiene CO2"
                                    },
                                    "hasAirPump": {
                                        "type": "boolean",
                                        "description": "Si tiene bomba de aire"
                                    },
                                    "substrateType": {
                                        "type": "string",
                                        "enum": ["gravel", "sand", "soil", "bare_bottom"],
                                        "nullable": true,
                                        "description": "Tipo de sustrato"
                                    },
                                    "substrateDepthCm": {
                                        "type": "number",
                                        "format": "double",
                                        "nullable": true,
                                        "description": "Profundidad del sustrato en centímetros"
                                    },
                                    "hasPlants": {
                                        "type": "boolean",
                                        "description": "Si tiene plantas"
                                    },
                                    "hasDecorations": {
                                        "type": "boolean",
                                        "description": "Si tiene decoraciones"
                                    },
                                    "decorationDescription": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Descripción de las decoraciones"
                                    },
                                    "lastWaterChange": {
                                        "type": "string",
                                        "format": "date",
                                        "nullable": true,
                                        "description": "Último cambio de agua"
                                    },
                                    "waterChangeFrequency": {
                                        "type": "integer",
                                        "nullable": true,
                                        "description": "Frecuencia de cambio de agua en días"
                                    },
                                    "lastCleaning": {
                                        "type": "string",
                                        "format": "date",
                                        "nullable": true,
                                        "description": "Última limpieza"
                                    },
                                    "notes": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Notas del acuario"
                                    },
                                    "observations": {
                                        "type": "string",
                                        "nullable": true,
                                        "description": "Observaciones del acuario"
                                    }
                                }
                            }
                        }
                    }
                }
                """.trimIndent(),
                io.ktor.http.ContentType.Application.Json
            )
        }
    }
}
