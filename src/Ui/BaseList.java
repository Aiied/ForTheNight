package Ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseList<T> {
    protected final ArrayList<T> items = new ArrayList<>();

    protected BaseList() {
    }

    protected BaseList(String filePath) {
        loadFromFile(filePath);
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                T item = parseLine(line);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T item) {
        items.add(item);
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    protected ArrayList<String> parseNotes(String text) {
        ArrayList<String> notes = new ArrayList<>();
        for (String note : text.split(",")) {
            notes.add(note.trim());
        }
        return notes;
    }

    protected abstract T parseLine(String line);
}
