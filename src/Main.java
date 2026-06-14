import InitialSetup.favorite.FavoriteWhiskyPage;
import Ui.util.AppPaths;

import javax.swing.*;
import java.io.File;

public class    Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            File favoriteFile = new File(AppPaths.FAVORITE_WHISKIES_FILE);
            if (!favoriteFile.exists()) {
                new FavoriteWhiskyPage();
                return;
            }

            new MainPage();
        });
    }
}
