import java.sql.*;
import java.io.*;
import java.nio.file.*;

public class DatabaseSetup {
    
    public static void main(String[] args) {
        System.out.println("=== Restaurant Database Setup ===");
        
        // First, test basic MySQL connection (without specific database)
        if (!testMySQLConnection()) {
            System.err.println("Cannot connect to MySQL server. Please check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Username/password are correct");
            System.err.println("3. MySQL is running on localhost:3306");
            return;
        }
        
        // Create database and tables
        setupDatabase();
        
        // Test final connection
        if (DatabaseConnection.testConnection()) {
            System.out.println("\n✅ Database setup completed successfully!");
            System.out.println("You can now run the restaurant application.");
        } else {
            System.err.println("\n❌ Database setup failed!");
        }
    }
    
    private static boolean testMySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/";
            Connection conn = DriverManager.getConnection(url, "root", "A@nchal911");
            conn.close();
            System.out.println("✅ MySQL server connection: SUCCESS");
            return true;
        } catch (Exception e) {
            System.err.println("❌ MySQL server connection: FAILED");
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    private static void setupDatabase() {
        try {
            // Connect to MySQL server (not specific database)
            String url = "jdbc:mysql://localhost:3306/";
            Connection conn = DriverManager.getConnection(url, "root", "A@nchal911");
            
            // Read and execute schema file
            String schemaPath = "../db/rst_schema.sql";
            executeSQL(conn, schemaPath, "Schema");
            
            // Read and execute seed data file
            String seedPath = "../db/db/seed_data.sql";
            executeSQL(conn, seedPath, "Seed Data");
            
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeSQL(Connection conn, String filePath, String description) {
        try {
            System.out.println("Executing " + description + "...");
            
            // Read SQL file
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            
            // Split by semicolon and execute each statement
            String[] statements = sql.split(";");
            Statement stmt = conn.createStatement();
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        stmt.execute(statement);
                    } catch (SQLException e) {
                        // Ignore "already exists" errors
                        if (!e.getMessage().contains("already exists")) {
                            System.err.println("Warning: " + e.getMessage());
                        }
                    }
                }
            }
            
            System.out.println("✅ " + description + " executed successfully");
            
        } catch (IOException e) {
            System.err.println("❌ Could not read " + filePath + ": " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ SQL execution error: " + e.getMessage());
        }
    }
}