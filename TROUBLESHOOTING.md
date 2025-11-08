# Restaurant App - Database Connection Troubleshooting Guide

## Common Issues and Solutions

### 1. MySQL JDBC Driver Not Found
**Error:** `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
**Solution:** 
- Ensure `mysql-connector-j-9.4.0.jar` is in the `lib/mysql-connector-j-9.4.0/` folder
- Check that the classpath in run.bat includes the correct jar file path

### 2. Database Connection Failed
**Error:** `SQLException: Access denied for user 'root'@'localhost'`
**Solutions:**
- Verify MySQL server is running
- Check username/password in DatabaseConnection.java
- Ensure MySQL is running on port 3306

### 3. Database Does Not Exist
**Error:** `SQLException: Unknown database 'restaurant'`
**Solution:**
- Run `setup_db.bat` to create the database and tables
- Or manually run the SQL files in the db folder

### 4. Tables Not Found
**Error:** `SQLException: Table 'restaurant.menu' doesn't exist`
**Solution:**
- Run the database setup: `setup_db.bat`
- Check if rst_schema.sql was executed properly

## Setup Steps

1. **Install MySQL Server**
   - Download and install MySQL Server
   - Set root password to match DatabaseConnection.java
   - Start MySQL service

2. **Setup Database**
   ```batch
   # Run this command in the project root
   setup_db.bat
   ```

3. **Run Application**
   ```batch
   # Run this command in the project root
   run.bat
   ```

## Manual Database Setup

If automatic setup fails, run these SQL commands manually:

```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE IF NOT EXISTS restaurant;
USE restaurant;

-- Run the schema file
source /path/to/restaurant-app/db/rst_schema.sql;

-- Run the seed data file
source /path/to/restaurant-app/db/db/seed_data.sql;
```

## Verification

To verify everything is working:

1. Check if MySQL is running:
   ```batch
   net start mysql
   ```

2. Test database connection:
   ```batch
   cd src
   java -cp ".;../lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar" DatabaseSetup
   ```

3. Run the application:
   ```batch
   run.bat
   ```

## Configuration

Current database configuration in DatabaseConnection.java:
- URL: jdbc:mysql://localhost:3306/restaurant
- Username: root
- Password: A@nchal911

Change these values if your MySQL setup is different.