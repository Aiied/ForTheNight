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

    public TastingNote(
            String whiskyName,
            String date,
            ArrayList<String> aroma,
            ArrayList<String> taste,
            ArrayList<String> finish,
            int score,
            String detailReview
    ) {
        this.whiskyName = whiskyName;
        this.date = date;
        this.aroma = aroma;
        this.taste = taste;
        this.finish = finish;
        this.score = score;
        this.detailReview = detailReview;
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
}
