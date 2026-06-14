package Ui.component;

import java.awt.Dimension;

public final class FixedImageLabel extends AbstractImageLabel {
    public FixedImageLabel(String imagePath, int imageWidth, int imageHeight, Dimension size) {
        this(imagePath, imageWidth, imageHeight, size, false);
    }

    public FixedImageLabel(String imagePath, int imageWidth, int imageHeight, Dimension size, boolean lockMaximumSize) {
        super(imageWidth, imageHeight, size, lockMaximumSize);
        setImagePath(imagePath);
    }
}
