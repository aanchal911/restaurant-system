import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("=== Testing Database Tables ===");
            
            // Check what tables exist
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables("restaurant_2_0", null, "%", new String[]{"TABLE"});
            
            System.out.println("Tables in database:");
            while (tables.next()) {
                System.out.println("- " + tables.getString("TABLE_NAME"));
            }
            
            // Check category table
            System.out.println("\n=== Category Table ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM category LIMIT 5");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt(1) + ", Name: " + rs.getString(2));
            }
            
            // Check menu table structure
            System.out.println("\n=== Menu Table Structure ===");
            rs = stmt.executeQuery("DESCRIBE menu");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString(1) + " - Type: " + rs.getString(2));
            }
            
            // Check menu data
            System.out.println("\n=== Menu Data (first 5 items) ===");
            rs = stmt.executeQuery("SELECT * FROM menu LIMIT 5");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("item_id") + 
                                 ", Category: " + rs.getInt("category_id") + 
                                 ", Name: " + rs.getString("name") + 
                                 ", Price: " + rs.getDouble("price"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}