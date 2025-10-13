# Nexus Aquarium - Mobile App

## 🐠 Descripción

Aplicación multiplataforma (Android, iOS, Desktop) desarrollada con Kotlin Multiplatform y Compose que permite gestionar una colección de peces conectándose a la API REST de Nexus Aquarium.

## ✨ Características

- **Interfaz Moderna**: Diseño Material Design 3 con animaciones fluidas
- **Navegación por pestañas**: Menú inferior con 4 secciones principales
- **Visualización de peces**: Lista de peces con imágenes y detalles
- **Tarjetas expandibles**: Cada pez tiene información detallada
- **Estados de UI**: Loading, Success, Error, y Empty State
- **Gestión de estado**: ViewModel con StateFlow
- **Consumo de API**: Integración con el backend mediante Ktor Client
- **Carga de imágenes**: Soporte para imágenes con Coil

## 🏗️ Arquitectura

```
mobile/composeApp/src/commonMain/kotlin/com/nexusaquarium/
├── data/
│   ├── model/
│   │   └── Fish.kt                    # Data model
│   └── remote/
│       ├── FishApiService.kt          # API service layer
│       └── HttpClientProvider.kt      # HTTP client configuration
├── ui/
│   ├── components/
│   │   └── FishCard.kt                # Fish card component
│   ├── navigation/
│   │   ├── BottomNavItem.kt           # Navigation items
│   │   └── BottomNavigationBar.kt     # Bottom navigation bar
│   ├── screens/
│   │   ├── HomeScreen.kt              # Main screen with fish list
│   │   ├── AddFishScreen.kt           # Add fish screen (placeholder)
│   │   ├── ProfileScreen.kt           # Profile screen (placeholder)
│   │   └── SettingsScreen.kt          # Settings screen (placeholder)
│   └── viewmodel/
│       └── FishViewModel.kt           # ViewModel for fish data
└── App.kt                              # Main app entry point
```

## 🌍 Configuración de Entornos

La aplicación soporta múltiples entornos para desarrollo y producción:

### Entornos Disponibles
- **LOCAL**: Desarrollo con emulador Android (`http://10.0.2.2:4301`)
- **DEVELOP**: Dispositivo físico en red local (`http://192.168.1.100:4301`)
- **QA**: Servidor de pruebas (`http://qa.nexusaquarium.com:4301`)
- **PROD**: Servidor de producción (`http://pappstest.com:4301`)

### Cambiar de Entorno
```kotlin
// En AppConfig.kt, cambiar esta línea:
val CURRENT_ENVIRONMENT = Environment.LOCAL  // Cambiar por el entorno deseado
```

Ver [ENVIRONMENT_CONFIG.md](ENVIRONMENT_CONFIG.md) para más detalles.

## 🚀 Tecnologías Utilizadas

- **Kotlin Multiplatform**: Código compartido entre plataformas
- **Jetpack Compose**: UI declarativa
- **Material Design 3**: Sistema de diseño moderno
- **Ktor Client**: Cliente HTTP multiplataforma
- **Kotlinx Serialization**: Serialización JSON
- **Coil 3**: Carga de imágenes asíncrona
- **ViewModel & StateFlow**: Gestión de estado reactivo
- **Coroutines**: Programación asíncrona

## 📱 Pantallas

### 1. Home (Inicio)
- Lista de todos los peces en la base de datos
- Tarjeta con estadísticas (total de peces)
- Cada pez se muestra en una tarjeta con:
  - Imagen (si está disponible)
  - Nombre común
  - Nombre científico (al expandir)
  - Botón de eliminación
- Pull to refresh
- Estados: Loading, Error, Empty, Success

### 2. Add (Agregar)
- Placeholder para formulario de agregar peces
- Próximamente: Formulario con campos para crear nuevos peces

### 3. Profile (Perfil)
- Placeholder para perfil de usuario
- Próximamente: Información del usuario y configuraciones de perfil

### 4. Settings (Configuración)
- Placeholder para configuraciones
- Próximamente: Ajustes de la aplicación

## 🔧 Configuración

### Requisitos previos

1. **Backend corriendo**: El servidor debe estar ejecutándose en `http://localhost:8080`
   ```bash
   cd server
   ./gradlew run
   ```

2. **Base de datos PostgreSQL**: Docker Compose debe estar activo
   ```bash
   docker-compose up -d
   ```

### Ejecutar la aplicación

#### Desktop (JVM)
```bash
cd mobile
./gradlew :composeApp:run
```

#### Android
```bash
cd mobile
./gradlew :composeApp:assembleDebug
```

#### iOS
```bash
cd mobile/iosApp
open iosApp.xcodeproj
# Run desde Xcode
```

## 🎨 Diseño

### Color Scheme
La aplicación utiliza el sistema de colores dinámico de Material Design 3:
- `primary`: Color principal de la marca
- `primaryContainer`: Contenedores con acento
- `surface`: Superficies de fondo
- `surfaceContainerLow/Lowest`: Variaciones de superficie
- `error`: Para estados de error
- `onSurface/onPrimaryContainer`: Colores de contenido

### Componentes principales

#### FishCard
Tarjeta moderna con:
- Imagen de fondo con overlay gradiente
- Título sobre la imagen
- Botón de expansión
- Detalles expandibles con animación
- Botón de eliminación en modo expandido

#### BottomNavigationBar
Barra de navegación inferior con:
- 4 pestañas
- Iconos y etiquetas
- Animaciones de selección
- Material Design 3 styling

## 🔌 API Integration

La aplicación se conecta al backend mediante `FishApiService` que proporciona:

```kotlin
// Get all fish
suspend fun getAllFish(): Result<List<Fish>>

// Get fish by ID
suspend fun getFishById(id: Int): Result<Fish>

// Create new fish
suspend fun createFish(fish: Fish): Result<String>

// Update fish
suspend fun updateFish(id: Int, fish: Fish): Result<Unit>

// Delete fish
suspend fun deleteFish(id: Int): Result<Unit>
```

## 📦 Dependencias principales

```toml
# Ktor Client
ktor = "3.0.3"

# Serialization
kotlinx-serialization = "1.8.0"

# Image loading
coil = "3.0.4"

# Lifecycle
androidx-lifecycle = "2.9.4"
```

## 🐛 Solución de problemas

### Error de conexión
- Verificar que el backend esté corriendo en `http://localhost:8080`
- Verificar que PostgreSQL esté activo: `docker ps`

### Imágenes no cargan
- Verificar conexión a internet
- Verificar URLs de imágenes en la base de datos

### Build errors
```bash
# Limpiar y reconstruir
./gradlew clean
./gradlew build
```

## 🚧 Próximas características

- [ ] Formulario para agregar nuevos peces
- [ ] Edición de peces existentes
- [ ] Búsqueda y filtrado
- [ ] Ordenamiento (alfabético, por ID)
- [ ] Pantalla de detalles completa
- [ ] Compartir peces
- [ ] Modo offline con caché
- [ ] Autenticación de usuarios
- [ ] Favoritos

## 📝 Notas

- La aplicación requiere conexión al backend para funcionar
- Las imágenes se cargan de forma asíncrona con Coil
- El estado se gestiona de forma reactiva con StateFlow
- La UI es totalmente compartida entre plataformas (Android, iOS, Desktop)

