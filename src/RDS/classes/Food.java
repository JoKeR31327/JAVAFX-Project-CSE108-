package RDS.classes;

import java.io.Serializable;
import java.util.Objects;

public class Food implements Serializable {
    private String category;
    private String name;
    private double price;

    public Food(String category, String name, double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Food(Food F) {
        this.category = F.category;
        this.name = F.name;
        this.price = F.price;
    }

    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean nameContains(String keyString) {
        return name.toLowerCase().contains(keyString.toLowerCase());
    }

    public boolean hasCategory(String category) {
        return this.category.equalsIgnoreCase(category);
    }

    public boolean containsCategory(String category) {
        return this.category.toLowerCase().contains(category.toLowerCase());
    }

    public boolean priceBetween(DoubleRange range) {
        return (price >= range.lower && price <= range.upper);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Food)) {
            return false;
        }
        Food food = (Food) o;
        return Objects.equals(category, food.category) && Objects.equals(name, food.name) && price == food.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, name, price);
    }

    @Override
    public String toString() {
        return "\tName: " + getName() + "\n" +
                "\tPrice: " + getPrice() + "\n" +
                "\tCategory: " + getCategory() + "\n";
    }

}
