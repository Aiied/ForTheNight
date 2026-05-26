package Ui.Button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;

public abstract class ImageMenuButton extends JButton {
    private static final int IMAGE_WIDTH = 360;
    private static final int IMAGE_HEIGHT = 120;

    protected ImageMenuButton(String imagePath) {
        super();

        setIcon(createButtonIcon(imagePath));
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        setMaximumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    private ImageIcon createButtonIcon(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
