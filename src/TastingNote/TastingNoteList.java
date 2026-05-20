package TastingNote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TastingNoteList {
    private final ArrayList<TastingNote> tastingNotes = new ArrayList<>();

    public TastingNoteList() {
    }

    public TastingNoteList(String filePath) {
        loadFromFile(filePath);
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                tastingNotes.add(parseTastingNote(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(TastingNote tastingNote) {
        tastingNotes.add(tastingNote);
    }

    public List<TastingNote> getTastingNotes() {
        return Collections.unmodifiableList(tastingNotes);
    }

    private TastingNote parseTastingNote(String line) {
        String[] values = line.split("\\|");

        return new TastingNote(
                values[0],
                values[1],
                parseNotes(values[2]),
                parseNotes(values[3]),
                parseNotes(values[4]),
                Integer.parseInt(values[5])
        );
    }

    private ArrayList<String> parseNotes(String text) {
        ArrayList<String> notes = new ArrayList<>();

        for (String note : text.split(",")) {
            notes.add(note.trim());
        }

        return notes;
    }
}
