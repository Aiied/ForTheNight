package TastingNote;

import Ui.util.BaseList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TastingNoteList extends BaseList<TastingNote> {

    public TastingNoteList() {
        super();
    }

    public TastingNoteList(String filePath) {
        super(filePath);
    }

    public List<TastingNote> getTastingNotes() {
        return getItems();
    }

    public static void saveTastingNotes(String filePath, List<TastingNote> notes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (TastingNote note : notes) {
                writer.write(note.toStorageLine());
                writer.newLine();
            }
        }
    }

    public static List<TastingNote> replaceTastingNote(String filePath, int index, TastingNote updatedNote) throws IOException {
        ArrayList<TastingNote> notes = new ArrayList<>(new TastingNoteList(filePath).getTastingNotes());
        if (index < 0 || index >= notes.size()) {
            throw new IOException("Invalid tasting note index.");
        }

        notes.set(index, updatedNote);
        saveTastingNotes(filePath, notes);
        return notes;
    }

    @Override
    protected TastingNote parseLine(String line) {
        String[] values = line.split("\\|", -1);
        if (values.length < 6) {
            return null;
        }

        return new TastingNote(
                values[0],
                values[1],
                parseNotes(values[2]),
                parseNotes(values[3]),
                parseNotes(values[4]),
                parseScore(values[5]),
                values.length > 6 ? values[6] : "",
                values.length > 7 ? values[7] : ""
        );
    }

    private int parseScore(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
