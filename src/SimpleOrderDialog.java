import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SimpleOrderDialog extends JDialog {
    private List<MenuItem> menu;
    private JComboBox<String> tableCombo;
    private JList<String> menuList;
    private DefaultListModel<String> orderModel;
    private JList<String> orderList;
    private JLabel totalLabel;
    private double totalAmount = 0.0;
    private Map<String, Double> orderItems = new HashMap<>();

    public SimpleOrderDialog(JFrame parent, List<MenuItem> menu) {
        super(parent, "🪑 Book Table & Place Order", true);
        this.menu = menu;
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    private void initComponents() {
        // Top Panel - Table Selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(52, 152, 219));
        topPanel.add(new JLabel("Select Table:"));
        
        tableCombo = new JComboBox<>();
        for (int i = 1; i <= 5; i++) {
            tableCombo.addItem("Table " + i);
        }
        topPanel.add(tableCombo);
        
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
        
        JButton addButton = new JButton("➕ Add to Order");
        addButton.addActionListener(this::addToOrder);
        menuPanel.add(addButton, BorderLayout.SOUTH);
        
        // Order Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Current Order"));
        
        orderModel = new DefaultListModel<>();
        orderList = new JList<>(orderModel);
        JScrollPane orderScroll = new JScrollPane(orderList);
        orderPanel.add(orderScroll, BorderLayout.CENTER);
        
        JPanel orderButtonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("➖ Remove Item");
        removeButton.addActionListener(this::removeFromOrder);
        orderButtonPanel.add(removeButton);
        
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        orderButtonPanel.add(totalLabel);
        
        orderPanel.add(orderButtonPanel, BorderLayout.SOUTH);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton placeOrderButton = new JButton("✅ Place Order");
        placeOrderButton.setBackground(new Color(39, 174, 96));
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.addActionListener(this::placeOrder);
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        
        bottomPanel.add(placeOrderButton);
        bottomPanel.add(cancelButton);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, orderPanel);
        splitPane.setDividerLocation(400);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addToOrder(ActionEvent e) {
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
            
            // Extract price from selected item
            String[] parts = selectedItem.split(" - ₹");
            double price = Double.parseDouble(parts[1]);
            double itemTotal = price * quantity;
            
            String orderItem = String.format("%s x%d = ₹%.2f", parts[0], quantity, itemTotal);
            orderModel.addElement(orderItem);
            orderItems.put(orderItem, itemTotal);
            
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
        }
    }
    
    private void removeFromOrder(ActionEvent e) {
        String selectedItem = orderList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an order item to remove!");
            return;
        }
        
        orderModel.removeElement(selectedItem);
        orderItems.remove(selectedItem);
        updateTotal();
    }
    
    private void updateTotal() {
        totalAmount = orderItems.values().stream().mapToDouble(Double::doubleValue).sum();
        totalLabel.setText(String.format("Total: ₹%.2f", totalAmount));
    }
    
    private void placeOrder(ActionEvent e) {
        if (orderModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items to the order!");
            return;
        }
        
        String table = (String) tableCombo.getSelectedItem();
        
        // Save order to OrderManager
        List<String> orderItemsList = new ArrayList<>();
        for (int i = 0; i < orderModel.size(); i++) {
            orderItemsList.add(orderModel.get(i));
        }
        OrderManager.addOrder(table, orderItemsList, totalAmount);
        
        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("Order placed successfully!\n\n");
        orderSummary.append("Table: ").append(table).append("\n");
        orderSummary.append("Items:\n");
        
        for (String item : orderItemsList) {
            orderSummary.append("• ").append(item).append("\n");
        }
        
        orderSummary.append(String.format("\nTotal: ₹%.2f", totalAmount));
        orderSummary.append("\n\nYou can now add more items or generate bill!");
        
        JOptionPane.showMessageDialog(this, orderSummary.toString());
        dispose();
    }
}