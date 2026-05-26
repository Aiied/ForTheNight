package InitialSetup;

import Ui.BackgroundPanel;
import Ui.BaseSearchField;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FavoriteWhiskyPage extends JFrame {
    private static final String WHISKY_FILE_PATH = "src/assets/Flie(txt)/whiskyList.txt";
    private static final String FAVORITE_FILE_PATH = "src/assets/Flie(txt)/favoriteWhiskies.txt";
    private static final Color ITEM_NORMAL = new Color(24, 24, 24);
    private static final Color ITEM_SELECTED = new Color(110, 72, 24);

    private final ArrayList<Whisky> whiskies;
    private final Set<Whisky> selectedWhiskies = new LinkedHashSet<>();
    private final JPanel listPanel = new BackgroundPanel();
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 0, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameSearchField.onChange(this::refreshList);

        refreshList();

        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setBorder(BorderFactory.createEmptyBorder());
        listScrollPane.setPreferredSize(new Dimension(560, 420));

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        nameSearchField.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(nameSearchField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        listScrollPane.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(listScrollPane);

        JButton saveButton = new JButton("Save Favorites");
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        saveButton.setPreferredSize(new Dimension(220, 46));
        saveButton.addActionListener(e -> saveFavoritesAndMove());

        JButton noTastedButton = new JButton("I Have Not Tried Whisky");
        noTastedButton.setFocusPainted(false);
        noTastedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noTastedButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        noTastedButton.setPreferredSize(new Dimension(280, 46));
        noTastedButton.addActionListener(e -> {
            new TastePreferencePage(this);
            setVisible(false);
        });

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 32, 0));
        bottomPanel.add(saveButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(16, 0)));
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

    private JPanel createWhiskyItem(Whisky whisky) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 0));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        itemPanel.setPreferredSize(new Dimension(520, 92));
        itemPanel.setBackground(selectedWhiskies.contains(whisky) ? ITEM_SELECTED : ITEM_NORMAL);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 12));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = createImageLabel(whisky.getImagePath());
        JLabel nameLabel = new JLabel(whisky.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

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
        listPanel.removeAll();

        for (Whisky whisky : whiskies) {
            if (!keyword.isBlank() && !whisky.getName().toLowerCase().contains(keyword)) {
                continue;
            }
            listPanel.add(createWhiskyItem(whisky));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JLabel createImageLabel(String imagePath) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(64, 72));
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

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(64, 72, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
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
