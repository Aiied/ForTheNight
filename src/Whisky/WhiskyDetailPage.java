package Whisky;

import Ui.buttons.BackButton;
import Ui.buttons.FavoriteStarButton;
import Ui.component.FixedImageLabel;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.util.ArrayList;

public class WhiskyDetailPage extends JFrame {
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(18),
                ScreenScale.scale(18),
                ScreenScale.scale(18),
                ScreenScale.scale(18)
        ));

        JButton backButton = new BackButton();
        backButton.addActionListener(e -> {
            previousPage.setVisible(true);
            dispose();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 52));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel imageLabel = new FixedImageLabel(
                whisky.getImagePath(),
                ThemeSizes.WHISKY_DETAIL_IMAGE_WIDTH,
                ThemeSizes.WHISKY_DETAIL_IMAGE_HEIGHT,
                ThemeSizes.scaledWhiskyDetailImage(),
                true
        );
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel nameRow = new JPanel(new BorderLayout(10, 0));
        nameRow.setOpaque(false);
        nameRow.setMaximumSize(ScreenScale.dimension(
                ThemeSizes.WHISKY_DETAIL_NAME_ROW_WIDTH,
                ThemeSizes.WHISKY_DETAIL_NAME_ROW_HEIGHT
        ));

        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(ThemeColors.TEXT_WHITE);
        nameLabel.setFont(ThemeFonts.bold(24));

        FavoriteStarButton starButton = new FavoriteStarButton(whisky.getName(), ScreenScale.scale(34));
        nameRow.add(nameLabel, BorderLayout.CENTER);
        nameRow.add(starButton, BorderLayout.EAST);
        nameRow.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(topPanel);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        contentPanel.add(nameRow);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
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
                ScreenScale.scale(16),
                ScreenScale.scale(16),
                ScreenScale.scale(16),
                ScreenScale.scale(16)
        ));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(ScreenScale.dimension(720, 320));

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
        JPanel rowPanel = new JPanel(new BorderLayout(ScreenScale.scale(12), 0));
        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 34));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(ScreenScale.dimension(72, 26));
        titleLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        titleLabel.setFont(ThemeFonts.bold(13));

        JLabel valueLabel = new JLabel(isBlank(value) ? "-" : value);
        valueLabel.setForeground(ThemeColors.TEXT_WHITE);
        valueLabel.setFont(ThemeFonts.plain(14));

        rowPanel.add(titleLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.CENTER);

        return rowPanel;
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

