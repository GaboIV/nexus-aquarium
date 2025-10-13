@echo off
REM Script de despliegue para Nexus Aquarium API con PostgreSQL (Windows)

echo 🐳 Desplegando Nexus Aquarium API con PostgreSQL...

REM Parar contenedores existentes
echo 🛑 Parando contenedores existentes...
docker-compose down

REM Limpiar imágenes antiguas (opcional)
echo 🧹 Limpiando imágenes antiguas...
docker system prune -f

REM Construir la imagen de la API
echo 🔨 Construyendo imagen de la API...
docker-compose build

REM Levantar los servicios
echo 🚀 Levantando servicios...
docker-compose up -d

REM Esperar a que PostgreSQL esté listo
echo ⏳ Esperando a que PostgreSQL esté listo...
timeout /t 10 /nobreak > nul

REM Verificar que los servicios estén corriendo
echo ✅ Verificando servicios...
docker-compose ps

REM Verificar logs de la API
echo 📋 Logs de la API:
docker-compose logs nexus-api

REM Verificar conexión a la base de datos
echo 🔍 Verificando conexión a PostgreSQL...
docker-compose exec postgres psql -U admin -d nexus_aquarium_db -c "SELECT COUNT(*) FROM fish;"

echo 🎉 ¡Despliegue completado!
echo 📡 API disponible en: http://pappstest.com:4301
echo 🐟 Endpoint de peces: http://pappstest.com:4301/api/v1/fish
echo 💾 PostgreSQL disponible en: localhost:5432

pause
