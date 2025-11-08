import java.sql.*;

public class CheckCategories {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("=== Category Mapping Issues ===");
            
            Statement stmt = conn.createStatement();
            
            // Check all categories
            System.out.println("All Categories:");
            ResultSet rs = stmt.executeQuery("SELECT * FROM category ORDER BY category_id");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("category_id") + " = " + rs.getString("name"));
            }
            
            System.out.println("\n=== Items with Wrong Categories ===");
            
            // Check items that should be in BEVERAGES but are in other categories
            rs = stmt.executeQuery("SELECT m.item_id, m.category_id, c.name as cat_name, m.name FROM menu m JOIN category c ON m.category_id = c.category_id WHERE m.name LIKE '%Lassi%' OR m.name LIKE '%Chai%' OR m.name LIKE '%Coffee%' OR m.name LIKE '%Soda%' ORDER BY m.item_id");
            System.out.println("Beverages in wrong categories:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("item_id") + " | Cat: " + rs.getString("cat_name") + " | Item: " + rs.getString("name"));
            }
            
            // Check items that should be in DESSERTS
            rs = stmt.executeQuery("SELECT m.item_id, m.category_id, c.name as cat_name, m.name FROM menu m JOIN category c ON m.category_id = c.category_id WHERE m.name LIKE '%Ice Cream%' OR m.name LIKE '%Kulfi%' OR m.name LIKE '%Halwa%' OR m.name LIKE '%Jamun%' ORDER BY m.item_id");
            System.out.println("\nDesserts in wrong categories:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("item_id") + " | Cat: " + rs.getString("cat_name") + " | Item: " + rs.getString("name"));
            }
            
            // Check duplicate items
            System.out.println("\n=== Duplicate Items ===");
            rs = stmt.executeQuery("SELECT name, COUNT(*) as count FROM menu GROUP BY name HAVING COUNT(*) > 1");
            while (rs.next()) {
                System.out.println("Duplicate: " + rs.getString("name") + " (appears " + rs.getInt("count") + " times)");
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}