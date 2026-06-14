package InitialSetup.favorite;

import Whisky.Whisky;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class FavoriteWhiskySelectionModel {
    private final LinkedHashSet<Whisky> selectedWhiskies = new LinkedHashSet<>();

    public boolean isSelected(Whisky whisky) {
        return selectedWhiskies.contains(whisky);
    }

    public boolean toggle(Whisky whisky) {
        if (selectedWhiskies.contains(whisky)) {
            selectedWhiskies.remove(whisky);
            return false;
        }

        selectedWhiskies.add(whisky);
        return true;
    }

    public boolean hasMinimumSelection(int minimumCount) {
        return selectedWhiskies.size() >= minimumCount;
    }

    public Set<String> getSelectedNamesInDisplayOrder(List<Whisky> whiskies) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (Whisky whisky : whiskies) {
            if (selectedWhiskies.contains(whisky)) {
                names.add(whisky.getName());
            }
        }
        return names;
    }
}
