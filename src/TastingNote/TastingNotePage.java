package TastingNote;

import Ui.Button.BackButton;
import Ui.BackgroundPanel;
import Ui.StarIconFactory;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TastingNotePage extends JFrame {
    private static final String NOTE_FILE_PATH = "src/assets/Flie(txt)/tastingNotes.txt";
    private static final int STAR_COUNT = 5;

    private final JFrame previousPage;
    private final JPanel listPanel = new BackgroundPanel();
    private final ImageIcon scoreOnIcon = StarIconFactory.createStarIcon(18, 1.0f);
    private final ImageIcon scoreOffIcon = StarIconFactory.createStarIcon(18, 1.0f, Color.WHITE);

    public TastingNotePage() {
        this(null);
    }

    public TastingNotePage(JFrame previousPage) {
        this.previousPage = previousPage;
        setTitle("TastingNote");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel(new BorderLayout()));

        JButton backButton = new BackButton();
        backButton.addActionListener(e -> {
            if (this.previousPage != null) {
                this.previousPage.setVisible(true);
            }
            dispose();
        });

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Tasting Notes");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 12, 0));

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));

        refreshList();

        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setOpaque(false);
        listScrollPane.getViewport().setOpaque(false);
        listScrollPane.setBorder(null);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(titleLabel);
        centerPanel.add(listScrollPane);

        JButton newNoteButton = new JButton("New Note");
        newNoteButton.setFocusPainted(false);
        newNoteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newNoteButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        newNoteButton.setPreferredSize(new Dimension(180, 44));
        newNoteButton.addActionListener(e -> {
            new NewNotePage(this);
            setVisible(false);
        });

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 32, 0));
        bottomPanel.add(newNoteButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshList() {
        listPanel.removeAll();

        List<TastingNote> notes = new TastingNoteList(NOTE_FILE_PATH).getTastingNotes();
        if (notes.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasting notes yet.");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            listPanel.add(emptyLabel);
            listPanel.revalidate();
            listPanel.repaint();
            return;
        }

        for (TastingNote note : notes) {
            listPanel.add(createNoteItem(note));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createNoteItem(TastingNote note) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 8));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        itemPanel.setPreferredSize(new Dimension(620, 108));
        itemPanel.setBackground(new Color(24, 24, 24));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel nameLabel = new JLabel(note.getWhiskyName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 17));

        JLabel dateLabel = new JLabel(note.getDate());
        dateLabel.setForeground(new Color(170, 170, 170));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(dateLabel);

        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(createScorePanel(note.getScore()), BorderLayout.EAST);
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TastingNoteDetailPage(note, TastingNotePage.this);
                setVisible(false);
            }
        });

        return itemPanel;
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
                star.setFont(new Font("SansSerif", Font.BOLD, 16));
                star.setForeground(filled ? new Color(242, 196, 74) : Color.WHITE);
            }

            panel.add(star);
            if (i < STAR_COUNT - 1) {
                panel.add(Box.createRigidArea(new Dimension(2, 0)));
            }
        }

        return panel;
    }
}

