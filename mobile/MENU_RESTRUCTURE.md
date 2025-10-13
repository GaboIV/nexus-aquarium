# Reestructuración del Menú - Nexus Aquarium

## Resumen de Cambios

Se ha reestructurado completamente el menú de navegación inferior de la aplicación para reflejar una arquitectura más intuitiva y centrada en el usuario acuarista.

## Nueva Estructura de Navegación

### Menú Inferior (4 Secciones Principales)

#### 1. 🏠 Home
**Ruta:** `home`
**Archivo:** `NewHomeScreen.kt`

**Contenido:**
- Tarjeta de bienvenida
- Resumen rápido (estadísticas de acuarios, peces y parámetros)
- Peces más populares (carrusel horizontal)
- Gráficos rápidos (placeholder)
- Recomendaciones personalizadas

**Estado:** ✅ Maqueta completa

#### 2. 🐟 Peces
**Ruta:** `fish`
**Archivo:** `FishScreen.kt`

**Contenido:**
- Lista completa de peces desde el API
- Búsqueda de peces
- Botón para actualizar lista
- Tarjeta de estadísticas
- Integración con FishViewModel existente

**Estado:** ✅ Funcional con API

#### 3. 💧 Mis Acuarios
**Ruta:** `my_aquariums`
**Archivo:** `MyAquariumsScreen.kt`

**Contenido:**
- Lista de acuarios del usuario
- Vista vacía con información de funcionalidades
- Botón flotante para agregar acuario
- Tarjetas de acuario con:
  - Nombre
  - Volumen
  - Cantidad de peces
  - Acceso a detalles

**Funcionalidades dentro de cada acuario (futuro):**
- 💧 Parámetros del Agua
- 🐟 Habitantes
- 📊 Historial

**Estado:** ✅ Maqueta completa

#### 4. 👤 Mi Cuenta
**Ruta:** `my_account`
**Archivo:** `MyAccountScreen.kt`

**Contenido:**
- Tarjeta de perfil con avatar y estadísticas
- Sección de Cuenta:
  - Editar perfil
  - Email
  - Cambiar contraseña
- Sección de Preferencias:
  - Notificaciones
  - Idioma
  - Tema
- Sección de Aplicación:
  - Acerca de
  - Ayuda y soporte
  - Privacidad
- Cerrar sesión

**Estado:** ✅ Maqueta completa

## Arquitectura de Información

```
App
├── Home (Dashboard)
│   ├── Resumen de acuarios
│   ├── Peces populares
│   ├── Gráficos rápidos
│   └── Recomendaciones
│
├── Peces (Catálogo desde API)
│   ├── Lista de peces
│   ├── Búsqueda
│   └── Agregar a acuario
│
├── Mis Acuarios
│   ├── Lista de acuarios
│   └── [Al seleccionar un acuario]
│       ├── Parámetros
│       ├── Habitantes
│       └── Historial
│
└── Mi Cuenta
    ├── Perfil
    ├── Configuración
    └── Preferencias
```

## Archivos Modificados

### Navegación
- ✅ `ui/navigation/BottomNavItem.kt` - Actualizado con 4 nuevas secciones
- ✅ `ui/navigation/BottomNavigationBar.kt` - Referencias actualizadas
- ✅ `App.kt` - Rutas y pantallas actualizadas

### Nuevas Pantallas
- ✅ `ui/screens/NewHomeScreen.kt` - Dashboard principal
- ✅ `ui/screens/FishScreen.kt` - Catálogo de peces (usa API)
- ✅ `ui/screens/MyAquariumsScreen.kt` - Gestión de acuarios
- ✅ `ui/screens/MyAccountScreen.kt` - Perfil y configuración

### Pantallas Antiguas (Ahora Obsoletas)
Las siguientes pantallas fueron reemplazadas pero se mantienen para referencia:
- `AquariumsScreen.kt` (reemplazada por MyAquariumsScreen.kt)
- `ParametersScreen.kt` (se moverá dentro de detalle de acuario)
- `InhabitantsScreen.kt` (se moverá dentro de detalle de acuario)
- `HistoryScreen.kt` (se moverá dentro de detalle de acuario)
- `HomeScreen.kt` (reemplazada por FishScreen.kt)

## Características de las Maquetas

Todas las pantallas incluyen:
- ✅ Material Design 3
- ✅ Gradientes de fondo
- ✅ Tarjetas con elevación
- ✅ Iconos representativos
- ✅ Textos en español (UI)
- ✅ Comentarios en inglés (código)
- ✅ Estados vacíos informativos
- ✅ Diseño responsive

## Estado de Compilación

- ✅ Compila sin errores
- ✅ Todas las importaciones correctas
- ✅ Navegación funcional
- ✅ API de peces integrada

## Próximos Pasos

### Corto Plazo
1. Implementar navegación a detalle de acuario
2. Crear pantalla de detalle de acuario con tabs:
   - Información general
   - Parámetros
   - Habitantes
   - Historial
3. Implementar formularios de creación/edición de acuarios
4. Agregar funcionalidad de búsqueda en peces

### Mediano Plazo
1. Implementar modelos de datos (Aquarium, WaterParameter, Inhabitant)
2. Configurar base de datos local
3. Crear ViewModels para cada pantalla
4. Implementar sistema de gráficos
5. Agregar autenticación de usuarios

### Largo Plazo
1. Sincronización con backend
2. Sistema de recomendaciones inteligentes
3. Comunidad y peces populares reales
4. Notificaciones y recordatorios
5. Exportación de datos

## Notas Técnicas

- **Framework:** Kotlin Multiplatform con Compose Multiplatform
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **UI:** Material Design 3
- **API:** Integración existente con FishViewModel
- **Compatibilidad:** Android, iOS, Desktop (JVM)

## Decisiones de Diseño

### ¿Por qué 4 secciones principales?

1. **Home:** Punto de entrada rápido con información relevante
2. **Peces:** Acceso directo al catálogo para explorar especies
3. **Mis Acuarios:** Gestión central de los acuarios del usuario
4. **Mi Cuenta:** Configuración y personalización

### ¿Por qué mover Parámetros/Habitantes/Historial dentro de cada acuario?

- **Contexto:** Estos datos son específicos de cada acuario
- **Organización:** Evita confusión sobre qué acuario se está gestionando
- **Escalabilidad:** Permite gestionar múltiples acuarios fácilmente
- **UX:** Flujo más natural: seleccionar acuario → ver/editar sus datos

## Feedback del Usuario

Esta estructura permite:
- ✅ Acceso rápido a información importante (Home)
- ✅ Exploración de especies (Peces)
- ✅ Gestión organizada por acuario (Mis Acuarios)
- ✅ Personalización de la experiencia (Mi Cuenta)

