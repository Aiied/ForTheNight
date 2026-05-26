package Pairing;

import java.util.ArrayList;

public class Food {
    String name;
    ArrayList<String> whiskyList;

    public Food(
            String name,
            ArrayList<String> whiskyList
    ) {
        this.name = name;
        this.whiskyList = whiskyList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getWhiskyList() {
        return whiskyList;
    }
}
