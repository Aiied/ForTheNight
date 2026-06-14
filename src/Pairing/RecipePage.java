package Pairing;

import Ui.buttons.BackButton;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

public class RecipePage extends JFrame {
    private final Food food;
    private final JFrame previousPage;

    public RecipePage(Food food, JFrame previousPage) {
        this.food = food;
        this.previousPage = previousPage;

        setTitle(food.getRecipe());
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

        JLabel recipeLabel = new JLabel(food.getRecipe());
        recipeLabel.setForeground(ThemeColors.TEXT_WHITE);
        recipeLabel.setFont(ThemeFonts.bold(30));
        recipeLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel whiskyLabel = new JLabel(food.getWhiskyName());
        whiskyLabel.setForeground(ThemeColors.ACCENT_GOLD_BRIGHT);
        whiskyLabel.setFont(ThemeFonts.bold(20));
        whiskyLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(ThemeColors.SURFACE_CARD);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(18),
                ScreenScale.scale(18),
                ScreenScale.scale(18),
                ScreenScale.scale(18)
        ));
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(ScreenScale.dimension(760, 420));

        infoPanel.add(createBodyLabel("Recommended Pairing: " + food.getName()));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 14)));
        infoPanel.add(createBodyLabel("Whisky: " + food.getWhiskyName()));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        infoPanel.add(createBodyLabel("Recipe: " + safeValue(food.getRecipe())));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        infoPanel.add(createBodyLabel("Liqueur: " + safeValue(food.getLiqueur())));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        infoPanel.add(createBodyLabel("Mixer: " + safeValue(food.getDrink())));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        infoPanel.add(createBodyLabel("Garnish: " + safeValue(food.getGarnish())));
        infoPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        infoPanel.add(createBodyLabel("Additional Ingredients: " + safeValue(food.getExtraIngredient())));

        contentPanel.add(topPanel);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        contentPanel.add(recipeLabel);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 8)));
        contentPanel.add(whiskyLabel);
        contentPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 20)));
        contentPanel.add(infoPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane);

        setVisible(true);
    }

    private JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ThemeColors.TEXT_WHITE);
        label.setFont(ThemeFonts.plain(16));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private String safeValue(String text) {
        return text == null || text.isBlank() ? "-" : text;
    }
}
