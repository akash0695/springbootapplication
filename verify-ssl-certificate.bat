@echo off
echo ========================================
echo SSL Certificate Verification Tool
echo ========================================
echo.

set KEYSTORE_PATH=src\main\resources\keystore.p12
set KEYSTORE_PASSWORD=akashcreations2026

if not exist "%KEYSTORE_PATH%" (
    echo ERROR: Keystore file not found at: %KEYSTORE_PATH%
    echo.
    echo Please generate the certificate first using:
    echo   generate-ssl-certificate.bat
    pause
    exit /b 1
)

echo Checking keystore file...
echo.

echo Listing certificates in keystore:
keytool -list -v -keystore "%KEYSTORE_PATH%" -storepass "%KEYSTORE_PASSWORD%" -storetype PKCS12

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to read keystore. Possible issues:
    echo 1. Wrong password
    echo 2. Corrupted keystore file
    echo 3. Wrong keystore type
    pause
    exit /b 1
)

echo.
echo ========================================
echo Certificate Verification Complete
echo ========================================
echo.
echo Check the "Alias name" above - it should match:
echo   server.ssl.key-alias in application.properties
echo.
echo Current alias in config: akashcreationsspringboot
echo.
echo If the alias doesn't match, either:
echo 1. Update application.properties to use the correct alias
echo 2. Or regenerate the certificate with the correct alias
echo.
pause
