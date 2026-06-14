package Ui.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseList<T> {
    private static final int DEFAULT_TAB_SIZE = 5;

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

    public List<List<T>> getItemTabs() {
        return getItemTabs(DEFAULT_TAB_SIZE);
    }

    public List<List<T>> getItemTabs(int tabSize) {
        return partition(items, tabSize);
    }

    public static <E> List<List<E>> partition(List<E> source, int tabSize) {
        if (tabSize <= 0) {
            throw new IllegalArgumentException("tabSize must be greater than 0");
        }

        ArrayList<List<E>> tabs = new ArrayList<>();
        for (int start = 0; start < source.size(); start += tabSize) {
            int end = Math.min(start + tabSize, source.size());
            tabs.add(Collections.unmodifiableList(new ArrayList<>(source.subList(start, end))));
        }
        return Collections.unmodifiableList(tabs);
    }

    public List<T> getItemsInTab(int tabIndex) {
        return getItemsInTab(tabIndex, DEFAULT_TAB_SIZE);
    }

    public List<T> getItemsInTab(int tabIndex, int tabSize) {
        List<List<T>> tabs = getItemTabs(tabSize);
        if (tabIndex < 0 || tabIndex >= tabs.size()) {
            return Collections.emptyList();
        }
        return tabs.get(tabIndex);
    }

    public int getTabCount() {
        return getTabCount(DEFAULT_TAB_SIZE);
    }

    public int getTabCount(int tabSize) {
        if (tabSize <= 0) {
            throw new IllegalArgumentException("tabSize must be greater than 0");
        }
        return (int) Math.ceil((double) items.size() / tabSize);
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
