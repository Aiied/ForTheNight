package Ui.theme;

import java.awt.Dimension;

public final class ThemeSizes {
    public static final int SEARCH_FIELD_WIDTH = 560;
    public static final int SEARCH_FIELD_HEIGHT = 32;
    public static final int SEARCH_ROW_MAX_WIDTH = 900;
    public static final int SEARCH_ROW_MAX_HEIGHT = 40;
    public static final int SEARCH_ACCESSORY_GAP = 12;
    public static final int SEARCH_FILTER_PANEL_WIDTH = 440;
    public static final int SEARCH_RESULT_ITEM_WIDTH = 360;
    public static final int SEARCH_RESULT_ITEM_HEIGHT = 116;

    public static final int LIST_ITEMS_PER_TAB = 5;
    public static final int FAVORITE_TAB_WIDTH = 560;
    public static final int FAVORITE_TAB_HEIGHT = 420;
    public static final int FAVORITE_ITEM_WIDTH = 540;
    public static final int FAVORITE_ITEM_HEIGHT = 116;
    public static final int PAIRING_OPTION_WIDTH = 220;
    public static final int PAIRING_OPTION_HEIGHT = 32;
    public static final int PAIRING_ITEM_WIDTH = 520;
    public static final int PAIRING_ITEM_HEIGHT = 128;
    public static final int PAIRING_POPUP_WIDTH = 420;
    public static final int PAIRING_POPUP_HEIGHT = 240;
    public static final int TASTING_NOTE_ITEM_WIDTH = 620;
    public static final int TASTING_NOTE_ITEM_HEIGHT = 108;
    public static final int TASTING_NOTE_FORM_WIDTH = 760;
    public static final int TASTING_NOTE_FORM_HEIGHT = 600;
    public static final int TASTING_NOTE_FIELD_WIDTH = 620;
    public static final int TASTING_NOTE_FIELD_HEIGHT = 34;
    public static final int TASTING_NOTE_DETAIL_AREA_WIDTH = 620;
    public static final int TASTING_NOTE_DETAIL_AREA_HEIGHT = 150;
    public static final int WHISKY_DETAIL_NAME_ROW_WIDTH = 520;
    public static final int WHISKY_DETAIL_NAME_ROW_HEIGHT = 44;

    public static final int WHISKY_THUMB_WIDTH = 84;
    public static final int WHISKY_THUMB_HEIGHT = 96;
    public static final int TASTING_NOTE_THUMB_WIDTH = 72;
    public static final int TASTING_NOTE_THUMB_HEIGHT = 72;
    public static final int WHISKY_DETAIL_IMAGE_WIDTH = 280;
    public static final int WHISKY_DETAIL_IMAGE_HEIGHT = 340;
    public static final int TASTING_NOTE_DETAIL_IMAGE_WIDTH = 320;
    public static final int TASTING_NOTE_DETAIL_IMAGE_HEIGHT = 240;
    public static final int TASTING_NOTE_PREVIEW_WIDTH = 220;
    public static final int TASTING_NOTE_PREVIEW_HEIGHT = 160;
    public static final int MAIN_LOGO_WIDTH = 640;
    public static final int MAIN_LOGO_HEIGHT = 280;

    public static final int BACK_BUTTON_WIDTH = 96;
    public static final int BACK_BUTTON_HEIGHT = 36;
    public static final int MENU_BUTTON_WIDTH = 300;
    public static final int MENU_BUTTON_HEIGHT = 96;
    public static final int PRIMARY_BUTTON_WIDTH = 220;
    public static final int PRIMARY_BUTTON_HEIGHT = 46;
    public static final int SECONDARY_BUTTON_WIDTH = 280;
    public static final int SECONDARY_BUTTON_HEIGHT = 46;
    public static final int NOTE_ACTION_BUTTON_WIDTH = 180;
    public static final int NOTE_ACTION_BUTTON_HEIGHT = 44;
    public static final int NOTE_SAVE_BUTTON_WIDTH = 140;
    public static final int NOTE_SAVE_BUTTON_HEIGHT = 42;
    public static final int RATING_BUTTON_SIZE = 40;

    private ThemeSizes() {
    }

    public static Dimension scaledDimension(int width, int height) {
        return ScreenScale.dimension(width, height);
    }

    public static Dimension scaledWhiskyThumb() {
        return scaledDimension(WHISKY_THUMB_WIDTH, WHISKY_THUMB_HEIGHT);
    }

    public static Dimension scaledTastingNoteThumb() {
        return scaledDimension(TASTING_NOTE_THUMB_WIDTH, TASTING_NOTE_THUMB_HEIGHT);
    }

    public static Dimension scaledWhiskyDetailImage() {
        return scaledDimension(WHISKY_DETAIL_IMAGE_WIDTH, WHISKY_DETAIL_IMAGE_HEIGHT);
    }

    public static Dimension scaledTastingNoteDetailImage() {
        return scaledDimension(TASTING_NOTE_DETAIL_IMAGE_WIDTH, TASTING_NOTE_DETAIL_IMAGE_HEIGHT);
    }

    public static Dimension scaledTastingNotePreview() {
        return scaledDimension(TASTING_NOTE_PREVIEW_WIDTH, TASTING_NOTE_PREVIEW_HEIGHT);
    }

    public static Dimension scaledMainLogo() {
        return scaledDimension(MAIN_LOGO_WIDTH, MAIN_LOGO_HEIGHT);
    }

    public static Dimension scaledBackButton() {
        return scaledDimension(BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
    }

    public static Dimension scaledMenuButton() {
        return scaledDimension(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
    }
}
