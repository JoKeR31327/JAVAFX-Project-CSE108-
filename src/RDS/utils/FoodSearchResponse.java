package RDS.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import RDS.classes.Food;

public class FoodSearchResponse implements Serializable {
    HashMap<String, List<Food>> result;

    public FoodSearchResponse(HashMap<String, List<Food>> result) {
        this.result = result;
    }

    public HashMap<String, List<Food>> getResult() {
        return this.result;
    }

}
