package Ui.buttons;

import Ui.theme.ThemeSizes;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Image;

public abstract class ImageMenuButton extends JButton {
    protected ImageMenuButton(String imagePath) {
        super();

        setIcon(createButtonIcon(imagePath));
        setPreferredSize(ThemeSizes.scaledMenuButton());
        setMinimumSize(ThemeSizes.scaledMenuButton());
        setMaximumSize(ThemeSizes.scaledMenuButton());
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    private ImageIcon createButtonIcon(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(
                ThemeSizes.MENU_BUTTON_WIDTH,
                ThemeSizes.MENU_BUTTON_HEIGHT,
                Image.SCALE_SMOOTH
        );
        return new ImageIcon(image);
    }
}
