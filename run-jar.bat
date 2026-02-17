@echo off
echo ========================================
echo Spring Boot Application - JAR Runner
echo ========================================
echo.
echo Starting application with optimized memory settings...
echo.
echo Memory Configuration:
echo - Initial Heap: 128MB
echo - Maximum Heap: 384MB
echo - Metaspace: 64MB-128MB
echo - GC: G1 Garbage Collector
echo.

if not exist "target\springboot-0.0.1-SNAPSHOT.jar" (
    echo ERROR: JAR file not found. Please build the project first using:
    echo   mvnw.cmd clean package
    pause
    exit /b 1
)

java -Xms128m -Xmx384m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=200 -jar target\springboot-0.0.1-SNAPSHOT.jar

pause
