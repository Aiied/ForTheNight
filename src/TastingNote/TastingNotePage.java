package TastingNote;

import Ui.buttons.BackButton;
import Ui.buttons.AbstractActionButton;
import Ui.component.FixedImageLabel;
import Ui.icon.StarIconFactory;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.util.AppPaths;

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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TastingNotePage extends JFrame {
    private static final int STAR_COUNT = 5;
    private final JFrame previousPage;
    private final JPanel listPanel = new BackgroundPanel();
    private final ImageIcon scoreOnIcon = StarIconFactory.createStarIcon(ScreenScale.scale(18), 1.0f);
    private final ImageIcon scoreOffIcon = StarIconFactory.createStarIcon(ScreenScale.scale(18), 1.0f, ThemeColors.TEXT_WHITE);

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
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(16),
                0,
                ScreenScale.scale(16)
        ));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Tasting Notes");
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(10),
                ScreenScale.scale(18),
                ScreenScale.scale(12),
                0
        ));

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(8),
                ScreenScale.scale(16),
                ScreenScale.scale(16),
                ScreenScale.scale(16)
        ));

        refreshList();

        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setOpaque(false);
        listScrollPane.getViewport().setOpaque(false);
        listScrollPane.setBorder(null);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(ScreenScale.scale(16));

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(titleLabel);
        centerPanel.add(listScrollPane);

        JButton newNoteButton = new AbstractActionButton("New Note", ScreenScale.scale(16)) { };
        newNoteButton.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.NOTE_ACTION_BUTTON_WIDTH,
                ThemeSizes.NOTE_ACTION_BUTTON_HEIGHT
        ));
        newNoteButton.addActionListener(e -> {
            new NewNotePage(this);
            setVisible(false);
        });

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ScreenScale.scale(32), 0));
        bottomPanel.add(newNoteButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                refreshList();
            }
        });

        setVisible(true);
    }

    private void refreshList() {
        listPanel.removeAll();

        List<TastingNote> notes = new TastingNoteList(AppPaths.TASTING_NOTES_FILE).getTastingNotes();
        if (notes.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasting notes yet.");
            emptyLabel.setForeground(ThemeColors.TEXT_WHITE);
            emptyLabel.setFont(ThemeFonts.bold(16));
            emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(ScreenScale.scale(40), 0, 0, 0));
            listPanel.add(emptyLabel);
            listPanel.revalidate();
            listPanel.repaint();
            return;
        }

        for (int i = 0; i < notes.size(); i++) {
            listPanel.add(createNoteItem(notes.get(i), i));
            listPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createNoteItem(TastingNote note, int noteIndex) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 8));
        itemPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 108));
        itemPanel.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.TASTING_NOTE_ITEM_WIDTH,
                ThemeSizes.TASTING_NOTE_ITEM_HEIGHT
        ));
        itemPanel.setBackground(ThemeColors.SURFACE_CARD);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(14),
                ScreenScale.scale(12),
                ScreenScale.scale(14)
        ));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = new FixedImageLabel(
                note.getImagePath(),
                ThemeSizes.TASTING_NOTE_THUMB_WIDTH,
                ThemeSizes.TASTING_NOTE_THUMB_HEIGHT,
                ThemeSizes.scaledTastingNoteThumb()
        );

        JLabel nameLabel = new JLabel(note.getWhiskyName());
        nameLabel.setForeground(ThemeColors.TEXT_WHITE);
        nameLabel.setFont(ThemeFonts.bold(17));

        JLabel dateLabel = new JLabel(note.getDate());
        dateLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        dateLabel.setFont(ThemeFonts.plain(12));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 4)));
        textPanel.add(dateLabel);
        if (note.getImagePath() != null && !note.getImagePath().isBlank()) {
            JLabel photoLabel = new JLabel("Photo attached");
            photoLabel.setForeground(ThemeColors.TEXT_MUTED);
            photoLabel.setFont(ThemeFonts.plain(12));
            textPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 4)));
            textPanel.add(photoLabel);
        }

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(createScorePanel(note.getScore()), BorderLayout.EAST);
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TastingNoteDetailPage(note, noteIndex, TastingNotePage.this);
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
                star.setFont(ThemeFonts.bold(16));
                star.setForeground(filled ? ThemeColors.ACCENT_STAR : ThemeColors.TEXT_WHITE);
            }

            panel.add(star);
            if (i < STAR_COUNT - 1) {
                panel.add(Box.createRigidArea(ScreenScale.dimension(2, 0)));
            }
        }

        return panel;
    }
}

