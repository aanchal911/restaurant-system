@echo off
echo === Restaurant Management System ===
echo.

cd src

echo Compiling Java files...
javac -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" *.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

echo Testing database connection...
java -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" DatabaseConnection

echo.
echo Starting Restaurant Application...
java -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" Rst

pause