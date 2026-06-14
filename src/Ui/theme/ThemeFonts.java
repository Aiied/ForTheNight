package Ui.theme;

import java.awt.Font;

public final class ThemeFonts {
    public static final String PRIMARY_FAMILY = "SansSerif";

    private ThemeFonts() {
    }

    public static Font plain(int size) {
        return new Font(PRIMARY_FAMILY, Font.PLAIN, ScreenScale.scale(size));
    }

    public static Font bold(int size) {
        return new Font(PRIMARY_FAMILY, Font.BOLD, ScreenScale.scale(size));
    }

    public static Font plainRaw(int size) {
        return new Font(PRIMARY_FAMILY, Font.PLAIN, size);
    }

    public static Font boldRaw(int size) {
        return new Font(PRIMARY_FAMILY, Font.BOLD, size);
    }
}
