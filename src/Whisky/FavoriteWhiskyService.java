package Whisky;

public final class FavoriteWhiskyService {
    public enum AddResult {
        ADDED,
        EMPTY_NAME,
        ALREADY_EXISTS,
        FAILED
    }

    public AddResult addFavorite(String whiskyName) {
        if (whiskyName == null || whiskyName.isBlank()) {
            return AddResult.EMPTY_NAME;
        }

        if (FavoriteWhiskyStore.isFavorite(whiskyName)) {
            return AddResult.ALREADY_EXISTS;
        }

        if (FavoriteWhiskyStore.addFavorite(whiskyName)) {
            return AddResult.ADDED;
        }

        return AddResult.FAILED;
    }
}
