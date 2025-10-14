# Sistema de Gestión de Acuarios

Este documento describe el sistema completo de gestión de acuarios implementado en Nexus Aquarium.

## Características Principales

### 📊 Cálculos Automáticos de Volumen
- **Volumen Total**: Calculado automáticamente a partir de las dimensiones (largo × alto × profundidad)
- **Volumen Real**: Considera la reducción por sustrato, decoraciones y equipamiento
- **Porcentaje de Reducción**: Configurable (por defecto 15%)

### 🏠 Datos del Acuario
- **Información Básica**: Nombre, descripción, tipo de acuario
- **Dimensiones**: Largo, alto y profundidad en centímetros
- **Tipo de Acuario**: Agua dulce, salada, salobre, plantado, arrecife
- **Estado**: Activo/Inactivo

### 🔧 Equipamiento
- Calentador
- Filtro
- Iluminación
- Sistema de CO2
- Bomba de aire

### 🌱 Sustrato y Decoraciones
- **Tipos de Sustrato**: Grava, arena, tierra, fondo desnudo
- **Profundidad del Sustrato**: En centímetros
- **Plantas**: Indicador de presencia
- **Decoraciones**: Descripción de adornos

### 💧 Parámetros del Agua
- **Temperatura**: Actual y deseada
- **pH**: Actual y deseado
- **Dureza General (GH)**: Actual y deseada

### 📝 Mantenimiento
- **Cambios de Agua**: Último cambio y frecuencia
- **Limpieza**: Última limpieza registrada
- **Notas y Observaciones**: Campo libre para anotaciones

## Arquitectura del Sistema

### Backend (Ktor + PostgreSQL)
```
server/src/main/kotlin/
├── AquariumSchema.kt          # Modelo y servicio de base de datos
├── Databases.kt              # Configuración de endpoints
└── Application.kt            # Configuración principal
```

**Endpoints disponibles:**
- `GET /api/v1/aquariums` - Listar todos los acuarios
- `GET /api/v1/aquariums/{id}` - Obtener acuario por ID
- `POST /api/v1/aquariums` - Crear nuevo acuario
- `PUT /api/v1/aquariums/{id}` - Actualizar acuario
- `DELETE /api/v1/aquariums/{id}` - Eliminar acuario

### Frontend (Compose Multiplatform)
```
mobile/composeApp/src/commonMain/kotlin/com/nexusaquarium/
├── data/
│   ├── model/Aquarium.kt           # Modelo de datos
│   └── remote/AquariumApiService.kt # Servicio de API
├── ui/
│   ├── components/AquariumCard.kt   # Tarjeta de acuario
│   ├── screens/
│   │   ├── AquariumsListScreen.kt  # Lista de acuarios
│   │   ├── AddEditAquariumScreen.kt # Crear/Editar acuario
│   │   ├── AquariumDetailScreen.kt # Detalles del acuario
│   │   └── MyAquariumsScreen.kt     # Pantalla principal
│   └── viewmodel/AquariumViewModel.kt # ViewModel
```

## Funcionalidades Implementadas

### ✅ CRUD Completo
- **Crear**: Formulario completo con validaciones
- **Leer**: Lista y detalles con información completa
- **Actualizar**: Edición de todos los campos
- **Eliminar**: Confirmación y eliminación segura

### ✅ Cálculos Inteligentes
- Cálculo automático del volumen total
- Cálculo del volumen real considerando reducción
- Validaciones de campos numéricos
- Actualización en tiempo real de los cálculos

### ✅ Interfaz Intuitiva
- **Tarjetas Informativas**: Muestran información clave
- **Formularios Inteligentes**: Validación en tiempo real
- **Navegación Fluida**: Entre lista, detalles y edición
- **Estados de Carga**: Indicadores de progreso
- **Manejo de Errores**: Mensajes informativos

### ✅ Tipos de Acuario Soportados
- 🐟 **Agua Dulce**: Acuarios tropicales y de agua fría
- 🐠 **Agua Salada**: Acuarios marinos
- 🦐 **Agua Salobre**: Acuarios de transición
- 🌱 **Plantado**: Acuarios densamente plantados
- 🪸 **Arrecife**: Acuarios de arrecife marino

## Uso del Sistema

### 1. Crear un Acuario
1. Ir a "Mis Acuarios"
2. Presionar el botón "+" o "Crear Mi Primer Acuario"
3. Completar el formulario:
   - Nombre del acuario
   - Dimensiones (largo, alto, profundidad)
   - Tipo de acuario
   - Equipamiento
   - Sustrato y decoraciones
4. El volumen se calcula automáticamente
5. Guardar

### 2. Ver Lista de Acuarios
- Muestra tarjetas con información clave
- Estado del acuario (activo/inactivo)
- Dimensiones y volumen
- Tipo y categoría de tamaño
- Equipamiento instalado

### 3. Ver Detalles del Acuario
- Información completa del acuario
- Parámetros del agua
- Historial de mantenimiento
- Notas y observaciones

### 4. Editar Acuario
- Modificar cualquier campo
- Los cálculos se actualizan automáticamente
- Guardar cambios

## Categorías de Tamaño

El sistema clasifica automáticamente los acuarios por tamaño:
- 🔬 **Nano**: < 50L
- 📦 **Pequeño**: 50-100L
- 📦📦 **Mediano**: 100-200L
- 📦📦📦 **Grande**: 200-500L
- 🏢 **Muy Grande**: > 500L

## Datos de Ejemplo

El sistema incluye acuarios de ejemplo:
1. **Acuario Comunitario**: 60x30x30cm, agua dulce
2. **Nano Plantado**: 30x20x20cm, plantado con CO2

## Próximas Funcionalidades

- 📊 **Parámetros del Agua**: Registro y seguimiento
- 🐟 **Habitantes**: Gestión de peces y plantas
- 📈 **Historial**: Gráficos y tendencias
- 🔔 **Recordatorios**: Mantenimiento programado
- 📱 **Notificaciones**: Alertas de mantenimiento

## Tecnologías Utilizadas

### Backend
- **Ktor**: Framework web para Kotlin
- **PostgreSQL**: Base de datos relacional
- **Kotlinx Serialization**: Serialización JSON
- **Coroutines**: Programación asíncrona

### Frontend
- **Compose Multiplatform**: UI multiplataforma
- **Material Design 3**: Sistema de diseño
- **Ktor Client**: Cliente HTTP
- **State Management**: ViewModel y State

## Configuración

### Base de Datos
El sistema crea automáticamente la tabla `aquariums` con todos los campos necesarios.

### API
Los endpoints están disponibles en `/api/v1/aquariums` con soporte completo para CRUD.

### Frontend
El ViewModel maneja el estado y la comunicación con la API de forma reactiva.

## Conclusión

El sistema de gestión de acuarios proporciona una solución completa para:
- ✅ Gestión de información básica
- ✅ Cálculos automáticos de volumen
- ✅ Seguimiento de equipamiento
- ✅ Registro de parámetros
- ✅ Mantenimiento y observaciones

Todo esto con una interfaz intuitiva y cálculos automáticos que facilitan la gestión de acuarios de cualquier tamaño y tipo.
