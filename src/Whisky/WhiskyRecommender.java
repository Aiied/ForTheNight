package Whisky;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class WhiskyRecommender {
    private static final int MIN_RECOMMENDATIONS = 2;
    private static final int MAX_RECOMMENDATIONS = 5;

    private WhiskyRecommender() {
    }

    public static List<Whisky> recommend(List<Whisky> allWhiskies, Set<String> favoriteNames) {
        if (allWhiskies == null || allWhiskies.isEmpty() || favoriteNames == null || favoriteNames.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<String> favoriteSet = new LinkedHashSet<>();
        for (String favoriteName : favoriteNames) {
            if (favoriteName != null && !favoriteName.isBlank()) {
                favoriteSet.add(favoriteName);
            }
        }

        if (favoriteSet.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<String> favoriteAromas = new LinkedHashSet<>();
        LinkedHashSet<String> favoriteTastes = new LinkedHashSet<>();
        LinkedHashSet<String> favoriteFinishes = new LinkedHashSet<>();

        for (Whisky whisky : allWhiskies) {
            if (!favoriteSet.contains(whisky.getName())) {
                continue;
            }

            addNotes(favoriteAromas, whisky.getAroma());
            addNotes(favoriteTastes, whisky.getTaste());
            addNotes(favoriteFinishes, whisky.getFinish());
        }

        ArrayList<Candidate> allCandidates = new ArrayList<>();
        for (Whisky whisky : allWhiskies) {
            if (favoriteSet.contains(whisky.getName())) {
                continue;
            }

            int aromaMatches = countMatches(whisky.getAroma(), favoriteAromas);
            int tasteMatches = countMatches(whisky.getTaste(), favoriteTastes);
            int finishMatches = countMatches(whisky.getFinish(), favoriteFinishes);

            if (aromaMatches == 0 && tasteMatches == 0 && finishMatches == 0) {
                continue;
            }

            allCandidates.add(new Candidate(whisky, aromaMatches, tasteMatches, finishMatches));
        }

        Comparator<Candidate> comparator = Comparator
                .comparingInt(Candidate::getAromaMatches).reversed()
                .thenComparingInt(Candidate::getTasteMatches).reversed()
                .thenComparingInt(Candidate::getFinishMatches).reversed()
                .thenComparing(candidate -> candidate.whisky.getName(), String.CASE_INSENSITIVE_ORDER);

        LinkedHashSet<Candidate> selected = new LinkedHashSet<>();
        addMatchesForStage(selected, allCandidates, comparator, 1);
        if (selected.size() < MIN_RECOMMENDATIONS) {
            addMatchesForStage(selected, allCandidates, comparator, 2);
        }
        if (selected.size() < MIN_RECOMMENDATIONS) {
            addMatchesForStage(selected, allCandidates, comparator, 3);
        }

        ArrayList<Candidate> sorted = new ArrayList<>(selected);
        sorted.sort(comparator);

        ArrayList<Whisky> recommendations = new ArrayList<>();
        for (Candidate candidate : sorted) {
            if (recommendations.size() >= MAX_RECOMMENDATIONS) {
                break;
            }
            recommendations.add(candidate.whisky);
        }

        if (recommendations.size() < MIN_RECOMMENDATIONS) {
            return List.of();
        }

        return recommendations;
    }

    private static void addMatchesForStage(
            Set<Candidate> selected,
            List<Candidate> candidates,
            Comparator<Candidate> comparator,
            int stage
    ) {
        ArrayList<Candidate> stageMatches = new ArrayList<>();
        for (Candidate candidate : candidates) {
            if (matchesStage(candidate, stage)) {
                stageMatches.add(candidate);
            }
        }

        stageMatches.sort(comparator);
        for (Candidate candidate : stageMatches) {
            selected.add(candidate);
        }
    }

    private static boolean matchesStage(Candidate candidate, int stage) {
        if (stage <= 1) {
            return candidate.aromaMatches > 0;
        }
        if (stage == 2) {
            return candidate.aromaMatches > 0 || candidate.tasteMatches > 0;
        }
        return candidate.aromaMatches > 0 || candidate.tasteMatches > 0 || candidate.finishMatches > 0;
    }

    private static void addNotes(Set<String> target, List<String> notes) {
        for (String note : notes) {
            String normalized = normalizeNote(note);
            if (!normalized.isBlank()) {
                target.add(normalized);
            }
        }
    }

    private static int countMatches(List<String> notes, Set<String> targets) {
        int matches = 0;
        LinkedHashSet<String> seen = new LinkedHashSet<>();
        for (String note : notes) {
            String normalized = normalizeNote(note);
            if (normalized.isBlank() || !seen.add(normalized)) {
                continue;
            }
            if (targets.contains(normalized)) {
                matches++;
            }
        }
        return matches;
    }

    private static String normalizeNote(String note) {
        if (note == null) {
            return "";
        }
        return note.trim().toLowerCase();
    }

    private static final class Candidate {
        private final Whisky whisky;
        private final int aromaMatches;
        private final int tasteMatches;
        private final int finishMatches;

        private Candidate(Whisky whisky, int aromaMatches, int tasteMatches, int finishMatches) {
            this.whisky = whisky;
            this.aromaMatches = aromaMatches;
            this.tasteMatches = tasteMatches;
            this.finishMatches = finishMatches;
        }

        private int getAromaMatches() {
            return aromaMatches;
        }

        private int getTasteMatches() {
            return tasteMatches;
        }

        private int getFinishMatches() {
            return finishMatches;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Candidate other)) {
                return false;
            }
            return whisky.getName().equals(other.whisky.getName());
        }

        @Override
        public int hashCode() {
            return whisky.getName().hashCode();
        }
    }
}
