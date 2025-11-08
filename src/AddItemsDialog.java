import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class AddItemsDialog extends JDialog {
    private List<MenuItem> menu;
    private JComboBox<String> orderCombo;
    private JList<String> menuList;
    private DefaultListModel<String> addItemsModel;
    private JList<String> addItemsList;
    private JLabel totalLabel;
    private double additionalTotal = 0.0;
    private Map<String, Double> newItems = new HashMap<>();

    public AddItemsDialog(JFrame parent, List<MenuItem> menu) {
        super(parent, "➕ Add Items to Existing Order", true);
        this.menu = menu;
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    private void initComponents() {
        // Top Panel - Order Selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(142, 68, 173));
        topPanel.add(new JLabel("Select Order:"));
        
        orderCombo = new JComboBox<>();
        for (String table : OrderManager.getActiveOrderTables()) {
            orderCombo.addItem(String.format("%s (₹%.2f)", table, OrderManager.getOrderTotal(table)));
        }
        
        if (!OrderManager.hasActiveOrders()) {
            orderCombo.addItem("No active orders found");
            orderCombo.setEnabled(false);
        }
        
        topPanel.add(orderCombo);
        
        // Menu Panel
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        
        DefaultListModel<String> menuModel = new DefaultListModel<>();
        for (MenuItem item : menu) {
            menuModel.addElement(String.format("[%d] %s - ₹%.2f", 
                item.getItemId(), item.getName(), item.getPrice()));
        }
        
        menuList = new JList<>(menuModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane menuScroll = new JScrollPane(menuList);
        menuPanel.add(menuScroll, BorderLayout.CENTER);
        
        JButton addButton = new JButton("➕ Add Item");
        addButton.addActionListener(this::addItem);
        menuPanel.add(addButton, BorderLayout.SOUTH);
        
        // Add Items Panel
        JPanel addItemsPanel = new JPanel(new BorderLayout());
        addItemsPanel.setBorder(BorderFactory.createTitledBorder("Items to Add"));
        
        addItemsModel = new DefaultListModel<>();
        addItemsList = new JList<>(addItemsModel);
        JScrollPane addItemsScroll = new JScrollPane(addItemsList);
        addItemsPanel.add(addItemsScroll, BorderLayout.CENTER);
        
        JPanel addItemsButtonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("➖ Remove");
        removeButton.addActionListener(this::removeItem);
        addItemsButtonPanel.add(removeButton);
        
        totalLabel = new JLabel("Additional Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        addItemsButtonPanel.add(totalLabel);
        
        addItemsPanel.add(addItemsButtonPanel, BorderLayout.SOUTH);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        JButton confirmButton = new JButton("✅ Add to Order");
        confirmButton.setBackground(new Color(39, 174, 96));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(this::confirmAddItems);
        confirmButton.setEnabled(OrderManager.hasActiveOrders());
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        
        bottomPanel.add(confirmButton);
        bottomPanel.add(cancelButton);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, addItemsPanel);
        splitPane.setDividerLocation(400);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addItem(ActionEvent e) {
        String selectedItem = menuList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select a menu item first!");
            return;
        }
        
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "1");
        if (quantityStr == null || quantityStr.trim().isEmpty()) return;
        
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive!");
                return;
            }
            
            String[] parts = selectedItem.split(" - ₹");
            double price = Double.parseDouble(parts[1]);
            double itemTotal = price * quantity;
            
            String newItem = String.format("%s x%d = ₹%.2f", parts[0], quantity, itemTotal);
            addItemsModel.addElement(newItem);
            newItems.put(newItem, itemTotal);
            
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
        }
    }
    
    private void removeItem(ActionEvent e) {
        String selectedItem = addItemsList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!");
            return;
        }
        
        addItemsModel.removeElement(selectedItem);
        newItems.remove(selectedItem);
        updateTotal();
    }
    
    private void updateTotal() {
        additionalTotal = newItems.values().stream().mapToDouble(Double::doubleValue).sum();
        totalLabel.setText(String.format("Additional Total: ₹%.2f", additionalTotal));
    }
    
    private void confirmAddItems(ActionEvent e) {
        if (!OrderManager.hasActiveOrders()) {
            JOptionPane.showMessageDialog(this, "No active orders available!");
            return;
        }
        
        if (addItemsModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items first!");
            return;
        }
        
        String selectedOrder = (String) orderCombo.getSelectedItem();
        String table = selectedOrder.split(" \\(")[0];
        
        List<String> itemsToAdd = new ArrayList<>();
        for (int i = 0; i < addItemsModel.size(); i++) {
            itemsToAdd.add(addItemsModel.get(i));
        }
        
        OrderManager.addItemsToOrder(table, itemsToAdd, additionalTotal);
        
        JOptionPane.showMessageDialog(this, 
            String.format("Added items to %s\nAdditional Total: ₹%.2f", table, additionalTotal));
        
        dispose();
    }
}