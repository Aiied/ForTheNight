package Whisky;

import java.util.ArrayList;

public class Whisky {
    String name;
    String country;
    String whiskyType;
    String distillery;
    String cask;
    String imagePath;
    float abv;
    ArrayList<String> aroma;
    ArrayList<String> taste;
    ArrayList<String> finish;

    public Whisky(
            String name,
            String country,
            String whiskyType,
            String distillery,
            String cask,
            String imagePath,
            float abv,
            ArrayList<String> aroma,
            ArrayList<String> taste,
            ArrayList<String> finish
    ) {

        this.name = name;
        this.country = country;
        this.whiskyType = whiskyType;
        this.distillery = distillery;
        this.cask = cask;
        this.imagePath = imagePath;
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

    public String getDistillery() {
        return distillery;
    }

    public String getCask() {
        return cask;
    }

    public float getAbv() {
        return abv;
    }

    public String getImagePath() {
        return imagePath;
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
