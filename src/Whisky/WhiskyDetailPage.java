package Whisky;

import Ui.buttons.BackButton;
import Ui.buttons.FavoriteStarButton;
import Ui.component.FixedImageLabel;
import Ui.panel.BackgroundPanel;
import Ui.text.WhiskyStrings;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.theme.ThemeSpacing;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
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

        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(ThemeColors.TEXT_WHITE);
        nameLabel.setFont(ThemeFonts.bold(24));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        FavoriteStarButton starButton = new FavoriteStarButton(whisky.getName(), ScreenScale.scale(34));
        JPanel imagePanel = createImagePanel(starButton);
        JPanel nameRow = createNameRow(nameLabel);
        nameRow.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(topPanel);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        contentPanel.add(imagePanel);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        contentPanel.add(nameRow);
        contentPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        contentPanel.add(createInfoPanel());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane);

        setVisible(true);
    }

    private JPanel createImagePanel(FavoriteStarButton starButton) {
        Dimension imageSize = ThemeSizes.scaledWhiskyDetailImage();
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(imageSize);
        layeredPane.setMinimumSize(imageSize);
        layeredPane.setMaximumSize(imageSize);
        layeredPane.setSize(imageSize);

        JLabel imageLabel = new FixedImageLabel(
                whisky.getImagePath(),
                ThemeSizes.WHISKY_DETAIL_IMAGE_WIDTH,
                ThemeSizes.WHISKY_DETAIL_IMAGE_HEIGHT,
                imageSize,
                true
        );
        imageLabel.setBounds(0, 0, imageSize.width, imageSize.height);

        int starSize = starButton.getPreferredSize().width;
        int starInset = ThemeSpacing.scale(ThemeSpacing.SPACE_10);
        starButton.setBounds(
                imageSize.width - starSize - starInset,
                starInset,
                starSize,
                starSize
        );

        layeredPane.add(imageLabel, Integer.valueOf(0));
        layeredPane.add(starButton, Integer.valueOf(1));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(CENTER_ALIGNMENT);
        wrapper.setMaximumSize(imageSize);
        wrapper.add(layeredPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createNameRow(JLabel nameLabel) {
        JPanel nameRow = new JPanel(new BorderLayout());
        nameRow.setOpaque(false);
        nameRow.setMaximumSize(ScreenScale.dimension(
                ThemeSizes.WHISKY_DETAIL_NAME_ROW_WIDTH,
                ThemeSizes.WHISKY_DETAIL_NAME_ROW_HEIGHT
        ));
        nameRow.add(nameLabel, BorderLayout.CENTER);
        return nameRow;
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
        infoPanel.setMaximumSize(ScreenScale.dimension(720, 320));

        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_COUNTRY, whisky.getCountry()));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_TYPE, whisky.getWhiskyType()));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_DISTILLERY, whisky.getDistillery()));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_CASK, whisky.getCask()));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_ABV, formatAbv()));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_AROMA, joinNotes(whisky.getAroma())));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_TASTE, joinNotes(whisky.getTaste())));
        infoPanel.add(createInfoRow(WhiskyStrings.LABEL_FINISH, joinNotes(whisky.getFinish())));

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

        JLabel valueLabel = new JLabel(isBlank(value) ? WhiskyStrings.EMPTY_VALUE : value);
        valueLabel.setForeground(ThemeColors.TEXT_WHITE);
        valueLabel.setFont(ThemeFonts.plain(14));

        rowPanel.add(titleLabel, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.CENTER);

        return rowPanel;
    }
    private String formatAbv() {
        if (whisky.getAbv() == 0.0f) {
            return WhiskyStrings.EMPTY_VALUE;
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

