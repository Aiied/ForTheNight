package Pairing;

import Ui.BaseList;
import Ui.BaseSearchField;
import Ui.BackgroundPanel;
import Ui.Button.BackButton;
import Ui.ImageScaler;
import Ui.StyledTabbedPane;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PairingPage extends JFrame {
    private static final String PAIRING_FILE_PATH = "src/assets/Flie(txt)/pairing_english.txt";
    private static final String PAIRING_IMAGE_DIR = "src/assets/image/Pairing/";
    private static final int ITEMS_PER_TAB = 5;
    private static final int THUMB_WIDTH = 84;
    private static final int THUMB_HEIGHT = 96;

    private final JFrame previousPage;
    private final ArrayList<Food> allFoods;
    private final JTabbedPane listTabs;
    private final BaseSearchField nameField;
    private final JComboBox<String> recipeBox;

    public PairingPage() {
        this(null);
    }

    public PairingPage(JFrame previousPage) {
        this.previousPage = previousPage;
        setTitle("Pairing");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        FoodList foodList = new FoodList(PAIRING_FILE_PATH);
        allFoods = new ArrayList<>(foodList.getFoods());

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

        JPanel filterPanel = new JPanel();
        filterPanel.setOpaque(true);
        filterPanel.setBackground(new Color(18, 18, 18, 220));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        filterPanel.setPreferredSize(new Dimension(320, 0));
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        nameField = new BaseSearchField("search", 560, 32) { };
        nameField.onChange(this::refreshList);

        recipeBox = createOptionBox(buildRecipeOptions());
        recipeBox.addActionListener(e -> refreshList());
        addFilterBlock(filterPanel, "Recipe", recipeBox);

        listTabs = new StyledTabbedPane();
        refreshList();

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

    private void addFilterBlock(JPanel panel, String label, JComponent component) {
        JLabel title = new JLabel(label);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
    }

    private JComboBox<String> createOptionBox(List<String> options) {
        JComboBox<String> box = new JComboBox<>(options.toArray(new String[0]));
        box.setFont(new Font("SansSerif", Font.PLAIN, 13));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return box;
    }

    private List<String> buildRecipeOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.add("All");
        for (Food food : allFoods) {
            if (!food.getRecipe().isBlank() && !options.contains(food.getRecipe())) {
                options.add(food.getRecipe());
            }
        }
        return options;
    }

    private void refreshList() {
        listTabs.removeAll();
        List<List<Food>> tabs = BaseList.partition(applyFilters(), ITEMS_PER_TAB);

        if (tabs.isEmpty()) {
            listTabs.addTab("1", createListScrollPane(createEmptyMessagePanel()));
            listTabs.setEnabledAt(0, false);
            return;
        }

        for (int i = 0; i < tabs.size(); i++) {
            JPanel tabPanel = new BackgroundPanel();
            tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
            tabPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            for (Food food : tabs.get(i)) {
                tabPanel.add(createFoodItem(food));
                tabPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            listTabs.addTab(String.valueOf(i + 1), createListScrollPane(tabPanel));
        }
    }

    private List<Food> applyFilters() {
        ArrayList<Food> filtered = new ArrayList<>();
        String keyword = nameField.getSearchKeyword().toLowerCase();
        String selectedRecipe = (String) recipeBox.getSelectedItem();

        for (Food food : allFoods) {
            boolean matchesKeyword = keyword.isBlank()
                    || food.getName().toLowerCase().contains(keyword)
                    || food.getWhiskyName().toLowerCase().contains(keyword);
            boolean matchesRecipe = selectedRecipe == null
                    || "All".equals(selectedRecipe)
                    || food.getRecipe().equals(selectedRecipe);

            if (matchesKeyword && matchesRecipe) {
                filtered.add(food);
            }
        }

        return filtered;
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

        JLabel emptyLabel = new JLabel("No pairing data");
        emptyLabel.setForeground(Color.WHITE);
        emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(emptyLabel);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createFoodItem(Food food) {
        JPanel itemPanel = new JPanel(new BorderLayout(16, 0));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 128));
        itemPanel.setPreferredSize(new Dimension(520, 128));
        itemPanel.setBackground(new Color(24, 24, 24));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        itemPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JLabel imageLabel = createImageLabel(food);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel foodLabel = new JLabel(food.getName());
        foodLabel.setForeground(Color.WHITE);
        foodLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel whiskyLabel = new JLabel(food.getWhiskyName());
        whiskyLabel.setForeground(new Color(219, 193, 131));
        whiskyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JComponent recipeComponent = createRecipeComponent(food);

        JLabel garnishLabel = new JLabel(buildMetaLine(food));
        garnishLabel.setForeground(new Color(150, 150, 150));
        garnishLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        textPanel.add(foodLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        textPanel.add(whiskyLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        recipeComponent.setAlignmentX(LEFT_ALIGNMENT);
        textPanel.add(recipeComponent);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(garnishLabel);

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JComponent createRecipeComponent(Food food) {
        String recipe = food.getRecipe().isBlank() ? "-" : food.getRecipe();
        if ("-".equals(recipe)) {
            JLabel recipeLabel = new JLabel(recipe);
            recipeLabel.setForeground(new Color(190, 190, 190));
            recipeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            return recipeLabel;
        }

        JButton recipeButton = new JButton(recipe);
        recipeButton.setFocusPainted(false);
        recipeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recipeButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        recipeButton.setForeground(Color.WHITE);
        recipeButton.setBackground(new Color(186, 126, 52));
        recipeButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        recipeButton.addActionListener(e -> {
            new RecipePage(food, PairingPage.this);
            setVisible(false);
        });
        return recipeButton;
    }

    private String buildMetaLine(Food food) {
        ArrayList<String> parts = new ArrayList<>();
        if (!food.getDrink().isBlank()) {
            parts.add(food.getDrink());
        }
        if (!food.getGarnish().isBlank()) {
            parts.add(food.getGarnish());
        }
        if (!food.getLiqueur().isBlank()) {
            parts.add(food.getLiqueur());
        }
        if (!food.getExtraIngredient().isBlank()) {
            parts.add(food.getExtraIngredient());
        }
        return parts.isEmpty() ? "-" : String.join(" / ", parts);
    }

    private JLabel createImageLabel(Food food) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(THUMB_WIDTH, THUMB_HEIGHT));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(45, 45, 45));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        String imagePath = resolveImagePath(food.getName());
        if (imagePath == null) {
            return imageLabel;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return imageLabel;
        }

        javax.swing.ImageIcon icon = ImageScaler.loadScaledIcon(imagePath, THUMB_WIDTH, THUMB_HEIGHT);
        if (icon != null) {
            imageLabel.setIcon(icon);
        }

        return imageLabel;
    }

    private String resolveImagePath(String foodName) {
        if (foodName == null || foodName.isBlank()) {
            return null;
        }

        String normalized = foodName
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");

        if (normalized.isBlank()) {
            return null;
        }

        return PAIRING_IMAGE_DIR + normalized + ".png";
    }
}
