@echo off
echo ========================================
echo Spring Boot Application Starter
echo ========================================
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Setting JAVA_HOME...
set JAVA_HOME=C:\Program Files\Java\jdk-17
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17.0.10
)
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.10.7-hotspot
)

echo JAVA_HOME set to: %JAVA_HOME%
echo.

echo Starting Spring Boot Application...
echo.
echo The application will be available at:
echo - http://localhost:8080
echo - http://localhost:8080/test.html (test page)
echo - http://localhost:8080/check-status.html (status check)
echo.

.\mvnw.cmd spring-boot:run

pause 