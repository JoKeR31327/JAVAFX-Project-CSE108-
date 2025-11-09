package RDS;

import java.io.File;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * RestaurantApp
 */
public class RestaurantApp {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new FileReader(new File("restaurant.txt")));
        sc.useDelimiter(",");

        while (sc.hasNext()) {
            sc.next();
            String name = sc.next();
            System.out.println(name + "," + Objects.hash(name));
            sc.nextLine();
        }
    }
}