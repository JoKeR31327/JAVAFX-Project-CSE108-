package RDS.classes;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Restaurant implements Serializable {
    private int id;
    private String name;
    private double score;
    private String price;
    private String zipCode;
    private List<String> categories;
    private List<Food> menu;

    public Restaurant(int id, String name, double score, String price, String zipCode, List<String> categories) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.price = price;
        this.zipCode = zipCode;
        this.categories = new ArrayList<>();
        this.menu = new ArrayList<>();

        if (categories.size() <= 3) {
            this.categories = new ArrayList<>(categories);
        } else {
            Iterator<String> iter = categories.iterator();
            for (int i = 0; i < 3; i++) {
                this.categories.add(iter.next());
            }
        }
    }

    public Restaurant(Restaurant R) {
        this.id = R.id;
        this.name = R.name;
        this.score = R.score;
        this.price = R.price;
        this.zipCode = R.zipCode;
        this.categories = new ArrayList<>(R.categories);
        this.menu = new ArrayList<>(R.menu);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getScore() {
        return this.score;
    }

    public String getPrice() {
        return this.price;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public List<String> getCategories() {
        return new ArrayList<>(this.categories);
    }

    public List<Food> getMenu() {
        return new ArrayList<Food>(menu);
    }

    public boolean nameContains(String keyString) {
        return name.toLowerCase().contains(keyString.toLowerCase());
    }

    public boolean containsCategory(String cat) {
        for (String category : categories) {

            if (category.toLowerCase().contains(cat.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public boolean scoreBetween(DoubleRange range) {
        return (score >= range.lower && score <= range.upper);
    }

    public boolean matchPrice(String price) {
        if (price.isBlank()) {
            return true;
        }

        return this.price.equals(price);
    }

    public boolean zipCodeContains(String keyString) {
        return zipCode.toLowerCase().contains(keyString.toLowerCase());
    }

    public boolean foodExists(String foodName) {
        for (Food food : menu) {
            if (food.getName().equalsIgnoreCase(foodName)) {
                return true;
            }
        }
        return false;
    }

    public void addFood(Food food) {
        menu.add(food);
    }

    public List<Food> foodsByName(String foodName) {
        List<Food> result = new ArrayList<>();

        for (var food : menu) {
            if (food.nameContains(foodName)) {
                result.add(new Food(food));
            }
        }

        return result;
    }

    public List<Food> foodsByCategory(String category) {
        List<Food> result = new ArrayList<>();

        for (var food : menu) {
            if (food.containsCategory(category)) {
                result.add(new Food(food));
            }
        }

        return result;
    }

    public List<Food> foodsByPrice(double lower, double upper) {
        List<Food> result = new ArrayList<>();

        for (var food : menu) {
            if (food.getPrice() >= lower && food.getPrice() <= upper) {
                result.add(new Food(food));
            }
        }

        return result;
    }

    public List<Food> byAll(String foodName, DoubleRange priceRange, String category) {
        List<Food> result = new ArrayList<>();

        for (var food : menu) {
            if (food.nameContains(foodName)
                    && food.priceBetween(priceRange)
                    && food.containsCategory(category)) {
                result.add(new Food(food));
            }
        }

        return result;
    }

    public List<Food> costliestFoods() {

        double highestPrice = 0;
        for (Food food : menu) {
            if (food.getPrice() > highestPrice) {
                highestPrice = food.getPrice();
            }
        }

        List<Food> result = new ArrayList<>();

        for (Food food : menu) {
            if (food.getPrice() == highestPrice) {
                result.add(new Food(food));
            }
        }

        return result;
    }

    public int foodCount() {
        return menu.size();
    }

    @Override
    public String toString() {
        return "\tName: " + name + "\n" +
                "\tScore: " + score + "\n" +
                "\tPrice: " + price + "\n" +
                "\tZip Code: " + zipCode + "\n" +
                "\tCategories: " + String.join(", ", categories) + "\n";
    }

}
