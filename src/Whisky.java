import java.util.ArrayList;

public class Whisky {
    String name;
    String country;
    String whiskyType;
    int abv;
    ArrayList<String> aroma;
    ArrayList<String> taste;
    ArrayList<String> finish;

    public Whisky(
            String name,
            String country,
            String whiskyType,
            int abv,
            ArrayList<String> aroma,
            ArrayList<String> taste,
            ArrayList<String> finish
    ) {

        this.name = name;
        this.country = country;
        this.whiskyType = whiskyType;
        this.abv = abv;
        this.aroma = aroma;
        this.finish = finish;
        this.taste = taste;
    }
}
