package TastingNote;

import java.util.ArrayList;

public class TastingNote {
    String whiskyName;
    String date;
    ArrayList<String> aroma;
    ArrayList<String> taste;
    ArrayList<String> finish;
    int score;
    String detailReview;
    String imagePath;

    public TastingNote(
            String whiskyName,
            String date,
            ArrayList<String> aroma,
            ArrayList<String> taste,
            ArrayList<String> finish,
            int score,
            String detailReview,
            String imagePath
    ) {
        this.whiskyName = whiskyName;
        this.date = date;
        this.aroma = aroma;
        this.taste = taste;
        this.finish = finish;
        this.score = score;
        this.detailReview = detailReview;
        this.imagePath = imagePath;
    }

    public String getWhiskyName() {
        return whiskyName;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getAroma() {
        return aroma;
    }

    public ArrayList<String> getTaste() {
        return taste;
    }

    public ArrayList<String> getFinish() {
        return finish;
    }

    public int getScore() {
        return score;
    }

    public String getDetailReview() {
        return detailReview;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String toStorageLine() {
        return String.join(
                "|",
                safe(whiskyName),
                safe(date),
                joinNotes(aroma),
                joinNotes(taste),
                joinNotes(finish),
                Integer.toString(score),
                safe(detailReview),
                safe(imagePath)
        );
    }

    private String joinNotes(ArrayList<String> notes) {
        ArrayList<String> filtered = new ArrayList<>();
        for (String note : notes) {
            if (note != null && !note.isBlank()) {
                filtered.add(note.trim());
            }
        }
        return String.join(", ", filtered);
    }

    private String safe(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\r", " ").replace("\n", " ").trim();
    }
}
