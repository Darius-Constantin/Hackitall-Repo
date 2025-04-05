package com.ctrlaltdefeat.annotations.ui;

import com.ctrlaltdefeat.annotations.dataContainers.Entry;
import com.ctrlaltdefeat.annotations.dataContainers.Footprint;
import com.ctrlaltdefeat.annotations.dataContainers.Question;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionPanel extends JPanel {
    private final JPanel repliesPanel;
    private final JPanel replyInputPanel;
    private final JTextArea replyInputArea;

    public QuestionPanel(Question q) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header
        JLabel questionLabel = new JLabel("<html><b>" + escapeHtml(q.getQuestion().getText())
                + "</b></html>");
        JLabel authorLabel =
                new JLabel("By "
                        + q.getQuestion().getFootprint().getName()
                        + " on "
                        + formatDate(q.getQuestion().getFootprint().getTimestamp()));
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.ITALIC, 10f));
        authorLabel.setForeground(Color.LIGHT_GRAY);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(getBackground());
        header.add(questionLabel);
        header.add(authorLabel);
        header.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Replies panel
        repliesPanel = new JPanel();
        repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
        repliesPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        repliesPanel.setBackground(getBackground());

        // Reply input (hidden initially)
        replyInputPanel = new JPanel(new BorderLayout());
        replyInputPanel.setVisible(false);
        replyInputPanel.setBackground(getBackground());

        replyInputArea = new JTextArea(3, 40);
        replyInputArea.setLineWrap(true);
        replyInputArea.setWrapStyleWord(true);

        JButton submitReply = new JButton("Post");
        JButton cancelReply = new JButton("Cancel");

        JPanel replyButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        replyButtons.setBackground(getBackground());
        replyButtons.add(cancelReply);
        replyButtons.add(submitReply);

        replyInputPanel.add(new JScrollPane(replyInputArea), BorderLayout.CENTER);
        replyInputPanel.add(replyButtons, BorderLayout.SOUTH);

        // Reply button
        JButton replyButton = new JButton("Reply");
        replyButton.addActionListener(e -> {
            replyInputPanel.setVisible(!replyInputPanel.isVisible());
            replyInputArea.requestFocusInWindow();
        });

        submitReply.addActionListener(e -> {
            String text = replyInputArea.getText().trim();
            if (!text.isEmpty()) {
                // TODO: Replace "You" with actual user name
                addReply(new Entry(new Footprint("You", new Date()), text));
                replyInputArea.setText("");
                replyInputPanel.setVisible(false);
            }
        });

        cancelReply.addActionListener(e -> {
            replyInputArea.setText("");
            replyInputPanel.setVisible(false);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(getBackground());
        bottom.add(replyButton);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(getBackground());
        center.add(repliesPanel);
        center.add(replyInputPanel);
        center.add(bottom);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    public void addReply(Entry e) {
        JLabel replyLabel = new JLabel("<html>" + escapeHtml(e.getText()) + "</html>");
        JLabel meta = new JLabel("↳ " + e.getFootprint().getName() + ", "
                + formatDate(e.getFootprint().getTimestamp()));
        meta.setFont(meta.getFont().deriveFont(Font.PLAIN, 10f));
        meta.setForeground(Color.GRAY);

        JPanel replyBlock = new JPanel();
        replyBlock.setLayout(new BoxLayout(replyBlock, BoxLayout.Y_AXIS));
        replyBlock.setBackground(getBackground());
        replyBlock.add(meta);
        replyBlock.add(replyLabel);
        replyBlock.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 4));

        repliesPanel.add(replyBlock);
        revalidate();
        repaint();
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("MMM dd, yyyy HH:mm").format(date);
    }

    private String escapeHtml(String text) {
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }
}