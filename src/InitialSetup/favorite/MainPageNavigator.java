package InitialSetup.favorite;

import javax.swing.JFrame;

public final class MainPageNavigator {
    private MainPageNavigator() {
    }

    public static void openMainPage() {
        try {
            Class<?> mainPageClass = Class.forName("MainPage");
            JFrame mainPage = (JFrame) mainPageClass.getDeclaredConstructor().newInstance();
            mainPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
