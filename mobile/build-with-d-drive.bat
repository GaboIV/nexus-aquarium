@echo off
echo Configurando Gradle para usar disco D:...
set GRADLE_USER_HOME=D:\GradleCache
echo GRADLE_USER_HOME configurado en: %GRADLE_USER_HOME%

echo.
echo Limpiando proyecto...
call gradlew clean

echo.
echo Compilando proyecto...
call gradlew assembleDebug

echo.
echo Compilacion completada!
pause
