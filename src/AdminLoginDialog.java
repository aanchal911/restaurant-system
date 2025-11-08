import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;

    public AdminLoginDialog(JFrame parent) {
        super(parent, "Admin Login", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("ADMIN LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        mainPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1; gbc.insets = new Insets(5, 10, 5, 5);
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 10);
        mainPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 5);
        mainPanel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 10);
        mainPanel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(39, 174, 96));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this::attemptLogin);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Enter key support
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void attemptLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (AdminAuth.login(username, password)) {
            loginSuccessful = true;
            JOptionPane.showMessageDialog(this, "Login successful! Admin features unlocked.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials! Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}