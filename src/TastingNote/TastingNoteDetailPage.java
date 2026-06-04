package TastingNote;

import Ui.BackgroundPanel;
import Ui.Button.BackButton;
import Ui.StarIconFactory;

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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

public class TastingNoteDetailPage extends JFrame {
    private static final int STAR_COUNT = 5;

    private final TastingNote note;
    private final JFrame previousPage;
    private final ImageIcon scoreOnIcon = StarIconFactory.createStarIcon(22, 1.0f);
    private final ImageIcon scoreOffIcon = StarIconFactory.createStarIcon(22, 1.0f, Color.WHITE);

    public TastingNoteDetailPage(TastingNote note, JFrame previousPage) {
        this.note = note;
        this.previousPage = previousPage;

        setTitle(note.getWhiskyName());
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

        JLabel nameLabel = new JLabel(note.getWhiskyName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel(note.getDate());
        dateLabel.setForeground(new Color(170, 170, 170));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel scorePanel = createScorePanel(note.getScore());
        scorePanel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(topPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        contentPanel.add(dateLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(scorePanel);
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
        infoPanel.setBackground(new Color(24, 24, 24));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(760, 460));

        infoPanel.add(createInfoRow("Aroma", joinNotes(note.getAroma())));
        infoPanel.add(createInfoRow("Taste", joinNotes(note.getTaste())));
        infoPanel.add(createInfoRow("Finish", joinNotes(note.getFinish())));
        infoPanel.add(createInfoRow("Review", normalizeText(note.getDetailReview())));

        return infoPanel;
    }

    private JPanel createInfoRow(String title, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(82, 26));
        titleLabel.setForeground(new Color(170, 170, 170));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel valueLabel = new JLabel(isBlank(value) ? "-" : value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        rowPanel.add(titleLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.CENTER);
        return rowPanel;
    }

    private JPanel createScorePanel(int score) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        int safeScore = Math.max(0, Math.min(score, STAR_COUNT));
        for (int i = 0; i < STAR_COUNT; i++) {
            JLabel star = new JLabel();
            boolean filled = i < safeScore;

            if (scoreOnIcon != null && scoreOffIcon != null) {
                star.setIcon(filled ? scoreOnIcon : scoreOffIcon);
            } else {
                star.setText(filled ? "\u2605" : "\u2606");
                star.setFont(new Font("SansSerif", Font.BOLD, 20));
                star.setForeground(filled ? new Color(242, 196, 74) : Color.WHITE);
            }

            panel.add(star);
            if (i < STAR_COUNT - 1) {
                panel.add(Box.createRigidArea(new Dimension(3, 0)));
            }
        }
        return panel;
    }

    private String joinNotes(ArrayList<String> notes) {
        ArrayList<String> filteredNotes = new ArrayList<>();
        for (String noteText : notes) {
            if (!isBlank(noteText)) {
                filteredNotes.add(noteText);
            }
        }
        return String.join(", ", filteredNotes);
    }

    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
