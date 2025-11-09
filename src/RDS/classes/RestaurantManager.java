package RDS.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantManager {
    private List<Restaurant> restaurants;
    private HashMap<String, Integer> restNameToInd;
    private HashMap<Integer, Integer> restIdToInd;

    public SearchRestaurant searchRestaurant;
    public SearchMenu searchMenu;

    public RestaurantManager() {
        restaurants = new ArrayList<>();
        restNameToInd = new HashMap<>();
        restIdToInd = new HashMap<>();

        searchRestaurant = new SearchRestaurant();
        searchMenu = new SearchMenu();
    }

    public class SearchRestaurant {
        public List<Restaurant> byName(String name) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.nameContains(name)) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public List<Restaurant> byScore(double lower, double upper) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.scoreBetween(new DoubleRange(lower, upper))) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public List<Restaurant> byCategory(String category) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.containsCategory(category)) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public List<Restaurant> byPrice(String price) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.matchPrice(price)) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public List<Restaurant> byZipCode(String zipCode) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.getZipCode().equals(zipCode)) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public List<Restaurant> byAll(String name, DoubleRange scoreRange, String price, String category,
                String zipCode) {
            List<Restaurant> result = new ArrayList<>();

            for (var restaurant : restaurants) {
                if (restaurant.nameContains(name)
                        && restaurant.scoreBetween(scoreRange)
                        && restaurant.matchPrice(price)
                        && restaurant.containsCategory(category)
                        && restaurant.zipCodeContains(zipCode)) {
                    result.add(new Restaurant(restaurant));
                }
            }

            return result;
        }

        public HashMap<String, List<String>> categoryWiseRestaurants() {
            HashMap<String, List<String>> map = new HashMap<>();

            for (var restaurant : restaurants) {
                List<String> categories = restaurant.getCategories();
                for (String category : categories) {
                    if (category == null) {
                        continue;
                    }

                    List<String> restaurantsInCatgory = map.get(category);

                    if (restaurantsInCatgory == null) {
                        restaurantsInCatgory = new ArrayList<>();
                    }
                    restaurantsInCatgory.add(restaurant.getName());

                    map.put(category, restaurantsInCatgory);
                }
            }

            return map;
        }
    }

    public class SearchMenu {
        public HashMap<String, List<Food>> byName(String foodName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                List<Food> foods = restaurant.foodsByName(foodName);
                if (foods.size() > 0) {
                    result.put(restaurant.getName(), foods);
                }
            }

            return result;
        }

        public HashMap<String, List<Food>> byNameInRestaurant(String foodName, String restaurantName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                if (restaurant.nameContains(restaurantName)) {

                    List<Food> foods = restaurant.foodsByName(foodName);
                    if (foods.size() > 0) {
                        result.put(restaurant.getName(), foods);
                    }

                }
            }

            return result;
        }

        public HashMap<String, List<Food>> byCategory(String category) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                List<Food> foods = restaurant.foodsByCategory(category);
                if (foods.size() > 0) {
                    result.put(restaurant.getName(), foods);
                }

            }

            return result;
        }

        public HashMap<String, List<Food>> byCategoryInRestaurant(String category, String restaurantName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                if (restaurant.nameContains(restaurantName)) {

                    List<Food> foods = restaurant.foodsByCategory(category);
                    if (foods.size() > 0) {
                        result.put(restaurant.getName(), foods);
                    }

                }
            }

            return result;
        }

        public HashMap<String, List<Food>> byPrice(double lower, double upper) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                List<Food> foods = restaurant.foodsByPrice(lower, upper);
                if (foods.size() > 0) {
                    result.put(restaurant.getName(), foods);
                }

            }

            return result;
        }

        public HashMap<String, List<Food>> byPriceInRestaurant(double lower, double upper, String restaurantName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                if (restaurant.nameContains(restaurantName)) {

                    List<Food> foods = restaurant.foodsByPrice(lower, upper);
                    if (foods.size() > 0) {
                        result.put(restaurant.getName(), foods);
                    }

                }
            }

            return result;
        }

        public HashMap<String, List<Food>> byAllInRestaurant(String foodName, DoubleRange priceRange, String category,
                String restaurantName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                if (restaurant.nameContains(restaurantName)) {

                    List<Food> foods = restaurant.byAll(foodName, priceRange, category);
                    if (foods.size() > 0) {
                        result.put(restaurant.getName(), foods);
                    }

                }
            }

            return result;
        }

        public HashMap<String, List<Food>> costliestInRestaurant(String restaurantName) {
            HashMap<String, List<Food>> result = new HashMap<>();

            for (Restaurant restaurant : restaurants) {

                if (restaurant.nameContains(restaurantName)) {

                    List<Food> foods = restaurant.costliestFoods();
                    if (foods.size() > 0) {
                        result.put(restaurant.getName(), foods);
                    }

                }
            }

            return result;
        }

        public HashMap<String, Integer> totalInEachRestaurant() {
            HashMap<String, Integer> map = new HashMap<>();

            for (Restaurant restaurant : restaurants) {
                String restName = restaurant.getName();
                map.putIfAbsent(restName, restaurant.foodCount());
            }

            return map;
        }
    }

    public List<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public HashMap<Integer, List<Food>> getAllFoods() {
        HashMap<Integer, List<Food>> allFoods = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            allFoods.put(restaurant.getId(), restaurant.getMenu());
        }
        return allFoods;
    }

    public Restaurant getRestaurant(String name) {
        int ind = restNameToInd.get(name);
        return new Restaurant(restaurants.get(ind));
    }

    public int getRestaurantCount() {
        return restaurants.size();
    }

    public int getFoodCount() {
        int count = 0;

        for (Restaurant restaurant : restaurants) {
            count += restaurant.foodCount();
        }

        return count;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);

        restNameToInd.put(restaurant.getName(), restaurants.size() - 1);
        restIdToInd.put(restaurant.getId(), restaurants.size() - 1);
    }

    public void addRestaurant(String name, double score, String price, String zipCode, List<String> categories) {
        int id = restaurants.isEmpty() ? 1 : restaurants.get(restaurants.size() - 1).getId() + 1;
        restaurants.add(new Restaurant(id, name, score, price, zipCode, categories));

        restNameToInd.put(name, restaurants.size() - 1);
        restIdToInd.put(id, restaurants.size() - 1);
    }

    public void addFoodItem(int restaurantId, Food food) {
        int ind = restIdToInd.get(restaurantId);
        Restaurant foodRestaurant = restaurants.get(ind);

        foodRestaurant.addFood(food);
    }

    public void addFoodItem(String restaurantName, String category, String name, double price) {
        Restaurant foodRestaurant = restaurants.get(0);

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                foodRestaurant = restaurant;
                break;
            }
        }

        foodRestaurant.addFood(new Food(category, name, price));
    }

    public boolean restaurantExists(String name) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean foodItemExists(String restaurantName, String foodName, String category) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName) && restaurant.foodExists(foodName)) {
                return true;
            }
        }
        return false;
    }

}
