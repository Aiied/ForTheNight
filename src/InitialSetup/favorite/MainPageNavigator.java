package InitialSetup.favorite;

import Ui.text.AppStrings;

import javax.swing.JFrame;

public final class MainPageNavigator {
    private MainPageNavigator() {
    }

    public static void openMainPage() {
        try {
            Class<?> mainPageClass = Class.forName(AppStrings.MAIN_PAGE_CLASS);
            JFrame mainPage = (JFrame) mainPageClass.getDeclaredConstructor().newInstance();
            mainPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
