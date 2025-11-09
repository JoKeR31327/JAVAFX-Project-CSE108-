package RDS.utils;

import java.io.Serializable;
import java.util.HashMap;
import RDS.classes.Food;

public class OrderRequest implements Serializable {
    String restaurantName;
    HashMap<Food, Integer> foodNames;

    public OrderRequest(String restaurantName, HashMap<Food, Integer> foodNames) {
        this.restaurantName = restaurantName;
        this.foodNames = foodNames;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public HashMap<Food, Integer> getFoodNames() {
        return this.foodNames;
    }

}
