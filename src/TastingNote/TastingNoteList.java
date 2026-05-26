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
}
