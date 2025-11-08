import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BillingDialog extends JDialog {
    private JComboBox<String> orderCombo;
    private JTextArea billArea;
    private JLabel totalLabel;
    private JLabel taxLabel;
    private JLabel grandTotalLabel;

    public BillingDialog(JFrame parent) {
        super(parent, "💰 Final Billing", true);
        
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    private void initComponents() {
        // Top Panel - Order Selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(155, 89, 182));
        topPanel.add(new JLabel("Select Order to Close:"));
        
        orderCombo = new JComboBox<>();
        for (String table : OrderManager.getActiveOrderTables()) {
            orderCombo.addItem(String.format("%s (₹%.2f)", table, OrderManager.getOrderTotal(table)));
        }
        
        if (!OrderManager.hasActiveOrders()) {
            orderCombo.addItem("No active orders found");
            orderCombo.setEnabled(false);
        }
        
        orderCombo.addActionListener(this::orderSelected);
        topPanel.add(orderCombo);
        
        // Bill Preview Panel
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Bill Preview"));
        
        billArea = new JTextArea(20, 40);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billArea.setEditable(false);
        billArea.setBackground(Color.WHITE);
        JScrollPane previewScroll = new JScrollPane(billArea);
        previewPanel.add(previewScroll, BorderLayout.CENTER);
        
        // Totals Panel
        JPanel totalsPanel = new JPanel(new GridLayout(3, 1));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        
        totalLabel = new JLabel("Subtotal: ₹0.00");
        taxLabel = new JLabel("Tax (5%): ₹0.00");
        grandTotalLabel = new JLabel("Grand Total: ₹0.00");
        
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        taxLabel.setFont(new Font("Arial", Font.BOLD, 14));
        grandTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        grandTotalLabel.setForeground(new Color(231, 76, 60));
        
        totalsPanel.add(totalLabel);
        totalsPanel.add(taxLabel);
        totalsPanel.add(grandTotalLabel);
        
        previewPanel.add(totalsPanel, BorderLayout.SOUTH);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        JButton printButton = new JButton("🖨️ Print Bill");
        printButton.setBackground(new Color(52, 152, 219));
        printButton.setForeground(Color.WHITE);
        printButton.addActionListener(this::printBill);
        printButton.setEnabled(OrderManager.hasActiveOrders());
        
        JButton closeOrderButton = new JButton("✅ Close Order & Pay");
        closeOrderButton.setBackground(new Color(39, 174, 96));
        closeOrderButton.setForeground(Color.WHITE);
        closeOrderButton.addActionListener(this::closeOrder);
        closeOrderButton.setEnabled(OrderManager.hasActiveOrders());
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        
        bottomPanel.add(printButton);
        bottomPanel.add(closeOrderButton);
        bottomPanel.add(cancelButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(previewPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load first order if available
        if (OrderManager.hasActiveOrders()) {
            orderCombo.setSelectedIndex(0);
            orderSelected(null);
        }
    }
    
    private void orderSelected(ActionEvent e) {
        if (!OrderManager.hasActiveOrders()) return;
        
        String selectedOrder = (String) orderCombo.getSelectedItem();
        String table = selectedOrder.split(" \\(")[0];
        
        List<String> items = OrderManager.getOrderItems(table);
        double subtotal = OrderManager.getOrderTotal(table);
        double tax = subtotal * 0.05; // 5% tax
        double grandTotal = subtotal + tax;
        
        // Update totals
        totalLabel.setText(String.format("Subtotal: ₹%.2f", subtotal));
        taxLabel.setText(String.format("Tax (5%%): ₹%.2f", tax));
        grandTotalLabel.setText(String.format("Grand Total: ₹%.2f", grandTotal));
        
        // Generate bill preview
        generateBillPreview(table, items, subtotal, tax, grandTotal);
    }
    
    private void generateBillPreview(String table, List<String> items, double subtotal, double tax, double grandTotal) {
        StringBuilder bill = new StringBuilder();
        
        bill.append("═══════════════════════════════════════════\n");
        bill.append("           RESTAURANT BILL\n");
        bill.append("═══════════════════════════════════════════\n");
        bill.append(String.format("Table: %s\n", table));
        bill.append(String.format("Date: %s\n", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));
        bill.append("───────────────────────────────────────────\n");
        bill.append("Items Ordered:\n");
        bill.append("───────────────────────────────────────────\n");
        
        for (String item : items) {
            bill.append("• ").append(item).append("\n");
        }
        
        bill.append("───────────────────────────────────────────\n");
        bill.append(String.format("Subtotal:                     ₹%.2f\n", subtotal));
        bill.append(String.format("Tax (5%%):                     ₹%.2f\n", tax));
        bill.append("═══════════════════════════════════════════\n");
        bill.append(String.format("GRAND TOTAL:                  ₹%.2f\n", grandTotal));
        bill.append("═══════════════════════════════════════════\n");
        bill.append("\n        Thank you for dining with us!\n");
        bill.append("           Please visit again!\n");
        bill.append("═══════════════════════════════════════════\n");
        
        billArea.setText(bill.toString());
    }
    
    private void printBill(ActionEvent e) {
        try {
            billArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage());
        }
    }
    
    private void closeOrder(ActionEvent e) {
        if (!OrderManager.hasActiveOrders()) {
            JOptionPane.showMessageDialog(this, "No active orders to close!");
            return;
        }
        
        String selectedOrder = (String) orderCombo.getSelectedItem();
        String table = selectedOrder.split(" \\(")[0];
        double grandTotal = OrderManager.getOrderTotal(table) * 1.05; // Including tax
        
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Close order for %s?\nTotal Amount: ₹%.2f", table, grandTotal),
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            OrderManager.closeOrder(table);
            
            JOptionPane.showMessageDialog(this,
                String.format("Order for %s closed successfully!\nPayment received: ₹%.2f\nTable is now available.", 
                    table, grandTotal));
            
            dispose();
        }
    }
}