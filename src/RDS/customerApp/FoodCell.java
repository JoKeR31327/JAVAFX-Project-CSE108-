package RDS.customerApp;

import RDS.classes.Food;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class FoodCell {
    private int no;
    private SimpleStringProperty name;
    private SimpleStringProperty restName;
    private SimpleStringProperty category;
    private SimpleStringProperty price;
    private int count;
    private Button button1;
    private Button button2;

    public FoodCell(int no, String restName, Food food) {
        this.no = no;
        this.restName = new SimpleStringProperty(restName);
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

    public String getRestName() {
        return restName.get();
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

    public Button getButton1() {
        return button1;
    }

    public Button getButton2() {
        return button2;
    }

    public void setButton1(Button button1) {
        this.button1 = button1;
    }

    public void setButton2(Button button2) {
        this.button2 = button2;
    }

}
