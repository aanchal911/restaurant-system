@echo off
echo === Testing Menu Loading ===
echo.

cd src

echo Compiling test files...
javac -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" TestMenu.java DatabaseConnection.java MenuItem.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running menu test...
java -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" TestMenu

pause