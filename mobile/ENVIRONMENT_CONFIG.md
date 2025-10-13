# 🌍 Configuración de Entornos - Nexus Aquarium

## 📋 Entornos Disponibles

### 1. **LOCAL** (Desarrollo Local)
- **URL:** `http://10.0.2.2:4301`
- **Uso:** Desarrollo con emulador Android
- **Descripción:** Para desarrollo local con el servidor corriendo en localhost

### 2. **DEVELOP** (Desarrollo en Red)
- **URL:** `http://192.168.1.100:4301`
- **Uso:** Desarrollo con dispositivo físico en la misma red
- **Descripción:** Para probar en dispositivos reales conectados a la misma red WiFi

### 3. **QA** (Testing)
- **URL:** `http://qa.nexusaquarium.com:4301`
- **Uso:** Servidor de pruebas
- **Descripción:** Para testing y validación antes de producción

### 4. **PROD** (Producción)
- **URL:** `http://pappstest.com:4301`
- **Uso:** Servidor de producción
- **Descripción:** Para la versión final de la aplicación

## 🔧 Cómo Cambiar de Entorno

### Método 1: Cambiar en AppConfig.kt
```kotlin
// En AppConfig.kt, cambiar esta línea:
val CURRENT_ENVIRONMENT = Environment.LOCAL  // Cambiar por el entorno deseado
```

### Método 2: Compilación con Build Variants (Futuro)
```kotlin
// Se puede implementar build variants para cambiar automáticamente
// según el tipo de compilación (debug/release)
```

## 📱 Configuración por Dispositivo

### Para Emulador Android
```kotlin
val CURRENT_ENVIRONMENT = Environment.LOCAL
// URL: http://10.0.2.2:4301
```

### Para Dispositivo Físico (Misma Red)
```kotlin
val CURRENT_ENVIRONMENT = Environment.DEVELOP
// URL: http://192.168.1.100:4301
// Nota: Cambiar 192.168.1.100 por la IP real de tu computadora
```

### Para Testing
```kotlin
val CURRENT_ENVIRONMENT = Environment.QA
// URL: http://qa.nexusaquarium.com:4301
```

### Para Producción
```kotlin
val CURRENT_ENVIRONMENT = Environment.PROD
// URL: http://pappstest.com:4301
```

## 🔍 Verificación de Configuración

### 1. Verificar URL Actual
```kotlin
// En la app, puedes verificar la configuración actual:
Log.d("AppConfig", "Environment: ${AppConfig.ENVIRONMENT_NAME}")
Log.d("AppConfig", "Base URL: ${AppConfig.API_BASE_URL}")
Log.d("AppConfig", "Fish Endpoint: ${AppConfig.FISH_ENDPOINT}")
```

### 2. Verificar Conectividad
```kotlin
// Probar la conexión:
val healthUrl = "${AppConfig.API_BASE_URL}${AppConfig.HEALTH_ENDPOINT}"
// Debería responder con status 200
```

## 🚨 Troubleshooting

### Error: "Permission denied (missing INTERNET permission?)"
**Causa:** La app no tiene permisos de internet o la URL no es accesible.

**Soluciones:**
1. **Verificar permisos en AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

2. **Verificar que el servidor esté corriendo:**
```bash
# En tu computadora:
curl http://localhost:4301/api/v1/fish
```

3. **Verificar la IP correcta para dispositivo físico:**
```bash
# En Windows:
ipconfig
# Buscar la IP de tu adaptador WiFi (ej: 192.168.1.100)
```

### Error: "EAI_NODATA (No address associated with hostname)"
**Causa:** No se puede resolver el hostname.

**Soluciones:**
1. **Usar IP en lugar de hostname para LOCAL:**
```kotlin
Environment.LOCAL to EnvironmentConfig(
    baseUrl = "http://10.0.2.2:4301", // Para emulador
    // O usar IP real para dispositivo físico
)
```

2. **Verificar conectividad de red:**
```bash
# Desde el dispositivo, probar:
ping 10.0.2.2  # Para emulador
ping 192.168.1.100  # Para dispositivo físico
```

## 📋 Checklist de Configuración

### Para Desarrollo Local
- [ ] Servidor corriendo en `localhost:4301`
- [ ] App configurada con `Environment.LOCAL`
- [ ] Emulador Android funcionando
- [ ] API respondiendo en `http://localhost:4301/api/v1/fish`

### Para Dispositivo Físico
- [ ] Servidor corriendo en `localhost:4301`
- [ ] App configurada con `Environment.DEVELOP`
- [ ] IP de la computadora actualizada en la configuración
- [ ] Dispositivo y computadora en la misma red WiFi
- [ ] Firewall permitiendo conexiones en puerto 4301

### Para Producción
- [ ] Servidor desplegado en `pappstest.com:4301`
- [ ] App configurada con `Environment.PROD`
- [ ] Dominio accesible desde internet
- [ ] SSL/HTTPS configurado (recomendado)

## 🔄 Flujo de Desarrollo Recomendado

1. **Desarrollo:** Usar `Environment.LOCAL` con emulador
2. **Pruebas:** Usar `Environment.DEVELOP` con dispositivo físico
3. **Testing:** Usar `Environment.QA` con servidor de pruebas
4. **Producción:** Usar `Environment.PROD` con servidor final

## 📊 Estado Actual

- ✅ **Sistema de entornos:** Implementado
- ✅ **Configuración LOCAL:** Lista para emulador
- ✅ **Configuración DEVELOP:** Lista para dispositivo físico
- ✅ **Configuración QA:** Preparada para servidor de pruebas
- ✅ **Configuración PROD:** Lista para producción
- ⏳ **Testing:** Pendiente de verificación
