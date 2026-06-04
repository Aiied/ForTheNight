package Search;

import Ui.Button.BackButton;
import Ui.BaseList;
import Ui.BaseSearchField;
import Ui.BackgroundPanel;
import Ui.Button.FavoriteStarButton;
import Ui.ImageScaler;
import Ui.StyledTabbedPane;
import Whisky.Whisky;
import Whisky.WhiskyDetailPage;
import Whisky.WhiskyList;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends JFrame {
    private static final int ITEMS_PER_TAB = 5;
    private static final int THUMB_WIDTH = 84;
    private static final int THUMB_HEIGHT = 96;

    private final JFrame previousPage;
    private final ArrayList<Whisky> allWhiskies;
    private final Filter filter;
    private final JTabbedPane listTabs;
    private final BaseSearchField nameField;
    private final JComboBox<String> countryBox;
    private final JComboBox<String> typeBox;
    private final JComboBox<String> aromaBox;
    private final JComboBox<String> tasteBox;
    private final JComboBox<String> finishBox;

    public SearchPage(JFrame previousPage) {
        this.previousPage = previousPage;
        setTitle("Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        WhiskyList whiskyList = new WhiskyList("src/assets/Flie(txt)/whiskyList.txt");
        allWhiskies = new ArrayList<>(whiskyList.getWhiskies());
        filter = new Filter(allWhiskies, "src/assets/Flie(txt)/filterOptions.txt");

        JButton backButton = new BackButton();
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (this.previousPage != null) {
                this.previousPage.setVisible(true);
            }
            dispose();
        });

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setOpaque(true);
        filterPanel.setBackground(new Color(18, 18, 18, 220));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        filterPanel.setPreferredSize(new Dimension(440, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        nameField = new BaseSearchField("search", 560, 32) { };
        nameField.onChange(() -> refreshList(applyFilters()));

        countryBox = createOptionBox(filter.buildCountryOptions());
        addFilterRow(filterPanel, gbc, "Country", countryBox);

        typeBox = createOptionBox(filter.buildTypeOptions());
        addFilterRow(filterPanel, gbc, "Type", typeBox);

        aromaBox = createOptionBox(filter.buildAromaOptions());
        addFilterRow(filterPanel, gbc, "Aroma", aromaBox);

        tasteBox = createOptionBox(filter.buildTasteOptions());
        addFilterRow(filterPanel, gbc, "Taste", tasteBox);

        finishBox = createOptionBox(filter.buildFinishOptions());
        addFilterRow(filterPanel, gbc, "Finish", finishBox);

        JButton searchButton = new JButton("Search");
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> refreshList(applyFilters()));

        gbc.gridy++;
        filterPanel.add(searchButton, gbc);

        listTabs = new StyledTabbedPane();
        refreshList(allWhiskies);

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        nameField.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(nameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        listTabs.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(listTabs);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void addFilterRow(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        JLabel title = new JLabel(label);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy++;
        panel.add(title, gbc);
        gbc.gridy++;
        panel.add(component, gbc);
    }

    private JComboBox<String> createOptionBox(List<String> options) {
        JComboBox<String> box = new JComboBox<>(options.toArray(new String[0]));
        box.setFont(new Font("SansSerif", Font.PLAIN, 13));
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

    private void refreshList(List<Whisky> whiskies) {
        listTabs.removeAll();
        List<List<Whisky>> tabs = BaseList.partition(whiskies, ITEMS_PER_TAB);

        if (tabs.isEmpty()) {
            listTabs.addTab("1", createListScrollPane(createEmptyMessagePanel()));
            listTabs.setEnabledAt(0, false);
            return;
        }

        for (int i = 0; i < tabs.size(); i++) {
            JPanel tabPanel = new BackgroundPanel();
            tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
            tabPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            for (Whisky whisky : tabs.get(i)) {
                tabPanel.add(createWhiskyItem(whisky));
                tabPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            listTabs.addTab(String.valueOf(i + 1), createListScrollPane(tabPanel));
        }
    }

    private JScrollPane createListScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createEmptyMessagePanel() {
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel emptyLabel = new JLabel("No whisky data");
        emptyLabel.setForeground(java.awt.Color.WHITE);
        emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(emptyLabel);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createWhiskyItem(Whisky whisky) {
        JPanel itemPanel = new JPanel(new BorderLayout(16, 0));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 116));
        itemPanel.setPreferredSize(new Dimension(360, 116));
        itemPanel.setBackground(new java.awt.Color(24, 24, 24));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 14));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = createImageLabel(whisky);
        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(java.awt.Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        FavoriteStarButton starButton = new FavoriteStarButton(whisky.getName(), 28);

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

    private JLabel createImageLabel(Whisky whisky) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(THUMB_WIDTH, THUMB_HEIGHT));
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

        ImageIcon icon = ImageScaler.loadScaledIcon(resolvedPath, THUMB_WIDTH, THUMB_HEIGHT);
        if (icon != null) {
            imageLabel.setIcon(icon);
        }

        return imageLabel;
    }
}

