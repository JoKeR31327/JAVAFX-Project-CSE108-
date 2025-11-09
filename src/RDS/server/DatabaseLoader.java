package RDS.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import RDS.classes.Food;
import RDS.classes.Restaurant;
import RDS.classes.RestaurantManager;

class DatabaseLoadException extends Exception {

    public DatabaseLoadException(String s) {
        super(s);
    }
}

class DatabaseLoader {
    private static final String RESTAURANT_FILE_NAME = "restaurant.txt";
    private static final String MENU_FILE_NAME = "menu.txt";

    private static RestaurantManager restaurantManager;

    public static RestaurantManager load() throws DatabaseLoadException {

        restaurantManager = new RestaurantManager();

        try {
            parseRestaurantsFromFile(RESTAURANT_FILE_NAME);
            parseMenuFromFile(MENU_FILE_NAME);
        } catch (IOException e) {
            throw new DatabaseLoadException("Data Parsing Error.");
        }

        return restaurantManager;
    }

    private static void parseRestaurantsFromFile(String pathname) throws IOException {
        File restaurantFile = new File(pathname);

        if (!restaurantFile.exists()) {
            restaurantFile.createNewFile();
        }

        BufferedReader restaurantReader = new BufferedReader(new FileReader(restaurantFile));

        while (true) {
            String readLine = restaurantReader.readLine();

            if (readLine == null) {
                break;
            }

            parseRestaurantLine(readLine);
        }

        restaurantReader.close();

    }

    private static void parseRestaurantLine(String fileLine) {
        Scanner sc = new Scanner(fileLine);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String name = sc.next();
        double score = sc.nextDouble();
        String price = sc.next();
        String zipCode = sc.next();

        String[] categoryArr = sc.nextLine().substring(1).split(",");
        List<String> categories = new ArrayList<>(Arrays.asList(categoryArr));

        sc.close();

        restaurantManager.addRestaurant(new Restaurant(id, name, score, price, zipCode, categories));
    }

    private static void parseMenuFromFile(String pathname) throws IOException {
        File menuFile = new File(pathname);
        if (!menuFile.exists()) {
            menuFile.createNewFile();
        }

        BufferedReader menuReader = new BufferedReader(new FileReader(menuFile));

        while (true) {
            String readLine = menuReader.readLine();

            if (readLine == null) {
                break;
            }

            parseFoodLine(readLine);
        }

        menuReader.close();
    }

    private static void parseFoodLine(String fileLine) {
        Scanner sc = new Scanner(fileLine);
        sc.useDelimiter(",");

        int restaurantId = sc.nextInt();
        String category = sc.next();
        String name = sc.next();
        double price = sc.nextDouble();

        sc.close();

        restaurantManager.addFoodItem(restaurantId, new Food(category, name, price));
    }

    public static void updateDatabaseFile(RestaurantManager _restaurantManager) throws IOException {
        // Write restaurant file
        restaurantManager = _restaurantManager;

        BufferedWriter restaurantTxt = new BufferedWriter(new FileWriter(new File(RESTAURANT_FILE_NAME)));

        var resturants = restaurantManager.getRestaurants();
        var restaurantsIter = resturants.iterator();

        while (restaurantsIter.hasNext()) {
            Restaurant restaurant = restaurantsIter.next();
            restaurantTxt.write(formatRestaurantString(restaurant));
            if (restaurantsIter.hasNext()) {
                restaurantTxt.write(System.lineSeparator());
            }
        }
        restaurantTxt.close();

        // Write menu file
        BufferedWriter menuTxt = new BufferedWriter(new FileWriter(new File(MENU_FILE_NAME)));
        restaurantsIter = resturants.iterator();

        while (restaurantsIter.hasNext()) {
            Restaurant restaurant = restaurantsIter.next();
            var restMenuIter = restaurant.getMenu().iterator();

            while (restMenuIter.hasNext()) {
                menuTxt.write(restaurant.getId() + ",");
                menuTxt.write(formatFoodString(restMenuIter.next()));

                if (restMenuIter.hasNext()) {
                    menuTxt.write(System.lineSeparator());
                }
            }

            if (restaurantsIter.hasNext()) {
                menuTxt.write(System.lineSeparator());
            }
        }

        menuTxt.close();
    }

    private static String formatRestaurantString(Restaurant restaurant) {
        String categoryString = "";
        var categories = restaurant.getCategories();
        for (int i = 0; i < 2; i++) {
            if (i < categories.size()) {
                categoryString += categories.get(i);
            }
            categoryString += ',';
        }
        if (categories.size() == 3) {
            categoryString += categories.get(2);
        }

        String restaurantString = Long.toString(restaurant.getId()) + ',' +
                restaurant.getName() + ',' +
                restaurant.getScore() + ',' +
                restaurant.getPrice() + ',' +
                restaurant.getZipCode() + ',' +
                categoryString;

        return restaurantString;
    }

    private static String formatFoodString(Food food) {
        String foodString = food.getCategory() + ',' +
                food.getName() + ',' +
                Double.toString(food.getPrice());
        return foodString;
    }

}
