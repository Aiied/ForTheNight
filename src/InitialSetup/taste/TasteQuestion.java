package InitialSetup.taste;

import java.util.Collections;
import java.util.List;

public class TasteQuestion {
    private final String id;
    private final String question;
    private final String leftLabel;
    private final String rightLabel;
    private final List<String> targets;

    public TasteQuestion(String id, String question, String leftLabel, String rightLabel, List<String> targets) {
        this.id = id;
        this.question = question;
        this.leftLabel = leftLabel;
        this.rightLabel = rightLabel;
        this.targets = List.copyOf(targets);
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getLeftLabel() {
        return leftLabel;
    }

    public String getRightLabel() {
        return rightLabel;
    }

    public List<String> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
