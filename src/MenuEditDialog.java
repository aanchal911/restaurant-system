import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;

public class MenuEditDialog extends JDialog {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private List<MenuItem> menuItems;

    public MenuEditDialog(JFrame parent, List<MenuItem> menu) {
        super(parent, "Edit Menu - Admin Panel", true);
        this.menuItems = menu;
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents();
        loadMenuData();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        JLabel headerLabel = new JLabel("ADMIN - MENU EDITOR");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"ID", "Category", "Item Name", "Price", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3 || column == 4; // Only name, price, description editable
            }
        };
        
        menuTable = new JTable(tableModel);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 12));
        menuTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        menuTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        menuTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(menuTable);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(39, 174, 96));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this::saveChanges);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadMenuData());
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadMenuData() {
        tableModel.setRowCount(0);
        for (MenuItem item : menuItems) {
            tableModel.addRow(new Object[]{
                item.getItemId(),
                item.getCategoryName(),
                item.getName(),
                String.format("%.2f", item.getPrice()),
                item.getDescription()
            });
        }
    }
    
    private void saveChanges(ActionEvent e) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE menu SET name = ?, price = ?, description = ? WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            
            int updatedCount = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int itemId = (Integer) tableModel.getValueAt(i, 0);
                String name = (String) tableModel.getValueAt(i, 2);
                String priceStr = (String) tableModel.getValueAt(i, 3);
                String description = (String) tableModel.getValueAt(i, 4);
                
                try {
                    double price = Double.parseDouble(priceStr);
                    
                    stmt.setString(1, name);
                    stmt.setDouble(2, price);
                    stmt.setString(3, description);
                    stmt.setInt(4, itemId);
                    
                    stmt.addBatch();
                    updatedCount++;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid price format for item ID " + itemId + ". Please use decimal format (e.g., 120.50)");
                    return;
                }
            }
            
            stmt.executeBatch();
            JOptionPane.showMessageDialog(this, 
                "Successfully updated " + updatedCount + " menu items!");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving changes: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}