package RDS.restaurantApp;

import RDS.classes.Food;
import javafx.beans.property.SimpleStringProperty;

public class FoodCell {
    private int no;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleStringProperty price;
    private int count;

    public FoodCell(int no, Food food) {
        this.no = no;
        this.name = new SimpleStringProperty(food.getName());
        this.category = new SimpleStringProperty(food.getCategory());
        this.price = new SimpleStringProperty("$" + Double.toString(food.getPrice()));
        this.count = 0;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getPrice() {
        return price.get();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
