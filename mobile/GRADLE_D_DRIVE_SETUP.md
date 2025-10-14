# Configuración de Gradle en Disco D:

Este documento explica cómo configurar Gradle para usar el disco D: en lugar del disco C: para evitar problemas de espacio.

## ✅ **Configuración Completada**

### 1. **Archivos Creados/Modificados**

- **`gradle.properties`**: Configuración principal con `gradle.user.home=D:\\GradleCache`
- **`local.properties`**: Configuración local del proyecto
- **`build-with-d-drive.bat`**: Script para compilar usando disco D:
- **`gradle-wrapper.properties`**: Configuración del wrapper de Gradle

### 2. **Directorio de Caché**
- **Ubicación**: `D:\GradleCache`
- **Contenido**: Todas las dependencias y caché de Gradle

## 🚀 **Cómo Usar**

### **Opción 1: Script Automático (Recomendado)**
```bash
# Ejecutar el script que configura automáticamente el disco D:
build-with-d-drive.bat
```

### **Opción 2: Comando Manual**
```bash
# Configurar variable de entorno y compilar
set GRADLE_USER_HOME=D:\GradleCache && ./gradlew assembleDebug
```

### **Opción 3: PowerShell**
```powershell
# Configurar variable de entorno
$env:GRADLE_USER_HOME = "D:\GradleCache"
./gradlew assembleDebug
```

## 📋 **Verificación**

### **Verificar que funciona:**
```bash
# Limpiar proyecto
set GRADLE_USER_HOME=D:\GradleCache && ./gradlew clean

# Compilar proyecto
set GRADLE_USER_HOME=D:\GradleCache && ./gradlew assembleDebug
```

### **Verificar directorio de caché:**
```bash
# Ver contenido del directorio D:
dir "D:\GradleCache"
```

## 🔧 **Configuración Permanente (Opcional)**

Para configurar permanentemente la variable de entorno:

1. **Abrir Variables de Entorno**:
   - Presionar `Win + R`
   - Escribir `sysdm.cpl`
   - Ir a "Opciones avanzadas" → "Variables de entorno"

2. **Agregar Variable**:
   - **Nombre**: `GRADLE_USER_HOME`
   - **Valor**: `D:\GradleCache`

3. **Reiniciar terminal** para que tome efecto

## 📊 **Beneficios**

- ✅ **Libera espacio en disco C:**
- ✅ **Mejor rendimiento** (disco D: con más espacio)
- ✅ **Configuración centralizada**
- ✅ **Fácil de mantener**

## 🛠️ **Solución de Problemas**

### **Si no funciona:**
1. Verificar que el disco D: tenga espacio suficiente
2. Verificar permisos de escritura en D:
3. Usar el script `build-with-d-drive.bat`

### **Limpiar caché antiguo:**
```bash
# Limpiar caché del disco C: (opcional)
Remove-Item "C:\Users\Gabriel\.gradle\caches" -Recurse -Force
```

## 📝 **Notas Importantes**

- El directorio `D:\GradleCache` se crea automáticamente
- No es necesario mover archivos manualmente
- La configuración es específica para este proyecto
- El script `build-with-d-drive.bat` es la forma más fácil de usar
