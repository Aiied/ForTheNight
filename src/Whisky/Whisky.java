package Whisky;

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

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getWhiskyType() {
        return whiskyType;
    }

    public int getAbv() {
        return abv;
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
}
