package com.ctrlaltdefeat.annotations.ui;

import com.ctrlaltdefeat.annotations.dataContainers.*;
import com.ctrlaltdefeat.annotations.AnnotationStorage;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.ui.UIUtil;
import genericUI.CollapsiblePanel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class AnnotationToolWindowFactory implements ToolWindowFactory, DumbAware {
    private AnnotationToolWindowContent content = null;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        content = new AnnotationToolWindowContent(toolWindow);
        AnnotationStorage.setWindowContent(content);
        toolWindow.getContentManager().addContent(ContentFactory.getInstance()
                .createContent(content.getContentPanel(), "", false));
    }

    public static class AnnotationToolWindowContent {
        @Getter
        private final JPanel contentPanel = new JPanel();
        private CollapsiblePanel notePanel;

        public AnnotationToolWindowContent(ToolWindow toolWindow) {
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(createAnnotationPanel(AnnotationStorage.getCurrentAnnotation()),
                    BorderLayout.PAGE_START);
        }

        @NotNull
        private JPanel createAnnotationPanel(@Nullable AnnotationData annotationData) {
            JPanel panel = new JPanel();

            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            if (annotationData == null) {
                return panel;
            }

            notePanel = new CollapsiblePanel("Note");
            JTextPane textArea = new JTextPane();
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_JUSTIFIED);
            textArea.setParagraphAttributes(attributeSet, false);
            textArea.setFont(UIUtil.getLabelFont());
            panel.add(notePanel);
            notePanel.addContent(textArea);

            textArea.setBorder(new CompoundBorder(new LineBorder(UIUtil.getLabelForeground(), 1),
                    new EmptyBorder(5, 5, 5, 5)));
            textArea.setEditable(true);
            textArea.setText(annotationData.getAnnotation().getNote());
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update(); // For styled docs; not usually called in plain JTextArea
                }

                private void update() {
                    annotationData.getAnnotation().setNote(textArea.getText());
                }
            });

            return panel;
        }

        public void update() {
            contentPanel.removeAll();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(createAnnotationPanel(AnnotationStorage.getCurrentAnnotation()),
                    BorderLayout.PAGE_START);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }
}
