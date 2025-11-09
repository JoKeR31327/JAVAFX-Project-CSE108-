package RDS.customerApp;

import RDS.classes.Restaurant;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class RestaurantCell {
    private int no;
    private SimpleStringProperty name;
    private double score;
    private SimpleStringProperty price;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty categories;
    private Button button;

    public RestaurantCell(int count, Restaurant restaurant) {
        this.no = count;
        this.name = new SimpleStringProperty(restaurant.getName());
        this.score = restaurant.getScore();
        this.price = new SimpleStringProperty(restaurant.getPrice());
        this.zipCode = new SimpleStringProperty(restaurant.getZipCode());
        this.categories = new SimpleStringProperty(String.join(", ", restaurant.getCategories()));
        this.button = new Button();
        button.setText("Order");
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name.get();
    }

    public double getScore() {
        return score;
    }

    public String getPrice() {
        return price.get();
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public String getCategories() {
        return categories.get();
    }

    public Button getButton() {
        return button;
    }
}
