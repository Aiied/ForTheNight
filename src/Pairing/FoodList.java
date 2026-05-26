package Pairing;

import Ui.BaseList;

import java.util.List;

public class FoodList extends BaseList<Food> {

    public FoodList() {
        super();
    }

    public FoodList(String filePath) {
        super(filePath);
    }

    public List<Food> getFoods() {
        return getItems();
    }

    @Override
    protected Food parseLine(String line) {
        String[] values = line.split("\\|");

        return new Food(
                values[0],
                parseNotes(values[1])
        );
    }
}
