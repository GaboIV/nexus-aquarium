# Configuración de Producción - Nexus Aquarium

## 🌐 Configuración del Servidor

### URL del Servidor
- **Dominio:** `pappstest.com:4301`
- **Protocolo:** HTTP
- **URL Completa:** `http://pappstest.com:4301`

### Endpoints de la API
- **Base URL:** `http://pappstest.com:4301`
- **Peces:** `http://pappstest.com:4301/api/fish`
- **Health Check:** `http://pappstest.com:4301/health`

## 📱 Configuración de la App

### Archivos Modificados

#### 1. `AppConfig.kt` (NUEVO)
```kotlin
object AppConfig {
    const val API_BASE_URL = "http://pappstest.com:4301"
    const val FISH_ENDPOINT = "/api/fish"
    const val HEALTH_ENDPOINT = "/health"
    const val REQUEST_TIMEOUT = 30000L
    const val MAX_RETRY_ATTEMPTS = 3
}
```

#### 2. `HttpClientProvider.kt` (ACTUALIZADO)
- Configurado para usar `AppConfig.API_BASE_URL`
- Timeout configurado a 30 segundos
- Serialización JSON configurada
- Funciones helper para obtener URLs completas

#### 3. `FishApiService.kt` (ACTUALIZADO)
- Todas las URLs ahora usan `HttpClientProvider.getFishEndpoint()`
- Endpoints dinámicos basados en configuración
- Manejo de errores mejorado

#### 4. `FishViewModel.kt` (ACTUALIZADO)
- Usa `HttpClientProvider.client` en lugar de crear nueva instancia
- Configuración centralizada

## 🔧 Configuración del Servidor

### Docker Compose (Para el servidor)
```yaml
version: '3.8'

services:
  nexus-aquarium-api:
    image: tu-usuario/nexus-aquarium-api:latest
    container_name: nexus-aquarium-api
    ports:
      - "4301:8080"  # Puerto 4301 externo, 8080 interno
    environment:
      - SPRING_PROFILES_ACTIVE=production
    restart: unless-stopped
    networks:
      - nexus-network

networks:
  nexus-network:
    driver: bridge
```

### Comandos para Desplegar
```bash
# En el servidor
docker-compose up -d
docker logs nexus-aquarium-api
```

## 📱 Generación del APK

### Comando para Generar APK
```bash
cd mobile
./gradlew assembleDebug
```

### Ubicación del APK
```
mobile/composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

### Instalación en Poco X5 Pro
```bash
# Via ADB
adb install mobile/composeApp/build/outputs/apk/debug/composeApp-debug.apk

# O manualmente:
# 1. Copiar APK al teléfono
# 2. Habilitar "Fuentes desconocidas" en Configuración > Seguridad
# 3. Instalar desde el explorador de archivos
```

## 🔍 Verificación

### 1. Verificar que el servidor esté funcionando
```bash
curl http://pappstest.com:4301/api/fish
```

### 2. Verificar desde el navegador del teléfono
- Abrir: `http://pappstest.com:4301/api/fish`
- Debería mostrar JSON con la lista de peces

### 3. Verificar en la app
- Abrir la app en el teléfono
- Ir a la sección "Peces"
- Debería cargar los datos desde el servidor

## 🚨 Troubleshooting

### Problemas Comunes

#### 1. Error de Conexión
- **Causa:** Servidor no disponible o puerto bloqueado
- **Solución:** Verificar que el servidor esté corriendo y el puerto 4301 esté abierto

#### 2. Timeout
- **Causa:** Red lenta o servidor sobrecargado
- **Solución:** Aumentar `REQUEST_TIMEOUT` en `AppConfig.kt`

#### 3. CORS (si aplica)
- **Causa:** Problemas de CORS en el servidor
- **Solución:** Configurar CORS en el servidor para permitir requests desde la app

### Logs para Debugging
- **Servidor:** `docker logs nexus-aquarium-api`
- **App:** Usar Logcat en Android Studio o IntelliJ IDEA

## 📋 Checklist de Despliegue

- [ ] Servidor desplegado en `pappstest.com:4301`
- [ ] API respondiendo en `/api/fish`
- [ ] App compilada con la nueva configuración
- [ ] APK generado exitosamente
- [ ] APK instalado en Poco X5 Pro
- [ ] App conectándose al servidor correctamente
- [ ] Datos de peces cargando desde el servidor

## 🔄 Cambios de Configuración

Para cambiar el servidor en el futuro, solo modifica `AppConfig.kt`:

```kotlin
// Para desarrollo local
const val API_BASE_URL = "http://localhost:8080"

// Para servidor de producción
const val API_BASE_URL = "http://pappstest.com:4301"

// Para otro servidor
const val API_BASE_URL = "http://nuevo-servidor.com:puerto"
```

## 📊 Estado Actual

- ✅ **Configuración:** Completada
- ✅ **Compilación:** Exitosa
- ✅ **URLs:** Configuradas para `pappstest.com:4301`
- ⏳ **Despliegue:** Pendiente (en proceso)
- ⏳ **APK:** Pendiente de generación
- ⏳ **Instalación:** Pendiente

## 🎯 Próximos Pasos

1. **Desplegar servidor** en `pappstest.com:4301`
2. **Generar APK** con la nueva configuración
3. **Instalar APK** en Poco X5 Pro
4. **Verificar funcionamiento** de la conexión
5. **Probar todas las funcionalidades** de la app
