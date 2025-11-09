package RDS.utils;

import java.io.Serializable;

import RDS.classes.DoubleRange;

public class FoodSearchRequest implements Serializable {
    String restName;
    String foodName;
    DoubleRange priceRange;
    String category;

    public FoodSearchRequest(String restName, String foodName, DoubleRange priceRange, String category) {
        this.restName = restName;
        this.foodName = foodName;
        this.priceRange = priceRange;
        this.category = category;
    }

    public String getRestName() {
        return this.restName;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public DoubleRange getPriceRange() {
        return this.priceRange;
    }

    public String getCategory() {
        return this.category;
    }

}
