import java.sql.*;
import java.util.List;

public class TestMenu {
    public static void main(String[] args) {
        System.out.println("=== Testing Menu Loading ===");
        
        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("Cannot connect to database!");
            return;
        }
        
        // Test menu loading
        List<MenuItem> menu = Database.fetchMenu();
        
        if (menu.isEmpty()) {
            System.err.println("No menu items found!");
            System.err.println("Checking database tables...");
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check if tables exist and have data
                Statement stmt = conn.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM category");
                rs.next();
                int categoryCount = rs.getInt(1);
                System.out.println("Categories in database: " + categoryCount);
                
                rs = stmt.executeQuery("SELECT COUNT(*) FROM menu");
                rs.next();
                int menuCount = rs.getInt(1);
                System.out.println("Menu items in database: " + menuCount);
                
                rs = stmt.executeQuery("SELECT COUNT(*) FROM menu WHERE is_active = TRUE");
                rs.next();
                int activeMenuCount = rs.getInt(1);
                System.out.println("Active menu items: " + activeMenuCount);
                
            } catch (SQLException e) {
                System.err.println("Error checking tables: " + e.getMessage());
            }
        } else {
            System.out.println("Successfully loaded " + menu.size() + " menu items:");
            for (MenuItem item : menu) {
                System.out.printf("[%d] %s - %s - ₹%.2f\n", 
                    item.getItemId(), item.getCategoryName(), 
                    item.getName(), item.getPrice());
            }
        }
    }
}