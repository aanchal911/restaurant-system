import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

class Database {
    static List<MenuItem> fetchMenu() {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT MIN(m.item_id) as item_id, m.category_id, c.name as category_name, m.name, m.price, m.description FROM menu m JOIN category c ON m.category_id = c.category_id GROUP BY m.name, m.category_id, c.name, m.price, m.description ORDER BY c.name, m.name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String description = rs.getString("description");
                if (description == null) description = "Delicious item";
                
                String itemName = rs.getString("name");
                String categoryName = rs.getString("category_name");
                int categoryId = rs.getInt("category_id");
                
                // Fix wrong categories based on item names
                if (itemName.contains("Dosa") || itemName.contains("Idli") || itemName.contains("Vada") || itemName.contains("Fish Curry")) {
                    categoryName = "South Indian";
                } else if (itemName.contains("Manchurian") || itemName.contains("Hakka") || itemName.contains("Schezwan") || itemName.contains("Chilli Paneer") || itemName.contains("Chicken 65")) {
                    categoryName = "Chinese";
                } else if (itemName.contains("Dal Makhani") || itemName.contains("Butter Chicken") || itemName.contains("Chole") || itemName.contains("Sarson") || itemName.contains("Rogan Josh")) {
                    categoryName = "Punjabi";
                } else if (itemName.contains("Rice") || itemName.contains("Biryani")) {
                    categoryName = "Rice";
                } else if (itemName.contains("Naan") || itemName.contains("Roti") || itemName.contains("Paratha")) {
                    categoryName = "Breads";
                } else if (itemName.contains("Salad")) {
                    categoryName = "Salads";
                } else if (itemName.contains("Lassi") || itemName.contains("Chai") || itemName.contains("Coffee") || itemName.contains("Soda") || itemName.contains("Chaas")) {
                    categoryName = "Beverages";
                } else if (itemName.contains("Ice Cream") || itemName.contains("Kulfi") || itemName.contains("Halwa") || itemName.contains("Jamun") || itemName.contains("Rasmalai") || itemName.contains("Falooda")) {
                    categoryName = "Desserts";
                }
                
                items.add(new MenuItem(
                    rs.getInt("item_id"),
                    categoryId,
                    categoryName,
                    itemName,
                    rs.getDouble("price"),
                    description
                ));
            }
            System.out.println("Loaded " + items.size() + " menu items from database");
        } catch (SQLException e) {
            System.err.println("Failed to load menu from database: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }
    
    static List<Table> fetchTables() {
        List<Table> tables = new ArrayList<>();
        // Create default tables since restaurant_table doesn't exist
        for (int i = 1; i <= 5; i++) {
            tables.add(new Table(i, "T" + i, 4, false));
        }
        return tables;
    }
}

public class Rst extends JFrame {
    private static List<MenuItem> menu = new ArrayList<>();
    private static List<Table> tables = new ArrayList<>();
    private JTextArea displayArea;
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(231, 76, 60);
    private Color bgColor = new Color(236, 240, 241);

    public Rst() {
        initializeComponents();
    }
    
    private void initializeApplication() {
        boolean dbConnected = DatabaseConnection.testConnection();
        menu = Database.fetchMenu();
        tables = Database.fetchTables();
        
        StringBuilder welcomeMsg = new StringBuilder();
        welcomeMsg.append("\n\n");
        welcomeMsg.append("=".repeat(65)).append("\n");
        welcomeMsg.append("                 WELCOME TO AANCHAL'S RESTAURANT\n");
        welcomeMsg.append("=".repeat(65)).append("\n\n");
        
        welcomeMsg.append("        A CULINARY JOURNEY AROUND THE WORLD\n\n");
        
        welcomeMsg.append("-".repeat(60)).append("\n");
        welcomeMsg.append("                OUR SIGNATURE CUISINES\n");
        welcomeMsg.append("-".repeat(60)).append("\n");
        welcomeMsg.append("   * Authentic Punjabi Delicacies\n");
        welcomeMsg.append("   * Traditional South Indian Flavors\n");
        welcomeMsg.append("   * Exotic Chinese Specialties\n");
        welcomeMsg.append("   * Fresh Soups & Appetizers\n");
        welcomeMsg.append("   * Aromatic Rice & Biryanis\n");
        welcomeMsg.append("   * Freshly Baked Breads\n");
        welcomeMsg.append("   * Refreshing Beverages & Desserts\n");
        welcomeMsg.append("-".repeat(60)).append("\n\n");
        
        welcomeMsg.append("                 QUICK START GUIDE\n\n");
        welcomeMsg.append("   1. Click 'Show Menu' to browse our delicious items\n");
        welcomeMsg.append("   2. Click 'Book Table & Order' to place your order\n");
        welcomeMsg.append("   3. Add more items anytime with 'Add Items to Order'\n");
        welcomeMsg.append("   4. Generate bill with 'Final Billing' when done\n\n");
        
        welcomeMsg.append("*".repeat(60)).append("\n");
        welcomeMsg.append("     Thank you for choosing Aanchal's Restaurant!\n");
        welcomeMsg.append("      We promise an unforgettable dining experience!\n");
        welcomeMsg.append("*".repeat(60)).append("\n");
        
        displayArea.setText(welcomeMsg.toString());
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(220, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void showMenu() {
        StringBuilder menuText = new StringBuilder();
        menuText.append("🍽️ OUR DELICIOUS MENU 🍽️\n");
        menuText.append("=".repeat(50)).append("\n\n");
        
        if (menu.isEmpty()) {
            menuText.append("⚠️ No menu items found!\n\n");
            menuText.append("Please check:\n");
            menuText.append("• Database connection is working\n");
            menuText.append("• Menu table has data\n");
            menuText.append("• Category table has data\n");
        } else {
            String currentCategory = "";
            for (MenuItem item : menu) {
                if (!item.getCategoryName().equals(currentCategory)) {
                    currentCategory = item.getCategoryName();
                    menuText.append("\n📂 ").append(currentCategory.toUpperCase()).append("\n");
                    menuText.append("-".repeat(30)).append("\n");
                }
                menuText.append(String.format("[%d] %s\n", item.getItemId(), item.getName()));
                menuText.append(String.format("    💰 ₹%.2f | %s\n\n", item.getPrice(), item.getDescription()));
            }
            menuText.append("\n👨🍳 All items are freshly prepared!");
        }
        
        displayArea.setText(menuText.toString());
    }
    
    private void showMessage(String message) {
        displayArea.setText(message);
    }
    
    private void openOrderDialog() {
        if (menu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No menu items available! Please check database connection.");
            return;
        }
        new SimpleOrderDialog(this, menu).setVisible(true);
    }
    
    private void showActiveOrders() {
        StringBuilder orderText = new StringBuilder();
        orderText.append("📋 ACTIVE ORDERS 📋\n");
        orderText.append("=".repeat(50)).append("\n\n");
        
        if (!OrderManager.hasActiveOrders()) {
            orderText.append("🔍 No active orders found.\n\n");
            orderText.append("💡 Use 'Book Table & Order' to create new orders.");
        } else {
            for (String table : OrderManager.getActiveOrderTables()) {
                orderText.append(String.format("🎫 %s | Total: ₹%.2f\n", table, OrderManager.getOrderTotal(table)));
                orderText.append("-".repeat(40)).append("\n");
                
                for (String item : OrderManager.getOrderItems(table)) {
                    orderText.append("  • ").append(item).append("\n");
                }
                orderText.append("\n");
            }
        }
        
        displayArea.setText(orderText.toString());
    }
    
    private void openAddItemsDialog() {
        if (menu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No menu items available!");
            return;
        }
        if (!OrderManager.hasActiveOrders()) {
            JOptionPane.showMessageDialog(this, "No active orders found! Please place an order first.");
            return;
        }
        new AddItemsDialog(this, menu).setVisible(true);
    }
    
    private void openBillingDialog() {
        if (!OrderManager.hasActiveOrders()) {
            JOptionPane.showMessageDialog(this, "No active orders found! Please place an order first.");
            return;
        }
        new BillingDialog(this).setVisible(true);
    }
    
    private void handleAdminAction() {
        if (AdminAuth.isLoggedIn()) {
            // Show admin panel
            String[] options = {"Edit Menu", "Logout", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                "Admin Panel - Choose an action:",
                "Admin Panel",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
            
            if (choice == 0) {
                // Edit Menu
                new MenuEditDialog(this, menu).setVisible(true);
                // Refresh menu after editing
                menu = Database.fetchMenu();
            } else if (choice == 1) {
                // Logout
                AdminAuth.logout();
                JOptionPane.showMessageDialog(this, "Admin logged out successfully!");
                // Refresh the button text
                refreshUI();
            }
        } else {
            // Show login dialog
            AdminLoginDialog loginDialog = new AdminLoginDialog(this);
            loginDialog.setVisible(true);
            
            if (loginDialog.isLoginSuccessful()) {
                refreshUI();
            }
        }
    }
    
    private void refreshUI() {
        // Recreate the UI to update button text
        getContentPane().removeAll();
        initializeComponents();
        revalidate();
        repaint();
    }
    
    private void initializeComponents() {
        // Move the UI initialization code here
        setTitle("Aanchal's Multicuisine Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        JLabel titleLabel = new JLabel("AANCHAL'S MULTICUISINE RESTAURANT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Display Area
        displayArea = new JTextArea(25, 60);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        displayArea.setBackground(Color.WHITE);
        displayArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2), 
            "Display Area", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 12), 
            primaryColor));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBackground(bgColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton[] buttons = {
            createStyledButton("Show Menu", primaryColor),
            createStyledButton("Book Table & Order", new Color(39, 174, 96)),
            createStyledButton("Show Active Orders", new Color(243, 156, 18)),
            createStyledButton("Add Items to Order", new Color(142, 68, 173)),
            createStyledButton("Final Billing", new Color(155, 89, 182)),
            createStyledButton(AdminAuth.isLoggedIn() ? "Admin Panel" : "Admin Login", new Color(231, 76, 60)),
            createStyledButton("Exit", new Color(52, 73, 94))
        };
        
        buttons[0].addActionListener(e -> showMenu());
        buttons[1].addActionListener(e -> openOrderDialog());
        buttons[2].addActionListener(e -> showActiveOrders());
        buttons[3].addActionListener(e -> openAddItemsDialog());
        buttons[4].addActionListener(e -> openBillingDialog());
        buttons[5].addActionListener(e -> handleAdminAction());
        buttons[6].addActionListener(e -> System.exit(0));
        
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize
        initializeApplication();
    }

    public static void main(String[] args) {
        System.out.println("=== Restaurant Management System Starting ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("=============================================");
        
        SwingUtilities.invokeLater(() -> {
            new Rst().setVisible(true);
        });
    }
}