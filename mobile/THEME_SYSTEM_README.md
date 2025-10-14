# Sistema de Temas - Nexus Aquarium

## Descripción

Este sistema de temas permite a los usuarios cambiar entre tres modos de tema:
- **Claro**: Tema claro siempre activo
- **Oscuro**: Tema oscuro siempre activo  
- **Sistema**: Sigue la configuración del sistema operativo

## Características

### 🎨 Temas Disponibles
- **Tema Claro**: Colores brillantes y contrastantes para uso diurno
- **Tema Oscuro**: Colores suaves y oscuros para uso nocturno
- **Tema Sistema**: Se adapta automáticamente a la configuración del dispositivo

### 🎯 Funcionalidades
- ✅ Cambio de tema en tiempo real
- ✅ Persistencia de preferencias del usuario
- ✅ Vista previa de temas antes de aplicar
- ✅ Integración completa con Material Design 3
- ✅ Colores personalizados para la aplicación de acuarios

## Estructura de Archivos

```
ui/theme/
├── ThemeMode.kt          # Enum y extensiones para modos de tema
├── ThemeViewModel.kt     # ViewModel para gestión de estado
└── AppTheme.kt          # Configuración de colores y tema principal

ui/components/
├── ThemeSelector.kt     # Componente selector de tema
└── ThemePreview.kt      # Vista previa de temas

data/preferences/
└── ThemePreferences.kt  # Interfaz para persistencia
```

## Uso

### 1. Aplicar el tema en la aplicación principal

```kotlin
@Composable
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeMode by themeViewModel.themeMode.collectAsState()
    
    AppTheme(themeMode = themeMode) {
        // Tu contenido de la aplicación
    }
}
```

### 2. Usar el selector de tema en pantallas

```kotlin
@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel) {
    val themeMode by themeViewModel.themeMode.collectAsState()
    
    ThemeSelector(
        currentTheme = themeMode,
        onThemeSelected = { theme ->
            themeViewModel.updateThemeMode(theme)
        }
    )
}
```

### 3. Acceder al estado del tema

```kotlin
@Composable
fun MyComponent() {
    val themeViewModel: ThemeViewModel = viewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    
    // Usar isDarkTheme para lógica condicional
}
```

## Paleta de Colores

### Tema Claro
- **Primario**: Azul (#2196F3) - Representa el agua del acuario
- **Secundario**: Verde (#4CAF50) - Representa las plantas acuáticas
- **Terciario**: Cian (#00BCD4) - Representa el aire y oxígeno

### Tema Oscuro
- **Primario**: Azul claro (#64B5F6) - Más suave para la oscuridad
- **Secundario**: Verde claro (#81C784) - Verde más brillante
- **Terciario**: Cian claro (#4DD0E1) - Cian más vibrante

## Personalización

### Agregar nuevos colores
1. Edita `AppTheme.kt`
2. Agrega los colores en `AppColors` y `AppDarkColors`
3. Actualiza los `ColorScheme` correspondientes

### Modificar comportamiento del tema sistema
1. Edita `ThemeViewModel.kt`
2. Modifica la función `updateDarkThemeState()`
3. Implementa la detección del tema del sistema

## Persistencia

El sistema actualmente usa `InMemoryThemePreferences` para simplicidad. Para producción, implementa:

```kotlin
class SharedPreferencesThemePreferences(
    private val context: Context
) : ThemePreferences {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    override fun getThemeMode(): ThemeMode {
        val modeString = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return ThemeMode.valueOf(modeString ?: ThemeMode.SYSTEM.name)
    }
    
    override fun setThemeMode(themeMode: ThemeMode) {
        prefs.edit().putString("theme_mode", themeMode.name).apply()
    }
}
```

## Próximas Mejoras

- [ ] Detección automática del tema del sistema
- [ ] Temas personalizados por el usuario
- [ ] Animaciones de transición entre temas
- [ ] Temas estacionales (navidad, verano, etc.)
- [ ] Accesibilidad mejorada para usuarios con problemas de visión
