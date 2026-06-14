package Whisky;

import Ui.util.BaseList;

import java.util.ArrayList;
import java.util.List;

public class WhiskyList extends BaseList<Whisky> {

    public WhiskyList() {
        super();
    }

    public WhiskyList(String filePath) {
        super(filePath);
    }

    public List<Whisky> getWhiskies() {
        return getItems();
    }

    @Override
    protected Whisky parseLine(String line) {
        String[] values = line.split("\\|", -1);

        if (values.length < 9) {
            System.out.println("Invalid whisky data: " + line);
            return null;
        }

        return new Whisky(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values.length > 9 ? values[9] : "",
                parseAbv(values[5]),
                parseNotes(values[6]),
                parseNotes(values[7]),
                parseNotes(values[8])
        );
    }

    private float parseAbv(String text) {
        if (text == null || text.isBlank()) {
            return 0.0f;
        }

        return Float.parseFloat(text);
    }

}
