package Search;

import Whisky.Whisky;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Filter {
    public static final String ALL_OPTION = "All";
    private static final String COUNTRY_KEY = "country";
    private static final String TYPE_KEY = "type";
    private static final String AROMA_KEY = "aroma";
    private static final String TASTE_KEY = "taste";
    private static final String FINISH_KEY = "finish";

    private final ArrayList<Whisky> allWhiskies;
    private final Map<String, List<String>> optionMap = new LinkedHashMap<>();

    public Filter(List<Whisky> whiskies, String optionFilePath) {
        this.allWhiskies = new ArrayList<>(whiskies);
        loadOptions(optionFilePath);
    }

    public List<String> buildCountryOptions() {
        return getOptions(COUNTRY_KEY);
    }

    public List<String> buildTypeOptions() {
        return getOptions(TYPE_KEY);
    }

    public List<String> buildAromaOptions() {
        return getOptions(AROMA_KEY);
    }

    public List<String> buildTasteOptions() {
        return getOptions(TASTE_KEY);
    }

    public List<String> buildFinishOptions() {
        return getOptions(FINISH_KEY);
    }

    public ArrayList<Whisky> apply(
            String nameKeyword,
            String selectedCountry,
            String selectedType,
            String selectedAroma,
            String selectedTaste,
            String selectedFinish
    ) {
        FilterSelection selection = new FilterSelection(
                normalizeKeyword(nameKeyword),
                selectedCountry,
                selectedType,
                selectedAroma,
                selectedTaste,
                selectedFinish
        );
        ArrayList<Whisky> filtered = new ArrayList<>();

        for (Whisky whisky : allWhiskies) {
            if (matches(whisky, selection)) {
                filtered.add(whisky);
            }
        }

        return filtered;
    }

    private boolean matches(Whisky whisky, FilterSelection selection) {
        return matchesName(whisky, selection.nameKeyword())
                && matchesExact(whisky.getCountry(), selection.selectedCountry())
                && matchesExact(whisky.getWhiskyType(), selection.selectedType())
                && matchesNote(whisky.getAroma(), selection.selectedAroma())
                && matchesNote(whisky.getTaste(), selection.selectedTaste())
                && matchesNote(whisky.getFinish(), selection.selectedFinish());
    }

    private String normalizeKeyword(String keyword) {
        return keyword == null ? "" : keyword.trim().toLowerCase();
    }

    private boolean matchesName(Whisky whisky, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        return whisky.getName() != null && whisky.getName().toLowerCase().contains(keyword);
    }

    private boolean matchesExact(String value, String selected) {
        if (selected == null || ALL_OPTION.equals(selected)) {
            return true;
        }
        return selected.equals(value);
    }

    private boolean matchesNote(List<String> notes, String selected) {
        if (selected == null || ALL_OPTION.equals(selected)) {
            return true;
        }
        for (String note : notes) {
            if (selected.equals(note.trim())) {
                return true;
            }
        }
        return false;
    }

    private void loadOptions(String optionFilePath) {
        if (optionFilePath == null || optionFilePath.isBlank()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(optionFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isBlank() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmed.split("=", 2);
                if (parts.length < 2) {
                    continue;
                }

                String key = parts[0].trim().toLowerCase();
                TreeSet<String> values = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                for (String value : parts[1].split(",")) {
                    String normalized = value.trim();
                    if (!normalized.isBlank()) {
                        values.add(normalized);
                    }
                }

                ArrayList<String> options = new ArrayList<>();
                options.add(ALL_OPTION);
                options.addAll(values);
                optionMap.put(key, options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getOptions(String key) {
        List<String> loaded = optionMap.get(key);
        if (loaded == null || loaded.isEmpty()) {
            return List.of(ALL_OPTION);
        }
        return loaded;
    }

    private record FilterSelection(
            String nameKeyword,
            String selectedCountry,
            String selectedType,
            String selectedAroma,
            String selectedTaste,
            String selectedFinish
    ) {
    }
}
