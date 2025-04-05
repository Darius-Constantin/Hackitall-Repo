package genericUI;

import javax.swing.*;
import java.awt.*;

public class HueSlider extends JSlider {
    public HueSlider() {
        super(0, 360, 0);
        setPaintTrack(true);
        setPaintTicks(false);
        setPaintLabels(false);
        setPreferredSize(new Dimension(300, 30));
        setUI(new GradientSliderUI(this, true));
    }
}
