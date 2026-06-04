package InitialSetup;

import Ui.BackgroundPanel;
import Ui.BaseList;
import Ui.BaseSearchField;
import Ui.ImageScaler;
import Ui.ScreenScale;
import Ui.StyledTabbedPane;
import Whisky.Whisky;
import Whisky.WhiskyList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FavoriteWhiskyPage extends JFrame {
    private static final String WHISKY_FILE_PATH = "src/assets/Flie(txt)/whiskyList.txt";
    private static final String FAVORITE_FILE_PATH = "src/assets/Flie(txt)/favoriteWhiskies.txt";
    private static final int ITEMS_PER_TAB = 5;
    private static final int THUMB_WIDTH = 84;
    private static final int THUMB_HEIGHT = 96;
    private static final Color ITEM_NORMAL = new Color(24, 24, 24);
    private static final Color ITEM_SELECTED = new Color(110, 72, 24);

    private final ArrayList<Whisky> whiskies;
    private final Set<Whisky> selectedWhiskies = new LinkedHashSet<>();
    private final JTabbedPane listTabs = new StyledTabbedPane();
    private final BaseSearchField nameSearchField = new BaseSearchField("search", 560, 32) { };

    public FavoriteWhiskyPage() {
        setTitle("Favorite Whisky Setup");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        whiskies = new ArrayList<>(new WhiskyList(WHISKY_FILE_PATH).getWhiskies());

        JLabel titleLabel = new JLabel("Select Your Favorite Whiskies");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(30)));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(24),
                ScreenScale.scale(24),
                0,
                ScreenScale.scale(24)
        ));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        listTabs.setPreferredSize(ScreenScale.dimension(560, 420));

        nameSearchField.onChange(this::refreshList);
        refreshList();

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        nameSearchField.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(nameSearchField);
        centerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        listTabs.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(listTabs);

        JButton saveButton = new JButton("Save Favorites");
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(18)));
        saveButton.setPreferredSize(ScreenScale.dimension(220, 46));
        saveButton.addActionListener(e -> saveFavoritesAndMove());

        JButton noTastedButton = new JButton("I Have Not Tried Whisky");
        noTastedButton.setFocusPainted(false);
        noTastedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noTastedButton.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(16)));
        noTastedButton.setPreferredSize(ScreenScale.dimension(280, 46));
        noTastedButton.addActionListener(e -> saveEmptyFavoritesAndMove());

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ScreenScale.scale(32), 0));
        bottomPanel.add(saveButton);
        bottomPanel.add(Box.createRigidArea(ScreenScale.dimension(16, 0)));
        bottomPanel.add(noTastedButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void saveFavoritesAndMove() {
        if (selectedWhiskies.size() < 2) {
            JOptionPane.showMessageDialog(this, "Please select at least 2 favorite whiskies.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITE_FILE_PATH))) {
            for (Whisky whisky : whiskies) {
                if (selectedWhiskies.contains(whisky)) {
                    writer.write(whisky.getName());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        openMainPage();
        dispose();
    }

    private void saveEmptyFavoritesAndMove() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITE_FILE_PATH))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        openMainPage();
        dispose();
    }

    private JPanel createWhiskyItem(Whisky whisky) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 0));
        itemPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 116));
        itemPanel.setPreferredSize(ScreenScale.dimension(540, 116));
        itemPanel.setBackground(selectedWhiskies.contains(whisky) ? ITEM_SELECTED : ITEM_NORMAL);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(10),
                ScreenScale.scale(12)
        ));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = createImageLabel(whisky.getImagePath());
        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(17)));

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
        if (selectedWhiskies.contains(whisky)) {
            selectedWhiskies.remove(whisky);
            itemPanel.setBackground(ITEM_NORMAL);
            return;
        }

        selectedWhiskies.add(whisky);
        itemPanel.setBackground(ITEM_SELECTED);
    }

    private void refreshList() {
        String keyword = nameSearchField.getSearchKeyword().toLowerCase();
        ArrayList<Whisky> filtered = new ArrayList<>();

        for (Whisky whisky : whiskies) {
            if (!keyword.isBlank() && !whisky.getName().toLowerCase().contains(keyword)) {
                continue;
            }
            filtered.add(whisky);
        }

        listTabs.removeAll();
        List<List<Whisky>> tabs = BaseList.partition(filtered, ITEMS_PER_TAB);

        if (tabs.isEmpty()) {
            listTabs.addTab("1", createListScrollPane(createEmptyMessagePanel()));
            listTabs.setEnabledAt(0, false);
            return;
        }

        for (int i = 0; i < tabs.size(); i++) {
            JPanel tabPanel = new BackgroundPanel();
            tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
            tabPanel.setBorder(BorderFactory.createEmptyBorder(
                    ScreenScale.scale(10),
                    ScreenScale.scale(10),
                    ScreenScale.scale(10),
                    ScreenScale.scale(10)
            ));

            for (Whisky whisky : tabs.get(i)) {
                tabPanel.add(createWhiskyItem(whisky));
                tabPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
            }

            listTabs.addTab(String.valueOf(i + 1), createListScrollPane(tabPanel));
        }
    }

    private JScrollPane createListScrollPane(JPanel panel) {
        JScrollPane listScrollPane = new JScrollPane(panel);
        listScrollPane.setBorder(BorderFactory.createEmptyBorder());
        listScrollPane.getVerticalScrollBar().setUnitIncrement(ScreenScale.scale(16));
        return listScrollPane;
    }

    private JPanel createEmptyMessagePanel() {
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel emptyLabel = new JLabel("No whisky data");
        emptyLabel.setForeground(Color.WHITE);
        emptyLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(16)));
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(emptyLabel);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JLabel createImageLabel(String imagePath) {
        JLabel imageLabel = new JLabel();
        int thumbWidth = ScreenScale.scale(THUMB_WIDTH);
        int thumbHeight = ScreenScale.scale(THUMB_HEIGHT);
        imageLabel.setPreferredSize(new Dimension(thumbWidth, thumbHeight));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(45, 45, 45));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        if (imagePath == null || imagePath.isBlank()) {
            return imageLabel;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return imageLabel;
        }

        ImageIcon icon = ImageScaler.loadScaledIcon(imagePath, thumbWidth, thumbHeight);
        if (icon != null) {
            imageLabel.setIcon(icon);
        }
        return imageLabel;
    }

    private void openMainPage() {
        try {
            Class<?> mainPageClass = Class.forName("MainPage");
            JFrame mainPage = (JFrame) mainPageClass.getDeclaredConstructor().newInstance();
            mainPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
