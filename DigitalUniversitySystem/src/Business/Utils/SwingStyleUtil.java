package Business.Utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class SwingStyleUtil {

    // Custom renderer for table cells (alternating row colors and center alignment)
    public static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(220, 230, 240) : Color.WHITE); // Alternating row colors
            }
            setHorizontalAlignment(SwingConstants.CENTER); // Center align text in cells
            return c;
        }
    }

    // Custom renderer for table headers (SteelBlue background, white foreground, bold font, center alignment)
    public static class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
        public CustomTableHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setForeground(Color.WHITE);
            setBackground(new Color(70, 130, 180)); // SteelBlue
            setFont(new Font("Dialog", Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(Color.WHITE);
            c.setBackground(new Color(70, 130, 180)); // SteelBlue
            return c;
        }
    }

    // Method to style a JTable
    public static void styleTable(JTable table) {
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.getTableHeader().setDefaultRenderer(new CustomTableHeaderRenderer());
        table.setRowHeight(25); // Increase row height for better aesthetics
        table.getTableHeader().setOpaque(true);
    }

    // Method to style a JButton
    public static void styleButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Dialog", Font.BOLD, 12));
        button.setFocusPainted(false); // Remove focus border
    }

    // Method to center align a JLabel
    public static void centerLabel(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // Method to style a JTextField
    public static void styleTextField(javax.swing.JTextField textField) {
        textField.setFont(new Font("Dialog", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 1), // LightBlue border
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Padding
        textField.setForeground(new Color(50, 50, 50)); // Dark grey text
    }

    // Method to style a JLabel (general labels, not titles)
    public static void styleLabel(javax.swing.JLabel label) {
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        label.setForeground(new Color(70, 70, 70)); // Darker grey for labels
    }
}