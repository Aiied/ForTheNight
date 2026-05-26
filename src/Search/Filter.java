package Search;

import Whisky.Whisky;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Filter {
    public static final String ALL_OPTION = "All";

    private final ArrayList<Whisky> allWhiskies;
    private final Map<String, List<String>> optionMap = new LinkedHashMap<>();

    public Filter(List<Whisky> whiskies, String optionFilePath) {
        this.allWhiskies = new ArrayList<>(whiskies);
        loadOptions(optionFilePath);
    }

    public List<String> buildCountryOptions() {
        return getOptions("country");
    }

    public List<String> buildTypeOptions() {
        return getOptions("type");
    }

    public List<String> buildAromaOptions() {
        return getOptions("aroma");
    }

    public List<String> buildTasteOptions() {
        return getOptions("taste");
    }

    public List<String> buildFinishOptions() {
        return getOptions("finish");
    }

    public ArrayList<Whisky> apply(
            String nameKeyword,
            String selectedCountry,
            String selectedType,
            String selectedAroma,
            String selectedTaste,
            String selectedFinish
    ) {
        String normalizedKeyword = nameKeyword == null ? "" : nameKeyword.trim().toLowerCase();
        ArrayList<Whisky> filtered = new ArrayList<>();

        for (Whisky whisky : allWhiskies) {
            if (!matchesName(whisky, normalizedKeyword)) {
                continue;
            }
            if (!matchesExact(whisky.getCountry(), selectedCountry)) {
                continue;
            }
            if (!matchesExact(whisky.getWhiskyType(), selectedType)) {
                continue;
            }
            if (!matchesNote(whisky.getAroma(), selectedAroma)) {
                continue;
            }
            if (!matchesNote(whisky.getTaste(), selectedTaste)) {
                continue;
            }
            if (!matchesNote(whisky.getFinish(), selectedFinish)) {
                continue;
            }
            filtered.add(whisky);
        }

        return filtered;
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

    private boolean matchesNote(ArrayList<String> notes, String selected) {
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
}
