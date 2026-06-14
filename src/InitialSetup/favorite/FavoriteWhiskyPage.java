package InitialSetup.favorite;

import Ui.component.AbstractSearchListPage;
import Ui.buttons.AbstractActionButton;
import Ui.component.FixedImageLabel;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Whisky.Whisky;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class FavoriteWhiskyPage extends AbstractSearchListPage<Whisky> {
    private static final int MINIMUM_FAVORITES = 2;
    private static final Color ITEM_NORMAL = ThemeColors.SURFACE_CARD;
    private static final Color ITEM_SELECTED = ThemeColors.ACCENT_SELECTION;

    private final FavoriteWhiskySetupService setupService = new FavoriteWhiskySetupService();
    private final FavoriteWhiskySelectionModel selectionModel = new FavoriteWhiskySelectionModel();
    private final List<Whisky> whiskies;

    public FavoriteWhiskyPage() {
        super("Favorite Whisky Setup", null, "search", ThemeSizes.SEARCH_FIELD_WIDTH, ThemeSizes.SEARCH_FIELD_HEIGHT);

        whiskies = setupService.loadWhiskies();
        initializeSearchPage();
    }

    private void saveFavoritesAndMove() {
        if (!selectionModel.hasMinimumSelection(MINIMUM_FAVORITES)) {
            JOptionPane.showMessageDialog(this, "Please select at least 2 favorite whiskies.");
            return;
        }

        try {
            setupService.saveFavorites(selectionModel.getSelectedNamesInDisplayOrder(whiskies));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MainPageNavigator.openMainPage();
        dispose();
    }

    private void saveEmptyFavoritesAndMove() {
        try {
            setupService.saveEmptyFavorites();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MainPageNavigator.openMainPage();
        dispose();
    }

    @Override
    protected int getItemsPerTab() {
        return ThemeSizes.LIST_ITEMS_PER_TAB;
    }

    @Override
    protected String getEmptyMessage() {
        return "No whisky data";
    }

    @Override
    protected int getTabbedPanePreferredWidth() {
        return ThemeSizes.FAVORITE_TAB_WIDTH;
    }

    @Override
    protected int getTabbedPanePreferredHeight() {
        return ThemeSizes.FAVORITE_TAB_HEIGHT;
    }

    @Override
    protected JComponent createTopCenterComponent() {
        JLabel titleLabel = new JLabel("Select Your Favorite Whiskies");
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        return titleLabel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JButton saveButton = new AbstractActionButton("Save Favorites", ScreenScale.scale(18)) { };
        saveButton.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.PRIMARY_BUTTON_WIDTH,
                ThemeSizes.PRIMARY_BUTTON_HEIGHT
        ));
        saveButton.addActionListener(e -> saveFavoritesAndMove());

        JButton noTastedButton = new AbstractActionButton("I Have Not Tried Whisky", ScreenScale.scale(16)) { };
        noTastedButton.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.SECONDARY_BUTTON_WIDTH,
                ThemeSizes.SECONDARY_BUTTON_HEIGHT
        ));
        noTastedButton.addActionListener(e -> saveEmptyFavoritesAndMove());

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ScreenScale.scale(32), 0));
        bottomPanel.add(saveButton);
        bottomPanel.add(Box.createRigidArea(ScreenScale.dimension(16, 0)));
        bottomPanel.add(noTastedButton);
        return bottomPanel;
    }

    @Override
    protected List<Whisky> filterItems() {
        String keyword = nameField.getSearchKeyword().toLowerCase();
        java.util.ArrayList<Whisky> filtered = new java.util.ArrayList<>();

        for (Whisky whisky : whiskies) {
            if (!keyword.isBlank() && !whisky.getName().toLowerCase().contains(keyword)) {
                continue;
            }
            filtered.add(whisky);
        }

        return filtered;
    }

    @Override
    protected JPanel createItemPanel(Whisky whisky) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 0));
        itemPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 116));
        itemPanel.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.FAVORITE_ITEM_WIDTH,
                ThemeSizes.FAVORITE_ITEM_HEIGHT
        ));
        itemPanel.setBackground(selectionModel.isSelected(whisky) ? ITEM_SELECTED : ITEM_NORMAL);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(12)
        ));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = new FixedImageLabel(
                whisky.getImagePath(),
                ThemeSizes.WHISKY_THUMB_WIDTH,
                ThemeSizes.WHISKY_THUMB_HEIGHT,
                ThemeSizes.scaledWhiskyThumb()
        );
        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(ThemeColors.TEXT_WHITE);
        nameLabel.setFont(ThemeFonts.bold(17));

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelection(whisky, itemPanel);
            }
        });

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(nameLabel, BorderLayout.CENTER);
        return itemPanel;
    }

    private void toggleSelection(Whisky whisky, JPanel itemPanel) {
        if (!selectionModel.toggle(whisky)) {
            itemPanel.setBackground(ITEM_NORMAL);
            return;
        }

        itemPanel.setBackground(ITEM_SELECTED);
    }

    @Override
    protected JPanel createTabPanel() {
        JPanel tabPanel = new BackgroundPanel();
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
        tabPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(10)
        ));
        return tabPanel;
    }
}
