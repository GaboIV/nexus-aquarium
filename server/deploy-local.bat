@echo off
REM Script de despliegue LOCAL para Nexus Aquarium API con PostgreSQL

echo ========================================
echo   Nexus Aquarium - Despliegue Local
echo ========================================
echo.

REM Parar y eliminar contenedores existentes
echo [1/5] Parando contenedores existentes...
docker compose -f docker-compose.local.yml down

echo.
echo [2/5] Eliminando contenedor PostgreSQL antiguo si existe...
docker stop nexus-aquarium-db 2>nul
docker rm nexus-aquarium-db 2>nul

echo.
echo [3/5] Construyendo imagen de la API (esto puede tardar varios minutos)...
docker compose -f docker-compose.local.yml build

echo.
echo [4/5] Levantando servicios...
docker compose -f docker-compose.local.yml up -d

echo.
echo [5/5] Esperando a que PostgreSQL este listo...
timeout /t 15 /nobreak > nul

echo.
echo ========================================
echo   Estado de los Servicios
echo ========================================
docker compose -f docker-compose.local.yml ps

echo.
echo ========================================
echo   Logs de la API
echo ========================================
docker compose -f docker-compose.local.yml logs nexus-aquarium-api

echo.
echo ========================================
echo   Verificacion
echo ========================================
echo API disponible en: http://localhost:4301/api/v1/fish
echo PostgreSQL disponible en: localhost:5433
echo.
echo Para ver logs en tiempo real:
echo   docker compose -f docker-compose.local.yml logs -f nexus-aquarium-api
echo.
echo Para parar los servicios:
echo   docker compose -f docker-compose.local.yml down
echo.

pause
