@echo off
echo === Database Setup Only ===
echo.

cd src

echo Compiling DatabaseSetup...
javac -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" DatabaseSetup.java DatabaseConnection.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running Database Setup...
java -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" DatabaseSetup

pause