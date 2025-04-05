package com.ctrlaltdefeat.annotations.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import genericUI.BrightnessSlider;
import genericUI.HueSlider;
import openAI.MyOpenAIClient;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class AnnotationInputDialog extends DialogWrapper {
    private JTextPane noteArea;
    private HueSlider hueSlider;
    private BrightnessSlider brightnessSlider;
    private final String code;

    public String getNoteText() {
        return noteArea.getText();
    }

    public Color getColor() {
        return Color.getHSBColor(hueSlider.getValue() / 360.f, 1,
                brightnessSlider.getValue() / 100.f);
    }

    public AnnotationInputDialog(String code) {
        super(true);
        init();
        setTitle("Add Annotation");
        this.code = code;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300, 300));

        // Note area
        JScrollPane noteAreaContainer = new JBScrollPane();
        noteArea = new JTextPane();
        noteAreaContainer.setViewportView(noteArea);
        noteAreaContainer.setBorder(new CompoundBorder(new LineBorder(UIUtil.getLabelForeground(), 1),
                new EmptyBorder(5, 5, 5, 5)));
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_JUSTIFIED);
        noteArea.setParagraphAttributes(attributeSet, false);
        noteArea.setFont(UIUtil.getLabelFont());
        noteAreaContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        SwingUtilities.invokeLater(() -> noteArea.requestFocusInWindow());

        // Color picker
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Highlight Color:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        hueSlider = new HueSlider();
        hueSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        brightnessSlider = new BrightnessSlider(hueSlider.getValue(), 0);
        brightnessSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPanel.add(label);
        colorPanel.add(Box.createVerticalStrut(5));
        colorPanel.add(hueSlider);
        colorPanel.add(Box.createVerticalStrut(5));
        colorPanel.add(brightnessSlider);
        colorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(new JLabel("Enter annotation or "));
        JButton generateWithAI = new JButton("generate with AI");
        generateWithAI.addActionListener(e -> {
            MyOpenAIClient client = new MyOpenAIClient();
            noteArea.setText(client.analyzeCode(code));
            noteArea.revalidate();
            noteArea.repaint();
            noteAreaContainer.repaint();
            noteAreaContainer.revalidate();
        });
        header.add(generateWithAI);
        header.add(new JLabel(":"));

        panel.add(header);
        panel.add(Box.createVerticalStrut(5));
        panel.add(noteAreaContainer);
        panel.add(Box.createVerticalStrut(5));
        panel.add(colorPanel);
        return panel;
    }
}
