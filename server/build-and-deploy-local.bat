@echo off
REM Script que construye el JAR localmente y luego despliega con Docker

echo ========================================
echo   Nexus Aquarium - Build y Despliegue
echo ========================================
echo.

REM Paso 1: Construir el JAR localmente
echo [1/6] Construyendo JAR localmente con Gradle...
call gradlew.bat clean build -x test
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo la construccion del JAR
    pause
    exit /b 1
)

echo.
echo [2/6] Verificando que el JAR existe...
if not exist "build\libs\*.jar" (
    echo ERROR: No se encontro el JAR en build/libs/
    pause
    exit /b 1
)
dir build\libs\*.jar

REM Paso 2: Parar contenedores existentes
echo.
echo [3/6] Parando contenedores existentes...
docker compose -f docker-compose.local.yml down
docker stop nexus-aquarium-db 2>nul
docker rm nexus-aquarium-db 2>nul

REM Paso 3: Construir imagen Docker (usando Dockerfile.simple)
echo.
echo [4/6] Construyendo imagen Docker...
docker build -f Dockerfile.simple -t nexus-aquarium-api:local .

REM Paso 4: Levantar servicios
echo.
echo [5/6] Levantando servicios...
docker compose -f docker-compose.local.yml up -d

REM Paso 5: Esperar a que PostgreSQL este listo
echo.
echo [6/6] Esperando a que PostgreSQL este listo...
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
docker compose -f docker-compose.local.yml logs --tail=50 nexus-aquarium-api

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


