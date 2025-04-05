package genericUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class RGBColorPicker extends JPanel {
    private final JSlider redSlider;
    private final JSlider greenSlider;
    private final JSlider blueSlider;
    private final JPanel previewPanel;

    public RGBColorPicker() {
        setLayout(new BorderLayout(10, 10));

        // Preview
        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(60, 60));
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Sliders
        redSlider = createColorSlider("Red");
        greenSlider = createColorSlider("Green");
        blueSlider = createColorSlider("Blue");

        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));
        slidersPanel.add(redSlider);
        slidersPanel.add(greenSlider);
        slidersPanel.add(blueSlider);

        add(slidersPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);

        updatePreview();
    }

    private JSlider createColorSlider(String label) {
        JLabel sliderLabel = new JLabel(label);
        JSlider slider = new JSlider(0, 255, 255);

        slider.setMajorTickSpacing(64);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(this::updatePreview);

        JPanel container = new JPanel(new BorderLayout());
        container.add(sliderLabel, BorderLayout.NORTH);
        container.add(slider, BorderLayout.CENTER);
        add(container);

        return slider;
    }

    private void updatePreview(ChangeEvent e) {
        updatePreview();
    }

    private void updatePreview() {
        previewPanel.setBackground(getSelectedColor());
    }

    public Color getSelectedColor() {
        return new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
    }
}