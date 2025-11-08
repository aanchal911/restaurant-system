import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_2_0"; 
    private static final String USER = "root";  
    private static final String PASS = "A@nchal911";      
    
    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-j-9.4.0.jar is in classpath");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database connection established successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database:");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
    
    // Test database connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("SELECT 1");
            System.out.println("Database connection test: SUCCESS");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED");
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        testConnection();
    }
}
