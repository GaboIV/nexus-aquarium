@echo off
REM Script de verificación para el despliegue local

echo ========================================
echo   Verificacion del Sistema
echo ========================================
echo.

echo [1] Verificando Docker...
docker --version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker no esta instalado o no esta corriendo
    pause
    exit /b 1
)

echo.
echo [2] Verificando Docker Compose...
docker compose version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker Compose no esta disponible
    pause
    exit /b 1
)

echo.
echo [3] Verificando archivos necesarios...
if not exist "Dockerfile" (
    echo ERROR: Dockerfile no encontrado
    pause
    exit /b 1
)
if not exist "docker-compose.local.yml" (
    echo ERROR: docker-compose.local.yml no encontrado
    pause
    exit /b 1
)
if not exist "init.sql" (
    echo ERROR: init.sql no encontrado
    pause
    exit /b 1
)

echo.
echo [4] Estado de contenedores actuales...
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo.
echo [5] Puertos en uso...
netstat -ano | findstr ":4301 :5433"

echo.
echo ========================================
echo   Todo listo para desplegar!
echo ========================================
echo.
echo Ejecuta: deploy-local.bat
echo.

pause
