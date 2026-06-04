package InitialSetup;

public class TasteResultProfile {
    private final String key;
    private final String title;
    private final String description;
    private final String examples;

    public TasteResultProfile(String key, String title, String description, String examples) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.examples = examples;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExamples() {
        return examples;
    }
}
