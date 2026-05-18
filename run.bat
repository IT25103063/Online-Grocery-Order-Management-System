@echo off
title FreshLink Supplier Portal
color 0A

echo.
echo  =====================================================
echo   FreshLink Supplier Management Portal
echo  =====================================================
echo.

:: Step 1: Check Java is installed
echo  [1/3] Checking Java installation...
where java >nul 2>nul
if %errorlevel% neq 0 (
    color 0C
    echo  [ERROR] Java is not installed or not in PATH!
    echo  Please install Java JDK 11+ and try again.
    pause
    exit /b 1
)
where javac >nul 2>nul
if %errorlevel% neq 0 (
    color 0C
    echo  [ERROR] Java compiler (javac) not found!
    echo  Please install Java JDK 11+ and try again.
    pause
    exit /b 1
)
echo  [OK] Java is available.
echo.

:: Step 2: Compile
echo  [2/3] Compiling application...
if not exist "backend\bin" mkdir "backend\bin"
javac -d backend/bin backend/src/*.java
if %errorlevel% neq 0 (
    color 0C
    echo  [ERROR] Compilation failed!
    pause
    exit /b 1
)
echo  [OK] Compiled successfully.
echo.

:: Step 3: Run the server (it will auto-open the browser)
echo  [3/3] Starting server...
echo.
echo  The application will open in your browser automatically.
echo  Press Ctrl+C in this window to stop the server.
echo.
echo  =====================================================
echo.

java -cp backend/bin Main
