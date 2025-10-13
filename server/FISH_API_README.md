# Fish API - Nexus Aquarium

## Configuración de PostgreSQL

### 1. Iniciar la base de datos con Docker

```bash
docker-compose up -d
```

Esto iniciará un contenedor de PostgreSQL con:
- **Usuario**: admin
- **Contraseña**: admin
- **Base de datos**: nexus_aquarium_db
- **Puerto**: 5432

### 2. Verificar que el contenedor esté corriendo

```bash
docker ps
```

Deberías ver el contenedor `nexus-aquarium-db` en la lista.

### 3. Configuración del servidor

El archivo `application.yaml` ya está configurado para conectarse a PostgreSQL:

```yaml
postgres:
  url: "jdbc:postgresql://localhost:5432/nexus_aquarium_db"
  user: admin
  password: admin
```

## Ejecutar el servidor

```bash
./gradlew run
```

El servidor se iniciará en `http://localhost:8080`

## Endpoints de la API

### Base URL: `/api/v1`

### 1. Listar todos los peces
```http
GET /api/v1/fish
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Neon Tetra",
    "scientificName": "Paracheirodon innesi",
    "imageUrl": null
  },
  {
    "id": 2,
    "name": "Guppy",
    "scientificName": "Poecilia reticulata",
    "imageUrl": null
  },
  {
    "id": 3,
    "name": "Corydoras Panda",
    "scientificName": "Corydoras panda",
    "imageUrl": "https://acdn-us.mitiendanube.com/stores/001/242/404/products/cory-panda1-f01ef7197d8564ae5415930549242542-1024-1024.jpg"
  }
]
```

### 2. Obtener un pez por ID
```http
GET /api/v1/fish/{id}
```

**Ejemplo:**
```http
GET /api/v1/fish/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Neon Tetra",
  "scientificName": "Paracheirodon innesi",
  "imageUrl": null
}
```

### 3. Crear un nuevo pez
```http
POST /api/v1/fish
Content-Type: application/json

{
  "id": 0,
  "name": "Betta",
  "scientificName": "Betta splendens",
  "imageUrl": "https://example.com/betta.jpg"
}
```

**Respuesta:** `201 Created` con el ID del nuevo pez

### 4. Actualizar un pez
```http
PUT /api/v1/fish/{id}
Content-Type: application/json

{
  "id": 1,
  "name": "Neon Tetra Updated",
  "scientificName": "Paracheirodon innesi",
  "imageUrl": "https://example.com/neon.jpg"
}
```

**Respuesta:** `200 OK`

### 5. Eliminar un pez
```http
DELETE /api/v1/fish/{id}
```

**Respuesta:** `200 OK`

## Estructura de la base de datos

### Tabla: `fish`

| Columna | Tipo | Descripción |
|---------|------|-------------|
| id | SERIAL | Primary key, auto-incremental |
| name | VARCHAR(255) | Nombre común del pez |
| scientific_name | VARCHAR(255) | Nombre científico del pez |
| image_url | TEXT | URL de la imagen (opcional) |

## Datos iniciales

La tabla se crea automáticamente al iniciar el servidor y se insertan 3 peces de ejemplo:
1. Neon Tetra
2. Guppy
3. Corydoras Panda (con imagen)

## Probar la API con curl

```bash
# Listar todos los peces
curl http://localhost:8080/api/v1/fish

# Obtener un pez específico
curl http://localhost:8080/api/v1/fish/1

# Crear un nuevo pez
curl -X POST http://localhost:8080/api/v1/fish \
  -H "Content-Type: application/json" \
  -d '{"id":0,"name":"Betta","scientificName":"Betta splendens","imageUrl":"https://example.com/betta.jpg"}'

# Actualizar un pez
curl -X PUT http://localhost:8080/api/v1/fish/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Neon Tetra Updated","scientificName":"Paracheirodon innesi","imageUrl":"https://example.com/neon.jpg"}'

# Eliminar un pez
curl -X DELETE http://localhost:8080/api/v1/fish/1
```

## Conectarse a PostgreSQL directamente

Si necesitas conectarte directamente a la base de datos:

```bash
docker exec -it nexus-aquarium-db psql -U admin -d nexus_aquarium_db
```

Comandos útiles en psql:
- `\dt` - Listar tablas
- `\d fish` - Describir la tabla fish
- `SELECT * FROM fish;` - Ver todos los peces
- `\q` - Salir

## Detener la base de datos

```bash
docker-compose down
```

Para detener y eliminar los datos:
```bash
docker-compose down -v
```

