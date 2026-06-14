package InitialSetup.favorite;

import Ui.util.AppPaths;
import Whisky.Whisky;
import Whisky.WhiskyList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class FavoriteWhiskySetupService {
    public List<Whisky> loadWhiskies() {
        return new ArrayList<>(new WhiskyList(AppPaths.WHISKY_LIST_FILE).getWhiskies());
    }

    public void saveFavorites(Set<String> favoriteNames) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AppPaths.FAVORITE_WHISKIES_FILE))) {
            for (String favoriteName : favoriteNames) {
                writer.write(favoriteName);
                writer.newLine();
            }
        }
    }

    public void saveEmptyFavorites() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AppPaths.FAVORITE_WHISKIES_FILE))) {
            writer.write("");
        }
    }
}
