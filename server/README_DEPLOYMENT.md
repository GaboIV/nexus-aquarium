# 🚀 Despliegue Completo - Nexus Aquarium API + PostgreSQL

## ✅ Configuración Completada

He configurado todo el sistema para que funcione con PostgreSQL. Aquí está lo que se ha creado:

### 📁 Archivos Creados/Modificados

#### 1. **`docker-compose.yml`** (NUEVO)
- PostgreSQL en puerto 5432
- API en puerto 4301 (externo) → 8080 (interno)
- Red compartida entre contenedores
- Volúmenes persistentes para la base de datos

#### 2. **`init.sql`** (NUEVO)
- Script de inicialización de PostgreSQL
- Creación de tabla `fish` con todos los campos
- 5 peces de ejemplo insertados
- Índices para optimizar consultas
- Triggers para actualización automática

#### 3. **`application.yaml`** (ACTUALIZADO)
- Configuración con variables de entorno
- URL de PostgreSQL configurada para Docker
- Usuario y contraseña configurables

#### 4. **`deploy.sh`** y **`deploy.bat`** (NUEVOS)
- Scripts de despliegue automático
- Verificación de servicios
- Logs y troubleshooting

#### 5. **`DEPLOYMENT_GUIDE.md`** (NUEVO)
- Guía completa de despliegue
- Comandos de mantenimiento
- Troubleshooting
- Verificación del sistema

## 🐳 Arquitectura del Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Mobile App    │    │   API Server    │    │   PostgreSQL    │
│                 │    │                 │    │                 │
│ pappstest.com   │───▶│   Port 4301     │───▶│   Port 5432     │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Cómo Desplegar

### Opción 1: Script Automático (Recomendado)

#### En Windows:
```cmd
cd server
deploy.bat
```

#### En Linux/Mac:
```bash
cd server
./deploy.sh
```

### Opción 2: Comandos Manuales

```bash
cd server
docker-compose down
docker-compose build
docker-compose up -d
```

## 🔍 Verificación

### 1. Verificar servicios
```bash
docker-compose ps
```

### 2. Probar API
```bash
curl http://pappstest.com:4301/api/v1/fish
```

### 3. Verificar base de datos
```bash
docker-compose exec postgres psql -U admin -d nexus_aquarium_db -c "SELECT COUNT(*) FROM fish;"
```

## 📊 Datos de Ejemplo

La base de datos se inicializa automáticamente con 5 peces:

1. **Guppy** - Pez popular para principiantes
2. **Neón Tetra** - Pez pequeño y colorido  
3. **Betta** - Pez solitario con colores vibrantes
4. **Corydoras** - Pez de fondo activo
5. **Molly** - Pez resistente y prolífico

## 🔧 Configuración de la API

### Variables de Entorno
- `POSTGRES_URL`: `jdbc:postgresql://postgres:5432/nexus_aquarium_db`
- `POSTGRES_USER`: `admin`
- `POSTGRES_PASSWORD`: `admin`

### Endpoints
- `GET /api/v1/fish` - Lista todos los peces
- `GET /api/v1/fish/{id}` - Obtiene un pez por ID
- `POST /api/v1/fish` - Crea un nuevo pez
- `PUT /api/v1/fish/{id}` - Actualiza un pez
- `DELETE /api/v1/fish/{id}` - Elimina un pez

## 📱 Configuración de la App

La app móvil ya está configurada para:
- **URL:** `http://pappstest.com:4301`
- **Endpoint:** `/api/v1/fish`

## 🛠️ Comandos Útiles

### Ver logs
```bash
docker-compose logs -f nexus-api
```

### Reiniciar API
```bash
docker-compose restart nexus-api
```

### Acceder a PostgreSQL
```bash
docker-compose exec postgres psql -U admin -d nexus_aquarium_db
```

### Backup de la base de datos
```bash
docker-compose exec postgres pg_dump -U admin nexus_aquarium_db > backup.sql
```

## 📋 Checklist de Despliegue

- [x] Docker Compose configurado
- [x] PostgreSQL configurado
- [x] API configurada para PostgreSQL
- [x] Scripts de despliegue creados
- [x] Documentación completa
- [ ] Desplegar en servidor (tu parte)
- [ ] Verificar funcionamiento
- [ ] Probar desde la app móvil

## 🎯 Próximos Pasos

1. **Ejecutar el despliegue** en tu servidor
2. **Verificar que todo funcione** con los comandos de verificación
3. **Generar APK** de la app móvil
4. **Instalar APK** en tu Poco X5 Pro
5. **Probar la conexión** desde la app

## 🚨 Troubleshooting

Si algo no funciona:

1. **Verificar logs:** `docker-compose logs`
2. **Verificar estado:** `docker-compose ps`
3. **Reiniciar servicios:** `docker-compose restart`
4. **Recrear todo:** `docker-compose down && docker-compose up -d`

¡Todo está listo para desplegar! 🎉
