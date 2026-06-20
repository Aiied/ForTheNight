package Whisky;

import Ui.util.AppPaths;

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
    private static LinkedHashSet<String> cachedFavorites;

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

    public static synchronized boolean addFavorite(String whiskyName) {
        String normalizedName = normalizeName(whiskyName);
        if (normalizedName == null) {
            return false;
        }

        LinkedHashSet<String> favorites = loadFavorites();
        if (favorites.contains(normalizedName)) {
            return false;
        }

        favorites.add(normalizedName);
        saveFavorites(favorites);
        return true;
    }

    public static synchronized LinkedHashSet<String> getFavorites() {
        return new LinkedHashSet<>(loadFavorites());
    }

    private static LinkedHashSet<String> loadFavorites() {
        if (cachedFavorites != null) {
            return new LinkedHashSet<>(cachedFavorites);
        }

        LinkedHashSet<String> favorites = new LinkedHashSet<>();
        Path path = Path.of(AppPaths.FAVORITE_WHISKIES_FILE);

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

        cachedFavorites = new LinkedHashSet<>(favorites);
        return favorites;
    }

    private static void saveFavorites(Set<String> favorites) {
        File file = new File(AppPaths.FAVORITE_WHISKIES_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String favorite : favorites) {
                writer.write(favorite);
                writer.newLine();
            }
            cachedFavorites = new LinkedHashSet<>(favorites);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String normalizeName(String whiskyName) {
        if (whiskyName == null) {
            return null;
        }

        String normalizedName = whiskyName.trim();
        if (normalizedName.isBlank()) {
            return null;
        }
        return normalizedName;
    }
}
