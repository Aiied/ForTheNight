import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WhiskyList {
    private final ArrayList<Whisky> whiskies = new ArrayList<>();

    public WhiskyList() {
    }

    public WhiskyList(String filePath) {
        loadFromFile(filePath);
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                whiskies.add(parseWhisky(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Whisky whisky) {
        whiskies.add(whisky);
    }

    public List<Whisky> getWhiskies() {
        return Collections.unmodifiableList(whiskies);
    }

    private Whisky parseWhisky(String line) {
        String[] values = line.split("\\|");

        return new Whisky(
                values[0],
                values[1],
                values[2],
                Integer.parseInt(values[3]),
                parseNotes(values[4]),
                parseNotes(values[5]),
                parseNotes(values[6])
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
