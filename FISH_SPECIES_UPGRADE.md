# Actualización del Sistema de Especies de Peces

## Resumen de Cambios

Se ha implementado un sistema completo y rico en información para las especies de peces, transformando el modelo básico anterior en una ficha detallada e interactiva que proporciona toda la información necesaria para el cuidado y compatibilidad de peces de acuario.

## Cambios en el Backend

### Modelo de Datos Enriquecido (`Fish`)

El modelo `Fish` ahora incluye **31 campos** organizados en categorías:

#### Información Básica
- `commonName`: Nombre común del pez
- `scientificName`: Nombre científico (taxonómico)
- `imageUrl`: Imagen principal
- `imageGallery`: Galería de imágenes adicionales (opcional)

#### Parámetros del Agua
- `tempMinC` / `tempMaxC`: Rango de temperatura en °C
- `phMin` / `phMax`: Rango de pH
- `ghMin` / `ghMax`: Dureza general del agua (opcional)

#### Tamaño y Requisitos del Acuario
- `maxSizeCm`: Tamaño máximo del pez adulto
- `minTankSizeLiters`: Tamaño mínimo del acuario en litros

#### Comportamiento y Compatibilidad
- `temperament`: Temperamento (peaceful, semi_aggressive, aggressive)
- `socialBehavior`: Comportamiento social (schooling, shoaling, pairs, solitary)
- `minGroupSize`: Tamaño mínimo del grupo (si aplica)
- `tankLevel`: Nivel de nado (bottom, mid, top, all)
- `isPredator`: Indica si es depredador
- `isFinNipper`: Indica si muerde aletas

#### Visión General
- `origin`: Origen geográfico
- `lifeExpectancyYears`: Esperanza de vida
- `difficultyLevel`: Nivel de dificultad (beginner, intermediate, advanced)

#### Configuración del Acuario
- `tankSetupDescription`: Descripción detallada del setup ideal

#### Comportamiento Detallado
- `behaviorDescription`: Descripción del comportamiento
- `idealTankMates`: Compañeros de tanque ideales

#### Alimentación
- `dietType`: Tipo de dieta (omnivore, carnivore, herbivore)
- `recommendedFoods`: Alimentos recomendados

#### Reproducción
- `sexualDimorphism`: Diferencias entre machos y hembras
- `breedingDifficulty`: Dificultad de cría (easy, moderate, difficult)
- `breedingMethod`: Método de reproducción

#### Variantes
- `hasVariants`: Indica si tiene variantes de color
- `variantsDescription`: Descripción de las variantes

### Base de Datos

La tabla `fish` ha sido actualizada con todos los campos correspondientes, utilizando tipos de datos apropiados:
- `DECIMAL` para valores numéricos con precisión (temperatura, pH, tamaño)
- `INTEGER` para valores enteros (litros, años, dureza)
- `BOOLEAN` para flags (isPredator, isFinNipper, hasVariants)
- `TEXT` para descripciones largas

### Datos de Ejemplo

Se han agregado **4 especies completamente documentadas**:

1. **Pez Neón** (Paracheirodon innesi)
   - Pez de cardumen pacífico
   - Ideal para principiantes
   - Temperatura: 22-26°C, pH: 6.0-7.5

2. **Guppy** (Poecilia reticulata)
   - Vivíparo con múltiples variantes
   - Muy fácil de criar
   - Temperatura: 22-28°C, pH: 6.8-8.0

3. **Corydora Panda** (Corydoras panda)
   - Pez de fondo pacífico
   - Vive en cardumen
   - Temperatura: 20-25°C, pH: 6.0-7.5

4. **Platy** (Xiphophorus maculatus)
   - Vivíparo con muchas variantes de color
   - Muy resistente
   - Temperatura: 20-26°C, pH: 7.0-8.2

## Cambios en el Mobile

### Modelo de Datos

El modelo `Fish` en mobile ahora refleja exactamente la estructura del backend, con funciones helper para la UI:

```kotlin
fun getTemperatureRange(): String
fun getPhRange(): String
fun getTemperamentEmoji(): String  // ☮️, ⚠️, 🛑
fun getSocialBehaviorEmoji(): String  // 🐠🐠, 💑, 👤
fun getDifficultyStars(): String  // ⭐, ⭐⭐, ⭐⭐⭐
```

### FishCard Mejorado

La tarjeta de pez ahora muestra una **Ficha Básica** con:

#### Siempre Visible
- Imagen principal
- Nombre común y científico
- **Parámetros críticos del agua**:
  - 🌡️ Temperatura
  - 💧 pH
  - 📏 Tamaño máximo
- **Iconos de alerta rápida**:
  - Temperamento con emoji (☮️ Pacífico, ⚠️ Semi-agresivo, 🛑 Agresivo)
  - Comportamiento social con emoji (🐠🐠 Cardumen, 💑 Pareja, 👤 Solitario)
  - Dificultad con estrellas (⭐ Principiante, ⭐⭐ Intermedio, ⭐⭐⭐ Avanzado)

#### Sección Expandible
- Visión general (origen, esperanza de vida, acuario mínimo, nivel de nado)
- Alimentación (tipo de dieta)
- **Advertencias** (si es depredador o muerde aletas)
- Botón para eliminar

