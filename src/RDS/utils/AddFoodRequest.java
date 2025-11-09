package RDS.utils;

import java.io.Serializable;

public class AddFoodRequest implements Serializable {
    String restaurantName;
    String foodName;
    String category;
    double price;

    public AddFoodRequest(String restaurantName, String foodName, String category, double price) {
        this.restaurantName = restaurantName;
        this.foodName = foodName;
        this.category = category;
        this.price = price;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public String getCategory() {
        return this.category;
    }

    public double getPrice() {
        return this.price;
    }

}
