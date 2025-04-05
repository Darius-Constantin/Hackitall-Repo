package com.ctrlaltdefeat.annotations.ui;

import com.ctrlaltdefeat.annotations.dataContainers.Suggestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

public class SuggestionPanel extends JPanel {
    private final JLabel nameLabel;
    private final JLabel dateLabel;
    private final JLabel suggestionLabel;
    private final JButton likeButton;
    private final JLabel likesLabel;
    private int likes = 0;

    public SuggestionPanel(Suggestion s) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // Format the date nicely.
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");

        // Create the labels and button.
        nameLabel = new JLabel("Suggestion by: " + s.getSuggestion().getFootprint().getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 12f));
        dateLabel = new JLabel(sdf.format(s.getSuggestion().getFootprint().getTimestamp()));
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.ITALIC, 10f));
        suggestionLabel = new JLabel("<html>" + s.getSuggestion().getText() + "</html>");
        suggestionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        likeButton = new JButton("Upvote");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.addActionListener(this::handleLike);

        likesLabel = new JLabel("Upvotes: " + likes);
        likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add some spacing and components.
        add(nameLabel);
        add(Box.createVerticalStrut(2));
        add(dateLabel);
        add(Box.createVerticalStrut(5));
        add(suggestionLabel);
        add(Box.createVerticalStrut(10));

        // Create a panel for the like button and like count.
        JPanel likePanel = new JPanel();
        likePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        likePanel.setBackground(getBackground());
        likePanel.add(likeButton);
        likePanel.add(likesLabel);
        likePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(likePanel);
    }

    private void handleLike(ActionEvent e) {
        likes++;
        likesLabel.setText("Upvotes: " + likes);
    }
}