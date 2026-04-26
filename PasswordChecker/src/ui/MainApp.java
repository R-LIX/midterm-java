package ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainApp {
    
    // ── Modern color palette ──────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0x2563EB);  // Vibrant Blue
    public static final Color PRIMARY_DARK  = new Color(0x1E40AF);  // Darker Blue
    public static final Color ACCENT        = new Color(0x10B981);  // Emerald Green
    public static final Color BG_PRIMARY    = Color.WHITE;
    public static final Color TEXT_PRIMARY  = new Color(0x1F2937);  // Dark Grey
    public static final Color TEXT_SECONDARY= new Color(0x6B7280);  // Medium Grey
    public static final Color BORDER        = new Color(0xE5E7EB);  // Light Grey
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatLightLaf()); } catch (Exception ignored) {}
        
        // ── Component styling ─────────────────────────────────────────
        UIManager.put("Button.arc", 999);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("defaultFont", new Font("SF Pro Display", Font.PLAIN, 14));

        // ── Button styling ────────────────────────────────────────────
        UIManager.put("Button.shadow", null);
        UIManager.put("Button.shadowColor", null);
        UIManager.put("Button.borderColor", null);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focusedBorderColor", PRIMARY_DARK);
        UIManager.put("Button.hoverBackground", PRIMARY_DARK);
        UIManager.put("Button.pressedBackground", new Color(0x1E40AF));
        
        // ── Text field styling ────────────────────────────────────────
        UIManager.put("TextField.placeholderForeground", new Color(0xD1D5DB));
        UIManager.put("PasswordField.placeholderForeground", new Color(0xD1D5DB));
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        UIManager.put("PasswordField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Password Checker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(560, 760);
            frame.setMinimumSize(new Dimension(420, 620));
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);
            frame.getContentPane().setBackground(BG_PRIMARY);
            frame.add(new AuthPanel(frame));

            // soft max-size enforcement (Swing doesn't honour setMaximumSize on JFrame)
            frame.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent e) {
                    Dimension d = frame.getSize();
                    int w = Math.min(d.width,  800);
                    int h = Math.min(d.height, 1000);
                    if (w != d.width || h != d.height) frame.setSize(w, h);
                }
            });

            frame.setVisible(true);
        });
    }
}