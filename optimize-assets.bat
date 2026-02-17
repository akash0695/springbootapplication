@echo off
echo ========================================
echo Asset Optimization Script
echo ========================================
echo.
echo Running PowerShell optimization script...
echo.

powershell -ExecutionPolicy Bypass -File "%~dp0optimize-assets.ps1"

pause
