package Search;

import Ui.buttons.FavoriteStarButton;
import Ui.buttons.AbstractActionButton;
import Ui.component.AbstractSearchListPage;
import Ui.component.FixedImageLabel;
import Ui.panel.BackgroundPanel;
import Ui.panel.WoodFilterPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.util.AppPaths;
import Whisky.FavoriteWhiskyStore;
import Whisky.Whisky;
import Whisky.WhiskyDetailPage;
import Whisky.WhiskyList;
import Whisky.WhiskyRecommender;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AbstractSearchListPage<Whisky> {
    private static ArrayList<Whisky> cachedWhiskies;
    private static Filter cachedFilter;

    private final ArrayList<Whisky> allWhiskies;
    private final Filter filter;
    private final JComboBox<String> countryBox;
    private final JComboBox<String> typeBox;
    private final JComboBox<String> aromaBox;
    private final JComboBox<String> tasteBox;
    private final JComboBox<String> finishBox;

    public SearchPage(JFrame previousPage) {
        super("Search", previousPage, "search", ThemeSizes.SEARCH_FIELD_WIDTH, ThemeSizes.SEARCH_FIELD_HEIGHT);

        allWhiskies = getCachedWhiskies();
        filter = getCachedFilter(allWhiskies);

        countryBox = createOptionBox(filter.buildCountryOptions());
        typeBox = createOptionBox(filter.buildTypeOptions());
        aromaBox = createOptionBox(filter.buildAromaOptions());
        tasteBox = createOptionBox(filter.buildTasteOptions());
        finishBox = createOptionBox(filter.buildFinishOptions());
        initializeSearchPage();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                refreshFavoriteStars();
            }
        });
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
    protected List<Whisky> filterItems() {
        return applyFilters();
    }

    @Override
    protected JPanel createItemPanel(Whisky whisky) {
        return createWhiskyItem(whisky);
    }

    @Override
    protected JComponent createEastPanel() {
        JPanel filterPanel = new WoodFilterPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(12),
                ScreenScale.scale(12),
                ScreenScale.scale(12)
        ));
        filterPanel.setPreferredSize(ScreenScale.dimension(ThemeSizes.SEARCH_FILTER_PANEL_WIDTH, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(ScreenScale.scale(4), 0, ScreenScale.scale(4), 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        addFilterRow(filterPanel, gbc, "Country", countryBox);
        addFilterRow(filterPanel, gbc, "Type", typeBox);
        addFilterRow(filterPanel, gbc, "Aroma", aromaBox);
        addFilterRow(filterPanel, gbc, "Taste", tasteBox);
        addFilterRow(filterPanel, gbc, "Finish", finishBox);

        JButton searchButton = new AbstractActionButton("Search", ScreenScale.scale(13)) { };
        WoodFilterPanel.styleActionButton(searchButton);
        searchButton.addActionListener(e -> refreshList());
        gbc.gridy++;
        filterPanel.add(searchButton, gbc);

        JButton recommendButton = new AbstractActionButton("Recommend", ScreenScale.scale(13)) { };
        WoodFilterPanel.styleActionButton(recommendButton);
        recommendButton.addActionListener(e -> showRecommendations());
        gbc.gridy++;
        filterPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 8)), gbc);
        gbc.gridy++;
        filterPanel.add(recommendButton, gbc);

        return filterPanel;
    }

    private void addFilterRow(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        JLabel title = new JLabel(label);
        WoodFilterPanel.styleFilterLabel(title);
        gbc.gridy++;
        panel.add(title, gbc);
        gbc.gridy++;
        panel.add(component, gbc);
    }

    private JComboBox<String> createOptionBox(List<String> options) {
        JComboBox<String> box = new JComboBox<>(options.toArray(new String[0]));
        box.setFont(ThemeFonts.plain(13));
        WoodFilterPanel.styleComboBox(box);
        return box;
    }

    private ArrayList<Whisky> applyFilters() {
        return filter.apply(
                nameField.getSearchKeyword(),
                (String) countryBox.getSelectedItem(),
                (String) typeBox.getSelectedItem(),
                (String) aromaBox.getSelectedItem(),
                (String) tasteBox.getSelectedItem(),
                (String) finishBox.getSelectedItem()
        );
    }

    private JPanel createWhiskyItem(Whisky whisky) {
        JPanel itemPanel = new JPanel(new BorderLayout(16, 0));
        itemPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 116));
        itemPanel.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.SEARCH_RESULT_ITEM_WIDTH,
                ThemeSizes.SEARCH_RESULT_ITEM_HEIGHT
        ));
        itemPanel.setBackground(new java.awt.Color(24, 24, 24));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(14)
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
        FavoriteStarButton starButton = new FavoriteStarButton(whisky.getName(), ScreenScale.scale(28));

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(nameLabel, BorderLayout.CENTER);
        itemPanel.add(starButton, BorderLayout.EAST);
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new WhiskyDetailPage(whisky, SearchPage.this);
                setVisible(false);
            }
        });

        return itemPanel;
    }
    private void refreshFavoriteStars() {
        refreshFavoriteStars(listTabs);
    }

    private void refreshFavoriteStars(java.awt.Container container) {
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof FavoriteStarButton favoriteStarButton) {
                favoriteStarButton.refreshFavoriteState();
                continue;
            }

            if (component instanceof java.awt.Container childContainer) {
                refreshFavoriteStars(childContainer);
            }
        }
    }

    private void showRecommendations() {
        List<Whisky> recommendations = WhiskyRecommender.recommend(
                allWhiskies,
                FavoriteWhiskyStore.getFavorites()
        );

        if (recommendations.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Not enough matching whiskies were found from your favorite whisky data.",
                    "Recommendation",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        nameField.setText("");
        countryBox.setSelectedIndex(0);
        typeBox.setSelectedIndex(0);
        aromaBox.setSelectedIndex(0);
        tasteBox.setSelectedIndex(0);
        finishBox.setSelectedIndex(0);
        refreshList(recommendations);
    }

    private static synchronized ArrayList<Whisky> getCachedWhiskies() {
        if (cachedWhiskies == null) {
            WhiskyList whiskyList = new WhiskyList(AppPaths.WHISKY_LIST_FILE);
            cachedWhiskies = new ArrayList<>(whiskyList.getWhiskies());
        }
        return new ArrayList<>(cachedWhiskies);
    }

    private static synchronized Filter getCachedFilter(List<Whisky> whiskies) {
        if (cachedFilter == null) {
            cachedFilter = new Filter(whiskies, AppPaths.FILTER_OPTIONS_FILE);
        }
        return cachedFilter;
    }
}

