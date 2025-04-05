package genericUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CollapsiblePanel extends JPanel {
    private final JButton toggleButton;
    private final JPanel contentPanel;
    private boolean expanded;

    public CollapsiblePanel(String title) {
        toggleButton = new JButton("▼\t" + title);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        toggleButton.addActionListener(this::toggleVisibility);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        setLayout(new BorderLayout());
        add(toggleButton, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        setExpanded(false);
    }

    public void addContent(JComponent content) {
        contentPanel.add(content, BorderLayout.CENTER);
    }

    public void addContent(JComponent content, int index) {
        contentPanel.add(content, BorderLayout.CENTER, index);
    }

    private void toggleVisibility(ActionEvent e) {
        expanded = !expanded;
        setExpanded(expanded);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expand) {
        expanded = expand;
        contentPanel.setVisible(expand);
        toggleButton.setText((expand ? "▼ " : "▶ ") + toggleButton.getText().substring(2));
        revalidate();
        repaint();
    }
}
