package Pairing;

import Ui.component.AbstractSearchListPage;
import Ui.buttons.AbstractActionButton;
import Ui.component.FixedImageLabel;
import Ui.panel.BackgroundPanel;
import Ui.panel.WoodFilterPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.util.AppPaths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PairingPage extends AbstractSearchListPage<Food> {
    private static final String ALL_RECIPES_OPTION = "All Cocktails";
    private final JFrame previousPage;
    private final ArrayList<Food> allFoods;
    private final JComboBox<String> recipeBox;

    public PairingPage() {
        this(null);
    }

    public PairingPage(JFrame previousPage) {
        super("Pairing", previousPage, "search", ThemeSizes.SEARCH_FIELD_WIDTH, ThemeSizes.SEARCH_FIELD_HEIGHT);
        this.previousPage = previousPage;

        FoodList foodList = new FoodList(AppPaths.PAIRING_FILE);
        allFoods = new ArrayList<>(foodList.getFoods());

        recipeBox = createOptionBox(buildRecipeOptions());
        recipeBox.addActionListener(e -> refreshList());
        initializeSearchPage();
    }

    @Override
    protected int getItemsPerTab() {
        return ThemeSizes.LIST_ITEMS_PER_TAB;
    }

    @Override
    protected String getEmptyMessage() {
        return "No pairing data";
    }

    @Override
    protected JComponent createSearchAccessoryComponent() {
        return recipeBox;
    }

    @Override
    protected List<Food> filterItems() {
        return applyFilters();
    }

    @Override
    protected JPanel createItemPanel(Food food) {
        return createFoodItem(food);
    }

    private JComboBox<String> createOptionBox(List<String> options) {
        JComboBox<String> box = new JComboBox<>(options.toArray(new String[0]));
        box.setFont(ThemeFonts.plain(13));
        Dimension size = ScreenScale.dimension(
                ThemeSizes.PAIRING_OPTION_WIDTH,
                ThemeSizes.PAIRING_OPTION_HEIGHT
        );
        box.setPreferredSize(size);
        box.setMinimumSize(size);
        box.setMaximumSize(size);
        WoodFilterPanel.styleComboBox(box);
        return box;
    }

    private List<String> buildRecipeOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.add(ALL_RECIPES_OPTION);
        for (Food food : allFoods) {
            if (!food.getRecipe().isBlank() && !options.contains(food.getRecipe())) {
                options.add(food.getRecipe());
            }
        }
        return options;
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
                    || ALL_RECIPES_OPTION.equals(selectedRecipe)
                    || food.getRecipe().equals(selectedRecipe);

            if (matchesKeyword && matchesRecipe) {
                filtered.add(food);
            }
        }

        return filtered;
    }

    private JPanel createFoodItem(Food food) {
        JPanel itemPanel = new JPanel(new BorderLayout(16, 0));
        itemPanel.setMaximumSize(ScreenScale.dimension(Integer.MAX_VALUE, 128));
        itemPanel.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.PAIRING_ITEM_WIDTH,
                ThemeSizes.PAIRING_ITEM_HEIGHT
        ));
        itemPanel.setBackground(ThemeColors.SURFACE_CARD);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(16),
                ScreenScale.scale(12),
                ScreenScale.scale(16)
        ));
        itemPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JLabel imageLabel = new FixedImageLabel(
                resolveImagePath(food.getName()),
                ThemeSizes.WHISKY_THUMB_WIDTH,
                ThemeSizes.WHISKY_THUMB_HEIGHT,
                ThemeSizes.scaledWhiskyThumb()
        );

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel foodLabel = new JLabel(food.getName());
        foodLabel.setForeground(ThemeColors.TEXT_WHITE);
        foodLabel.setFont(ThemeFonts.bold(18));

        JLabel whiskyLabel = new JLabel(food.getWhiskyName());
        whiskyLabel.setForeground(ThemeColors.ACCENT_GOLD_BRIGHT);
        whiskyLabel.setFont(ThemeFonts.bold(14));

        textPanel.add(foodLabel);
        textPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 6)));
        textPanel.add(whiskyLabel);
        JComponent recipeButton = createRecipeButton(food);
        if (recipeButton != null) {
            textPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 6)));
            textPanel.add(recipeButton);
        }

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JComponent createRecipeButton(Food food) {
        if (food.getRecipe().isBlank()) {
            return null;
        }

        JButton recipeButton = new AbstractActionButton(food.getRecipe(), ScreenScale.scale(12)) { };
        recipeButton.setForeground(ThemeColors.TEXT_WHITE);
        recipeButton.setBackground(ThemeColors.ACCENT_GOLD_DEEP);
        recipeButton.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(6),
                ScreenScale.scale(12),
                ScreenScale.scale(6),
                ScreenScale.scale(12)
        ));
        recipeButton.addActionListener(e -> showRecipePopup(food));
        return recipeButton;
    }

    private void showRecipePopup(Food food) {
        JTextArea detailArea = new JTextArea(buildRecipePopupText(food));
        detailArea.setEditable(false);
        detailArea.setFocusable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(ThemeFonts.plain(14));
        detailArea.setBackground(ThemeColors.SURFACE_CARD);
        detailArea.setForeground(ThemeColors.TEXT_WHITE);
        detailArea.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(14),
                ScreenScale.scale(14),
                ScreenScale.scale(14),
                ScreenScale.scale(14)
        ));

        JScrollPane scrollPane = new JScrollPane(detailArea);
        scrollPane.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.PAIRING_POPUP_WIDTH,
                ThemeSizes.PAIRING_POPUP_HEIGHT
        ));
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeColors.BORDER_POPUP));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                food.getRecipe(),
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private String buildRecipePopupText(Food food) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Recommended Pairing: " + food.getName());
        lines.add("");
        lines.add("Whisky: " + food.getWhiskyName());
        appendIfPresent(lines, "Liqueur", food.getLiqueur());
        appendIfPresent(lines, "Mixer", food.getDrink());
        appendIfPresent(lines, "Garnish", food.getGarnish());
        appendIfPresent(lines, "Additional Ingredients", food.getExtraIngredient());
        return String.join("\n", lines);
    }

    private void appendIfPresent(List<String> lines, String label, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        lines.add(label + ": " + value);
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

        return AppPaths.PAIRING_IMAGE_DIR + "/" + normalized + ".png";
    }
}
