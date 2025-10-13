// Script para cambiar entre entornos en Nexus Aquarium
// Copia y pega el código correspondiente en AppConfig.kt

// ========================================
// ENTORNO LOCAL (Emulador Android)
// ========================================
val CURRENT_ENVIRONMENT = Environment.LOCAL
// URL: http://10.0.2.2:4301
// Uso: Desarrollo con emulador Android
// Requisitos: Servidor corriendo en localhost:4301

// ========================================
// ENTORNO DEVELOP (Dispositivo Físico)
// ========================================
// val CURRENT_ENVIRONMENT = Environment.DEVELOP
// URL: http://192.168.1.100:4301
// Uso: Dispositivo físico en la misma red WiFi
// Requisitos: Cambiar 192.168.1.100 por tu IP real

// ========================================
// ENTORNO QA (Testing)
// ========================================
// val CURRENT_ENVIRONMENT = Environment.QA
// URL: http://qa.nexusaquarium.com:4301
// Uso: Servidor de pruebas
// Requisitos: Servidor QA desplegado

// ========================================
// ENTORNO PROD (Producción)
// ========================================
// val CURRENT_ENVIRONMENT = Environment.PROD
// URL: http://pappstest.com:4301
// Uso: Servidor de producción
// Requisitos: Servidor de producción desplegado

// ========================================
// INSTRUCCIONES DE USO:
// ========================================
// 1. Abrir AppConfig.kt
// 2. Buscar la línea: val CURRENT_ENVIRONMENT = Environment.LOCAL
// 3. Reemplazar por el entorno deseado
// 4. Guardar y recompilar la app
// 5. Probar la conexión

// ========================================
// VERIFICACIÓN:
// ========================================
// Para verificar que funciona:
// 1. Abrir la app
// 2. Ir a la sección "Peces"
// 3. Debería cargar los datos del servidor
// 4. Si hay error, verificar:
//    - Servidor corriendo
//    - URL correcta
//    - Conectividad de red
//    - Permisos de internet
