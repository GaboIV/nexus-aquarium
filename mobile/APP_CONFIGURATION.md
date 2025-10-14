# Configuración de la Aplicación

Este documento explica cómo está configurada la aplicación Nexus Aquarium y cómo modificar los parámetros principales.

## Archivos de Configuración

### 1. `gradle.properties` (Configuración Principal)
Este es el archivo principal donde se definen todos los parámetros de la aplicación:

```properties
# Información de la Aplicación
app.name=Nexus Aquarium
app.package.name=com.nexusaquarium
app.display.name=Nexus Aquarium
app.short.name=Nexus

# Información de Versión
app.version.name=1.0.0
app.version.code=1

# Configuración de Build
app.min.sdk=24
app.target.sdk=34
app.compile.sdk=34

# Información del Desarrollador
app.developer.name=Nexus Aquarium Team
app.company.name=Nexus Aquarium
app.support.email=support@nexusaquarium.com
app.website.url=https://nexusaquarium.com

# Descripción de la Aplicación
app.description=Nexus Aquarium - Fish Management System

# Configuración de Iconos
app.icon.name=ic_launcher
app.round.icon.name=ic_launcher_round
```

### 2. `AppInfo.kt` (Configuración en Código)
Archivo Kotlin que contiene la misma información para uso en el código:

```kotlin
object AppInfo {
    const val APP_NAME = "Nexus Aquarium"
    const val VERSION_NAME = "0.0.1"
    const val VERSION_CODE = 1
    // ... más configuraciones
}
```

## Cómo Modificar la Configuración

### Cambiar el Nombre de la Aplicación
1. Edita `gradle.properties`:
   ```properties
   app.name=Tu Nuevo Nombre
   app.display.name=Tu Nuevo Nombre
   ```

2. Edita `AppInfo.kt`:
   ```kotlin
   const val APP_NAME = "Tu Nuevo Nombre"
   ```

### Cambiar la Versión
1. Edita `gradle.properties`:
   ```properties
   app.version.name=1.0.0
   app.version.code=2
   ```

2. Edita `AppInfo.kt`:
   ```kotlin
   const val VERSION_NAME = "1.0.0"
   const val VERSION_CODE = 2
   ```

### Cambiar el Logo
1. Reemplaza los archivos en `src/androidMain/res/mipmap-*/`:
   - `ic_launcher.png` (icono normal)
   - `ic_launcher_round.png` (icono redondo)

2. Los archivos deben estar en diferentes resoluciones:
   - `mipmap-hdpi/` (72x72 px)
   - `mipmap-mdpi/` (48x48 px)
   - `mipmap-xhdpi/` (96x96 px)
   - `mipmap-xxhdpi/` (144x144 px)
   - `mipmap-xxxhdpi/` (192x192 px)

## Estructura de Archivos

```
mobile/
├── gradle.properties              # Configuración principal
├── composeApp/
│   ├── build.gradle.kts          # Usa las propiedades de gradle.properties
│   ├── src/
│   │   ├── commonMain/kotlin/com/nexusaquarium/config/
│   │   │   ├── AppInfo.kt        # Configuración en código
│   │   │   └── AppConfig.kt       # Configuración de entorno
│   │   └── androidMain/
│   │       ├── res/values/strings.xml  # Strings de la aplicación
│   │       ├── res/mipmap-*/          # Iconos de la aplicación
│   │       └── AndroidManifest.xml    # Manifest de Android
```

## Beneficios de esta Configuración

1. **Centralizada**: Un solo lugar para cambiar nombre, versión y configuración
2. **Consistente**: Misma información en todos los archivos
3. **Mantenible**: Fácil de actualizar y versionar
4. **Multiplataforma**: Funciona para Android, iOS y Desktop
5. **Automatizada**: Gradle lee automáticamente las propiedades

## Comandos Útiles

```bash
# Ver la configuración actual
./gradlew properties | grep app.

# Limpiar y reconstruir
./gradlew clean build

# Generar APK con la configuración actual
./gradlew assembleDebug
```

## Notas Importantes

- Siempre actualiza tanto `gradle.properties` como `AppInfo.kt` para mantener consistencia
- Los iconos deben seguir las guías de diseño de Material Design
- La versión debe seguir el formato semántico (MAJOR.MINOR.PATCH)
- **IMPORTANTE**: Para distribuciones nativas (macOS DMG), el MAJOR debe ser mayor que 0 (no usar 0.x.x)
- El `versionCode` debe incrementarse con cada release
