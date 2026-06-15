package Ui.theme;

import javax.swing.Box;
import java.awt.Component;
import java.awt.Insets;

public final class ThemeSpacing {
    public static final int SPACE_2 = 2;
    public static final int SPACE_4 = 4;
    public static final int SPACE_6 = 6;
    public static final int SPACE_8 = 8;
    public static final int SPACE_10 = 10;
    public static final int SPACE_12 = 12;
    public static final int SPACE_14 = 14;
    public static final int SPACE_16 = 16;
    public static final int SPACE_18 = 18;
    public static final int SPACE_20 = 20;
    public static final int SPACE_24 = 24;
    public static final int SPACE_32 = 32;
    public static final int PAGE_MARGIN = 16;

    private ThemeSpacing() {
    }

    public static int scale(int value) {
        return ScreenScale.scale(value);
    }

    public static Insets insets(int top, int left, int bottom, int right) {
        return new Insets(scale(top), scale(left), scale(bottom), scale(right));
    }

    public static Component verticalGap(int height) {
        return Box.createRigidArea(ScreenScale.dimension(0, height));
    }

    public static Component horizontalGap(int width) {
        return Box.createRigidArea(ScreenScale.dimension(width, 0));
    }
}
