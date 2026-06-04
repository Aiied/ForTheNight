package TastingNote;

import Ui.BaseList;

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
                values.length > 6 ? values[6] : ""
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