### Nueva Pantalla: FishDetailScreen

Se ha creado una pantalla completa de detalle con todas las secciones:

#### Hero Section
- Imagen grande del pez
- Nombre común y científico

#### Quick Stats
- Tres tarjetas con temperamento, comportamiento social y dificultad

#### Advertencias
- Card destacado si el pez es depredador o muerde aletas

#### Secciones Detalladas
1. **Visión General**
   - Origen
   - Esperanza de vida
   - Tamaño máximo

2. **Parámetros del Agua**
   - Temperatura
   - pH
   - Dureza (GH) si aplica

3. **Requisitos del Acuario**
   - Tamaño mínimo
   - Nivel de nado
   - Tamaño mínimo del grupo
   - Descripción del setup ideal

4. **Comportamiento y Compatibilidad**
   - Descripción del comportamiento
   - Compañeros ideales de tanque

5. **Alimentación**
   - Tipo de dieta
   - Alimentos recomendados

6. **Reproducción**
   - Dificultad de cría
   - Dimorfismo sexual
   - Método de reproducción

7. **Variantes** (si aplica)
   - Card especial con descripción de variantes de color

## Motor de Compatibilidad (Fundamentos)

Con la nueva estructura de datos, ahora es posible implementar un motor de compatibilidad que verifique:

### Reglas de Compatibilidad

1. **Parámetros de Agua**
   - Los rangos de temperatura deben solaparse
   - Los rangos de pH deben solaparse
   - La dureza (GH) debe ser compatible

2. **Temperamento**
   - Un pez `aggressive` no es compatible con `peaceful`
   - Los peces `semi_aggressive` requieren evaluación caso por caso

3. **Comportamiento Social**
   - Los peces `schooling`/`shoaling` requieren el número mínimo de individuos
   - Advertir si se intenta agregar menos del `minGroupSize`

4. **Nivel de Nado**
   - Una buena comunidad tiene peces en todos los niveles (bottom, mid, top)
   - Evitar sobrepoblación en un solo nivel

5. **Depredadores**
   - Si `isPredator = true`, verificar que otros peces no sean lo suficientemente pequeños

6. **Mordedores de Aletas**
   - Si `isFinNipper = true`, no es compatible con peces de aletas largas (Guppies, Bettas)

7. **Tamaño del Acuario**
   - El acuario debe cumplir con el `minTankSizeLiters` de todas las especies
   - Considerar el tamaño total de la población

## Manejo de Variantes

Para especies con muchas variantes (Platies, Guppies, Corydoras), se usa el siguiente enfoque:

- **Una ficha general** para la especie principal
- Campo `hasVariants = true`
- Campo `variantsDescription` con lista de variantes populares
- **Nota importante**: Solo se crea una ficha separada si una variante tiene requisitos de cuidado drásticamente diferentes

### Ejemplo: Platy
- Ficha única para *Xiphophorus maculatus*
- `variantsDescription`: "Mickey Mouse, Sunset, Tuxedo, Wagtail, Blue, Red Coral"
- Todos comparten los mismos parámetros de agua y cuidados

## Próximos Pasos

### Implementación Sugerida

1. **Motor de Compatibilidad**
   - Crear servicio que analice compatibilidad entre especies
   - Endpoint `/api/v1/compatibility/check` que reciba lista de especies
   - Retornar warnings y errores de compatibilidad

2. **Búsqueda y Filtros**
   - Filtrar por temperamento
   - Filtrar por nivel de dificultad
   - Filtrar por rango de temperatura/pH
   - Buscar por nombre común o científico

3. **Recomendaciones**
   - Endpoint `/api/v1/fish/recommendations` que sugiera peces compatibles
   - Basado en los peces actuales del usuario
   - Considerar parámetros del acuario del usuario

4. **Integración con Acuarios del Usuario**
   - Validar compatibilidad al agregar pez a un acuario
   - Mostrar warnings antes de confirmar
   - Sugerir ajustes de parámetros si es necesario

5. **Más Especies**
   - Agregar más especies populares
   - Categorizar por tipo (Tetras, Cíclidos, Vivíparos, etc.)
   - Agregar imágenes de alta calidad

## Archivos Modificados

### Backend
- `server/src/main/kotlin/FishSchema.kt` - Modelo y servicio completamente reescrito

### Mobile
- `mobile/composeApp/src/commonMain/kotlin/com/nexusaquarium/data/model/Fish.kt` - Modelo actualizado
- `mobile/composeApp/src/commonMain/kotlin/com/nexusaquarium/ui/components/FishCard.kt` - Card mejorado con ficha básica
- `mobile/composeApp/src/commonMain/kotlin/com/nexusaquarium/ui/screens/FishDetailScreen.kt` - Nueva pantalla de detalle completo

## Notas Técnicas

- Todos los textos descriptivos están en español
- Todos los comentarios de código están en inglés
- El backend compila correctamente
- La base de datos se crea automáticamente con los datos de ejemplo
- Las imágenes son URLs externas (considerar migrar a almacenamiento local)

