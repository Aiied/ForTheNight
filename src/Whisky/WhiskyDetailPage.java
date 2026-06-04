package Whisky;

import Ui.Button.BackButton;
import Ui.Button.FavoriteStarButton;
import Ui.BackgroundPanel;
import Ui.ImageScaler;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

public class WhiskyDetailPage extends JFrame {
    private static final int DETAIL_IMAGE_WIDTH = 280;
    private static final int DETAIL_IMAGE_HEIGHT = 340;

    private final Whisky whisky;
    private final JFrame previousPage;

    public WhiskyDetailPage(Whisky whisky, JFrame previousPage) {
        this.whisky = whisky;
        this.previousPage = previousPage;

        setTitle(whisky.getName());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new BackgroundPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JButton backButton = new BackButton();
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            previousPage.setVisible(true);
            dispose();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel imageLabel = createImageLabel(whisky);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel nameRow = new JPanel(new BorderLayout(10, 0));
        nameRow.setOpaque(false);
        nameRow.setMaximumSize(new Dimension(520, 44));

        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(java.awt.Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        FavoriteStarButton starButton = new FavoriteStarButton(whisky.getName(), 34);
        nameRow.add(nameLabel, BorderLayout.CENTER);
        nameRow.add(starButton, BorderLayout.EAST);
        nameRow.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(topPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(nameRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(createInfoPanel());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane);

        setVisible(true);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new java.awt.Color(24, 24, 24));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(720, 320));

        infoPanel.add(createInfoRow("Country", whisky.getCountry()));
        infoPanel.add(createInfoRow("Type", whisky.getWhiskyType()));
        infoPanel.add(createInfoRow("Distillery", whisky.getDistillery()));
        infoPanel.add(createInfoRow("Cask", whisky.getCask()));
        infoPanel.add(createInfoRow("Abv", formatAbv()));
        infoPanel.add(createInfoRow("Aroma", joinNotes(whisky.getAroma())));
        infoPanel.add(createInfoRow("Taste", joinNotes(whisky.getTaste())));
        infoPanel.add(createInfoRow("Finish", joinNotes(whisky.getFinish())));

        return infoPanel;
    }

    private JPanel createInfoRow(String title, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(72, 26));
        titleLabel.setForeground(new java.awt.Color(170, 170, 170));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel valueLabel = new JLabel(isBlank(value) ? "-" : value);
        valueLabel.setForeground(java.awt.Color.WHITE);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        rowPanel.add(titleLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.CENTER);

        return rowPanel;
    }

    private JLabel createImageLabel(Whisky whisky) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(DETAIL_IMAGE_WIDTH, DETAIL_IMAGE_HEIGHT));
        imageLabel.setMaximumSize(new Dimension(DETAIL_IMAGE_WIDTH, DETAIL_IMAGE_HEIGHT));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new java.awt.Color(45, 45, 45));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        if (whisky.getImagePath() == null || whisky.getImagePath().isBlank()) {
            return imageLabel;
        }

        String resolvedPath = whisky.getImagePath();
        File imageFile = new File(resolvedPath);
        if (!imageFile.exists()) {
            return imageLabel;
        }

        ImageIcon icon = ImageScaler.loadScaledIcon(resolvedPath, DETAIL_IMAGE_WIDTH, DETAIL_IMAGE_HEIGHT);
        if (icon != null) {
            imageLabel.setIcon(icon);
        }

        return imageLabel;
    }
    private String formatAbv() {
        if (whisky.getAbv() == 0.0f) {
            return "-";
        }

        return whisky.getAbv() + "%";
    }

    private String joinNotes(ArrayList<String> notes) {
        ArrayList<String> filteredNotes = new ArrayList<>();

        for (String note : notes) {
            if (!isBlank(note)) {
                filteredNotes.add(note);
            }
        }

        return String.join(", ", filteredNotes);
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}

