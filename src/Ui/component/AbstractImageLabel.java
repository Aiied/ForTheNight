package Ui.component;

import Ui.icon.ImageScaler;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.io.File;

public abstract class AbstractImageLabel extends JLabel {
    private final int imageWidth;
    private final int imageHeight;

    protected AbstractImageLabel(int imageWidth, int imageHeight, Dimension size, boolean lockMaximumSize) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        setPreferredSize(size);
        if (lockMaximumSize) {
            setMaximumSize(size);
        }
        setOpaque(true);
        setBackground(ThemeColors.SURFACE_THUMBNAIL);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }

    protected final void setImagePath(String imagePath) {
        setIcon(null);

        if (imagePath == null || imagePath.isBlank()) {
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return;
        }

        ImageIcon icon = ImageScaler.loadScaledIcon(
                imagePath,
                ScreenScale.scale(imageWidth),
                ScreenScale.scale(imageHeight)
        );
        if (icon != null) {
            setIcon(icon);
        }
    }
}
