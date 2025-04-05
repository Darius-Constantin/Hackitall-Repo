package genericUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;

class GradientSliderUI extends BasicSliderUI {
    private final boolean isHueSlider;

    public GradientSliderUI(JSlider slider, boolean isHueSlider) {
        super(slider);
        this.isHueSlider = isHueSlider;
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle trackBounds = trackRect;

        GradientPaint gradient;

        if (isHueSlider) {
            BufferedImage hueGradient = new BufferedImage(trackBounds.width, 1, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < trackBounds.width; x++) {
                float hue = (float) x / trackBounds.width;
                Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
                hueGradient.setRGB(x, 0, color.getRGB());
            }
            g2d.drawImage(hueGradient, trackBounds.x, trackBounds.y + trackBounds.height / 2 - 4, trackBounds.width, 8, null);
        } else {
            float hue = ((BrightnessSlider) slider).getHue();
            float sat = ((BrightnessSlider) slider).getSaturation();
            GradientPaint brightnessGradient = new GradientPaint(
                    trackBounds.x, 0, Color.BLACK,
                    trackBounds.x + trackBounds.width, 0, Color.getHSBColor(hue, sat, 1f)
            );
            g2d.setPaint(brightnessGradient);
            g2d.fillRect(trackBounds.x, trackBounds.y + trackBounds.height / 2 - 4, trackBounds.width, 8);
        }

        g2d.dispose();
    }
}