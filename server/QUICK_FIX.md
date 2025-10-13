# 🚨 Solución Rápida - Error de JAR no encontrado

## 🔴 Problema
```
Error: Unable to access jarfile build/libs/server-1.0-SNAPSHOT.jar
```

El contenedor `nexus-aquarium-api` está reiniciando constantemente porque:
1. Está usando la imagen de Docker Hub (`gaboiv/nexus-aquarium-api:latest`)
2. Esa imagen no tiene el JAR construido correctamente
3. Hay un PostgreSQL antiguo corriendo en el puerto 5432

## ✅ Solución Inmediata

### Paso 1: Limpiar todo
```powershell
# Parar TODOS los contenedores del proyecto
docker compose down

# Parar el PostgreSQL antiguo
docker stop nexus-aquarium-db
docker rm nexus-aquarium-db
```

### Paso 2: Usar el script de despliegue local
```powershell
cd server
.\deploy-local.bat
```

## 🔧 Solución Manual (si el script no funciona)

### 1. Parar todo
```powershell
docker compose -f docker-compose.local.yml down
docker stop nexus-aquarium-db
docker rm nexus-aquarium-db
```

### 2. Construir la imagen
```powershell
docker compose -f docker-compose.local.yml build
```

### 3. Levantar servicios
```powershell
docker compose -f docker-compose.local.yml up -d
```

### 4. Verificar
```powershell
# Ver estado
docker compose -f docker-compose.local.yml ps

# Ver logs
docker compose -f docker-compose.local.yml logs nexus-aquarium-api
```

## 📊 Diferencias: Local vs Producción

### `docker-compose.local.yml` (DESARROLLO LOCAL)
```yaml
nexus-aquarium-api:
  build: .  # Construye la imagen localmente
  ports:
    - "4301:8080"
```

### `docker-compose.yml` (PRODUCCIÓN)
```yaml
nexus-aquarium-api:
  image: gaboiv/nexus-aquarium-api:latest  # Usa imagen de Docker Hub
  ports:
    - "${API_PORT}:8080"
```

## 🎯 URLs de Acceso (Local)

- **API:** http://localhost:4301/api/v1/fish
- **PostgreSQL:** localhost:5433 (puerto 5433 para evitar conflictos)
- **Usuario:** admin
- **Password:** admin
- **Base de datos:** nexus_aquarium_db

## 🔍 Comandos Útiles

### Ver logs en tiempo real
```powershell
docker compose -f docker-compose.local.yml logs -f nexus-aquarium-api
```

### Reiniciar solo la API
```powershell
docker compose -f docker-compose.local.yml restart nexus-aquarium-api
```

### Acceder a PostgreSQL
```powershell
docker compose -f docker-compose.local.yml exec postgres psql -U admin -d nexus_aquarium_db
```

### Reconstruir desde cero
```powershell
docker compose -f docker-compose.local.yml down -v
docker compose -f docker-compose.local.yml build --no-cache
docker compose -f docker-compose.local.yml up -d
```

## ✅ Verificación de que Todo Funciona

### 1. Verificar contenedores
```powershell
docker compose -f docker-compose.local.yml ps
```

**Salida esperada:**
```
NAME                 STATUS
nexus-postgres       Up (healthy)
nexus-aquarium-api   Up
```

### 2. Probar API
```powershell
curl http://localhost:4301/api/v1/fish
```

**Salida esperada:** JSON con lista de 5 peces

### 3. Verificar base de datos
```powershell
docker compose -f docker-compose.local.yml exec postgres psql -U admin -d nexus_aquarium_db -c "SELECT COUNT(*) FROM fish;"
```

**Salida esperada:**
```
 count 
-------
     5
```

## 🚨 Si Sigue Fallando

### Error: Puerto 5432 en uso
```powershell
# Verificar qué proceso usa el puerto
netstat -ano | findstr :5432

# Cambiar puerto en docker-compose.local.yml
# Cambiar "5433:5432" por otro puerto como "5434:5432"
```

### Error: Gradle build falla
```powershell
# Construir el JAR localmente primero
.\gradlew.bat build -x test

# Luego construir la imagen
docker compose -f docker-compose.local.yml build
```

### Error: Imagen no se construye
```powershell
# Limpiar caché de Docker
docker system prune -a

# Reconstruir sin caché
docker compose -f docker-compose.local.yml build --no-cache
```

## 📝 Notas Importantes

1. **Puerto 5433:** Usamos el puerto 5433 en lugar de 5432 porque ya tienes otro PostgreSQL corriendo
2. **Construcción local:** El `docker-compose.local.yml` construye la imagen localmente
3. **Producción:** Para producción, subirás la imagen a Docker Hub y usarás `docker-compose.yml`

## 🎯 Próximos Pasos

Una vez que funcione en local:

1. **Subir imagen a Docker Hub:**
   ```powershell
   docker tag nexus-aquarium-api:latest gaboiv/nexus-aquarium-api:latest
   docker push gaboiv/nexus-aquarium-api:latest
   ```

2. **Desplegar en servidor:**
   ```bash
   # En el servidor
   docker compose up -d
   ```
