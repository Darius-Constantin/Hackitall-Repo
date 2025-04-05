package genericUI;

import javax.swing.*;
import java.awt.*;

public class BrightnessSlider extends JSlider {
    private float hue;
    private float saturation;

    public BrightnessSlider(float hue, float saturation) {
        super(0, 100, 100); // Brightness from 0% to 100%
        this.hue = hue;
        this.saturation = saturation;
        setPaintTrack(true);
        setPaintTicks(false);
        setPaintLabels(false);
        setPreferredSize(new Dimension(300, 30));
        setUI(new GradientSliderUI(this, false));
    }

    public void setHueSaturation(float hue, float saturation) {
        this.hue = hue;
        this.saturation = saturation;
        repaint();
    }

    public float getHue() { return hue; }
    public float getSaturation() { return saturation; }
}
