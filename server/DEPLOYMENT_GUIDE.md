# 🚀 Guía de Despliegue - Nexus Aquarium API con PostgreSQL

## 📋 Resumen

Esta guía te ayudará a desplegar la API de Nexus Aquarium con PostgreSQL en tu servidor `pappstest.com:4301`.

## 🏗️ Arquitectura del Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Mobile App    │    │   API Server    │    │   PostgreSQL    │
│                 │    │                 │    │                 │
│ pappstest.com   │───▶│   Port 4301     │───▶│   Port 5432     │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🐳 Componentes Docker

### 1. PostgreSQL Database
- **Imagen:** `postgres:15-alpine`
- **Puerto:** `5432`
- **Base de datos:** `nexus_aquarium_db`
- **Usuario:** `admin`
- **Contraseña:** `admin`

### 2. Nexus Aquarium API
- **Puerto externo:** `4301`
- **Puerto interno:** `8080`
- **Base de datos:** Conecta a PostgreSQL
- **Endpoints:** `/api/v1/fish`

## 🚀 Pasos de Despliegue

### Opción 1: Script Automático (Recomendado)

#### En Linux/Mac:
```bash
cd server
chmod +x deploy.sh
./deploy.sh
```

#### En Windows:
```cmd
cd server
deploy.bat
```

### Opción 2: Comandos Manuales

#### 1. Parar servicios existentes
```bash
docker-compose down
```

#### 2. Limpiar sistema (opcional)
```bash
docker system prune -f
```

#### 3. Construir imagen de la API
```bash
docker-compose build
```

#### 4. Levantar servicios
```bash
docker-compose up -d
```

#### 5. Verificar servicios
```bash
docker-compose ps
```

## 🔍 Verificación del Despliegue

### 1. Verificar que los contenedores estén corriendo
```bash
docker-compose ps
```

**Salida esperada:**
```
NAME                    IMAGE                    STATUS
nexus-postgres         postgres:15-alpine       Up
nexus-aquarium-api     nexus-aquarium-api       Up
```

### 2. Verificar logs de la API
```bash
docker-compose logs nexus-api
```

### 3. Verificar conexión a PostgreSQL
```bash
docker-compose exec postgres psql -U admin -d nexus_aquarium_db -c "SELECT COUNT(*) FROM fish;"
```

**Salida esperada:**
```
 count 
-------
     5
```

### 4. Probar endpoint de la API
```bash
curl http://pappstest.com:4301/api/v1/fish
```

**Salida esperada:** JSON con la lista de peces

## 📊 Datos de Ejemplo

La base de datos se inicializa automáticamente con 5 peces de ejemplo:

1. **Guppy** - Pez popular para principiantes
2. **Neón Tetra** - Pez pequeño y colorido
3. **Betta** - Pez solitario con colores vibrantes
4. **Corydoras** - Pez de fondo activo
5. **Molly** - Pez resistente y prolífico

## 🔧 Configuración de la API

### Variables de Entorno
```yaml
POSTGRES_URL: jdbc:postgresql://postgres:5432/nexus_aquarium_db
POSTGRES_USER: admin
POSTGRES_PASSWORD: admin
```

### Endpoints Disponibles
- `GET /api/v1/fish` - Lista todos los peces
- `GET /api/v1/fish/{id}` - Obtiene un pez por ID
- `POST /api/v1/fish` - Crea un nuevo pez
- `PUT /api/v1/fish/{id}` - Actualiza un pez
- `DELETE /api/v1/fish/{id}` - Elimina un pez

## 🛠️ Comandos de Mantenimiento

### Ver logs en tiempo real
```bash
docker-compose logs -f nexus-api
```

### Reiniciar solo la API
```bash
docker-compose restart nexus-api
```

### Reiniciar solo PostgreSQL
```bash
docker-compose restart postgres
```

### Acceder a la base de datos
```bash
docker-compose exec postgres psql -U admin -d nexus_aquarium_db
```

### Hacer backup de la base de datos
```bash
docker-compose exec postgres pg_dump -U admin nexus_aquarium_db > backup.sql
```

### Restaurar backup
```bash
docker-compose exec -T postgres psql -U admin nexus_aquarium_db < backup.sql
```

## 🚨 Troubleshooting

### Problema: API no se conecta a PostgreSQL
**Solución:**
```bash
# Verificar que PostgreSQL esté corriendo
docker-compose ps postgres

# Verificar logs de PostgreSQL
docker-compose logs postgres

# Reiniciar servicios
docker-compose restart
```

### Problema: Puerto 4301 ocupado
**Solución:**
```bash
# Verificar qué proceso usa el puerto
netstat -tulpn | grep 4301

# Cambiar puerto en docker-compose.yml
# Cambiar "4301:8080" por "4302:8080"
```

### Problema: Base de datos no se inicializa
**Solución:**
```bash
# Eliminar volúmenes y recrear
docker-compose down -v
docker-compose up -d
```

## 📱 Configuración de la App Móvil

La app móvil ya está configurada para apuntar a:
- **URL:** `http://pappstest.com:4301`
- **Endpoint:** `/api/v1/fish`

### Verificar desde la app
1. Abrir la app en el teléfono
2. Ir a la sección "Peces"
3. Debería cargar los 5 peces de ejemplo

## 📋 Checklist de Despliegue

- [ ] Docker instalado en el servidor
- [ ] Archivos de configuración en su lugar
- [ ] Script de despliegue ejecutado
- [ ] PostgreSQL corriendo en puerto 5432
- [ ] API corriendo en puerto 4301
- [ ] Base de datos inicializada con datos
- [ ] Endpoint `/api/v1/fish` respondiendo
- [ ] App móvil conectándose correctamente

## 🔄 Actualizaciones

### Actualizar la API
```bash
# Reconstruir y reiniciar
docker-compose build nexus-api
docker-compose up -d nexus-api
```

### Actualizar la base de datos
```bash
# Ejecutar nuevos scripts SQL
docker-compose exec postgres psql -U admin -d nexus_aquarium_db -f /path/to/new_script.sql
```

## 📞 Soporte

Si tienes problemas:
1. Verificar logs: `docker-compose logs`
2. Verificar estado: `docker-compose ps`
3. Reiniciar servicios: `docker-compose restart`
4. Recrear todo: `docker-compose down && docker-compose up -d`

¡Tu API con PostgreSQL está lista para funcionar! 🎉
