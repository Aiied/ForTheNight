import InitialSetup.FavoriteWhiskyPage;

import javax.swing.*;
import java.io.File;

public class  Main {
    private static final String FAVORITE_FILE_PATH = "src/assets/Flie(txt)/favoriteWhiskies.txt";

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            File favoriteFile = new File(FAVORITE_FILE_PATH);
            if (favoriteFile.exists()) {
                new MainPage();
            } else {
                new FavoriteWhiskyPage();
            }
        });
    }
}
