@echo off
title GroCerXX Portal — Indestructible Server
color 0A
cls
echo.
echo  ================================================================
echo    GroCerXX — Indestructible Grocery Portal Launcher
echo  ================================================================
echo.
echo    This launcher boots the crash-proof backend and opens the app.
echo    KEEP THIS WINDOW OPEN while using the application.
echo.
echo  ================================================================
echo.

:: 1. Check Node.js
echo  [1/4] Checking Node.js installation...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo  [ERROR] Node.js is not installed or not in PATH!
    echo  Download from: https://nodejs.org/
    pause
    exit /b 1
)
for /f "tokens=*" %%v in ('node -v') do echo  [OK] Node.js %%v detected.
echo.

:: 2. Kill any stale processes on port 8080
echo  [2/4] Clearing port 8080...
for /f "tokens=5" %%a in ('netstat -aon 2^>nul ^| findstr :8080 ^| findstr LISTENING') do (
    echo  [WARN] Killing stale process PID %%a on port 8080...
    taskkill /F /PID %%a >nul 2>&1
)
echo  [OK] Port 8080 is ready.
echo.

:: 3. Open browser
echo  [3/4] Opening application in browser...
timeout /t 1 /nobreak >nul
start "" "http://localhost:8080/"
echo  [OK] Browser launched.
echo.

:: 4. Start server
echo  [4/4] Starting indestructible backend server...
echo.
echo  ================================================================
echo    Server logs will appear below. Do NOT close this window.
echo  ================================================================
echo.
node server.js

:: If server exits (should never happen with crash guards)
echo.
echo  [!] Server process ended. Press any key to restart...
pause >nul
goto :eof
