package TastingNote;

import Ui.buttons.BackButton;
import Ui.buttons.AbstractActionButton;
import Ui.component.FixedImageLabel;
import Ui.icon.StarIconFactory;
import Ui.panel.BackgroundPanel;
import Ui.text.TastingNoteStrings;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.theme.ThemeSpacing;

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
import java.awt.Font;
import java.util.ArrayList;

public class TastingNoteDetailPage extends JFrame {
    private static final int STAR_COUNT = 5;
    private final TastingNote note;
    private final int noteIndex;
    private final JFrame previousPage;
    private final ImageIcon scoreOnIcon = StarIconFactory.createStarIcon(ScreenScale.scale(22), 1.0f);
    private final ImageIcon scoreOffIcon = StarIconFactory.createStarIcon(ScreenScale.scale(22), 1.0f, ThemeColors.TEXT_WHITE);

    public TastingNoteDetailPage(TastingNote note, int noteIndex, JFrame previousPage) {
        this.note = note;
        this.noteIndex = noteIndex;
        this.previousPage = previousPage;

        setTitle(note.getWhiskyName());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new BackgroundPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeSpacing.scale(ThemeSpacing.SPACE_18),
                ThemeSpacing.scale(ThemeSpacing.SPACE_18),
                ThemeSpacing.scale(ThemeSpacing.SPACE_18),
                ThemeSpacing.scale(ThemeSpacing.SPACE_18)
        ));

        JButton backButton = new BackButton(this, previousPage);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 52));
        topPanel.add(backButton, BorderLayout.WEST);

        JButton editButton = new AbstractActionButton(TastingNoteStrings.EDIT_BUTTON, ScreenScale.scale(14)) { };
        editButton.addActionListener(e -> {
            new NewNotePage(previousPage, note, noteIndex);
            dispose();
        });
        topPanel.add(editButton, BorderLayout.EAST);

        JLabel nameLabel = new JLabel(note.getWhiskyName());
        nameLabel.setForeground(ThemeColors.TEXT_WHITE);
        nameLabel.setFont(ThemeFonts.bold(26));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel(note.getDate());
        dateLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        dateLabel.setFont(ThemeFonts.plain(14));
        dateLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel scorePanel = createScorePanel(note.getScore());
        scorePanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel imageLabel = new FixedImageLabel(
                note.getImagePath(),
                ThemeSizes.TASTING_NOTE_DETAIL_IMAGE_WIDTH,
                ThemeSizes.TASTING_NOTE_DETAIL_IMAGE_HEIGHT,
                ThemeSizes.scaledTastingNoteDetailImage(),
                true
        );
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(topPanel);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        contentPanel.add(nameLabel);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_6));
        contentPanel.add(dateLabel);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_10));
        contentPanel.add(scorePanel);
        if (hasImage()) {
            contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
            contentPanel.add(imageLabel);
        }
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
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
        infoPanel.setBackground(ThemeColors.SURFACE_CARD);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN),
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN),
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN),
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN)
        ));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(ScreenScale.dimension(760, 460));

        infoPanel.add(createInfoRow(TastingNoteStrings.LABEL_AROMA, joinNotes(note.getAroma())));
        infoPanel.add(createInfoRow(TastingNoteStrings.LABEL_TASTE, joinNotes(note.getTaste())));
        infoPanel.add(createInfoRow(TastingNoteStrings.LABEL_FINISH, joinNotes(note.getFinish())));
        infoPanel.add(createInfoRow(TastingNoteStrings.LABEL_REVIEW, normalizeText(note.getDetailReview())));

        return infoPanel;
    }

    private JPanel createInfoRow(String title, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(ScreenScale.scale(12), 0));
        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 52));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(ScreenScale.scale(4), 0, ScreenScale.scale(4), 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(ScreenScale.dimension(82, 26));
        titleLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        titleLabel.setFont(ThemeFonts.bold(13));

        JLabel valueLabel = new JLabel(isBlank(value) ? TastingNoteStrings.EMPTY_VALUE : value);
        valueLabel.setForeground(ThemeColors.TEXT_WHITE);
        valueLabel.setFont(ThemeFonts.plain(14));

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
                star.setFont(ThemeFonts.bold(20));
                star.setForeground(filled ? ThemeColors.ACCENT_STAR : ThemeColors.TEXT_WHITE);
            }

            panel.add(star);
            if (i < STAR_COUNT - 1) {
                panel.add(ThemeSpacing.horizontalGap(3));
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

    private boolean hasImage() {
        return note.getImagePath() != null && !note.getImagePath().isBlank();
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
