@echo off
echo ========================================
echo   Serveur ChatRoom RMI
echo ========================================
echo.
echo Demarrage du serveur...
echo.

cd /d "%~dp0"

REM Compiler le projet si nécessaire
if not exist "target\classes" (
    echo Compilation du projet...
    call mvn clean compile
    if errorlevel 1 (
        echo Erreur lors de la compilation!
        pause
        exit /b 1
    )
)

echo Lancement du serveur...
echo.
mvn exec:java -Dexec.mainClass="com.chat.server.ChatServer"

pause
