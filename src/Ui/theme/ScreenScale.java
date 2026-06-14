package Ui.theme;

import java.awt.Dimension;
import java.awt.Toolkit;

public final class ScreenScale {
    private static final double BASE_WIDTH = 1920.0;
    private static final double BASE_HEIGHT = 1080.0;
    private static final double MIN_SCALE = 0.9;
    private static final double MAX_SCALE = 1.35;
    private static final double SCALE = resolveScale();

    private ScreenScale() {
    }

    public static int scale(int value) {
        if (value <= 0) {
            return value;
        }
        if (value == Integer.MAX_VALUE) {
            return value;
        }
        return Math.max(1, (int) Math.round(value * SCALE));
    }

    public static Dimension dimension(int width, int height) {
        return new Dimension(scale(width), scale(height));
    }

    private static double resolveScale() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double widthScale = screen.getWidth() / BASE_WIDTH;
        double heightScale = screen.getHeight() / BASE_HEIGHT;
        double scale = Math.min(widthScale, heightScale);
        return Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }
}
