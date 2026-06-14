package Pairing;

import Ui.util.BaseList;

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
        String[] values = line.split("\\|", -1);
        if (values.length < 7) {
            return null;
        }

        return new Food(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6]
        );
    }
}
