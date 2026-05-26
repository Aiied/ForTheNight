package Whisky;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class FavoriteWhiskyStore {
    private static final String FAVORITE_FILE_PATH = "src/assets/Flie(txt)/favoriteWhiskies.txt";

    private FavoriteWhiskyStore() {
    }

    public static synchronized boolean isFavorite(String whiskyName) {
        return loadFavorites().contains(whiskyName);
    }

    public static synchronized boolean toggleFavorite(String whiskyName) {
        LinkedHashSet<String> favorites = loadFavorites();
        boolean nowFavorite;

        if (favorites.contains(whiskyName)) {
            favorites.remove(whiskyName);
            nowFavorite = false;
        } else {
            favorites.add(whiskyName);
            nowFavorite = true;
        }

        saveFavorites(favorites);
        return nowFavorite;
    }

    private static LinkedHashSet<String> loadFavorites() {
        LinkedHashSet<String> favorites = new LinkedHashSet<>();
        Path path = Path.of(FAVORITE_FILE_PATH);

        if (!Files.exists(path)) {
            return favorites;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isBlank()) {
                    favorites.add(trimmed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return favorites;
    }

    private static void saveFavorites(Set<String> favorites) {
        File file = new File(FAVORITE_FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String favorite : favorites) {
                writer.write(favorite);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
