package RDS.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import RDS.classes.RestaurantManager;
import RDS.classes.Food;
import RDS.utils.SocketWrapper;
import RDS.utils.ConnectionMessage;
import RDS.utils.OrderRequest;

public class ServerApp {
    RestaurantManager restaurantManager;

    ServerSocket serverSocket;

    HashMap<String, String> restaurantPasswords = new HashMap<>();

    Map<String, SocketWrapper> restaurantSockets = Collections.synchronizedMap(new HashMap<>());
    Map<String, SocketWrapper> customerSockets = Collections.synchronizedMap(new HashMap<>());

    HashMap<String, HashMap<Food, Integer>> allRestaurantOrders = new HashMap<>();

    ServerApp() {
        try {
            restaurantManager = DatabaseLoader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            loadUserPassword();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        try {
            DatabaseLoader.updateDatabaseFile(restaurantManager);
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            serverSocket = new ServerSocket(4000);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void serve(Socket clientSocket) throws IOException, ClassNotFoundException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        ConnectionMessage connectionMessage = (ConnectionMessage) socketWrapper.read();

        switch (connectionMessage.getType()) {
            case "restaurant": {
                String restName = connectionMessage.getName();
                if (!checkPassword(connectionMessage)) {
                    System.out.println(restName);
                    socketWrapper.write("Invalid Credentials");
                    socketWrapper.closeConnection();
                    return;
                }

                SocketWrapper restSocketWrapper = restaurantSockets.get(restName);
                if (restSocketWrapper != null) {
                    socketWrapper.write("Restaurant Already Connected");
                    return;
                }

                restaurantSockets.put(restName, socketWrapper);
                socketWrapper.write(restaurantManager.getRestaurant(restName));
                System.out.println(restName + " Connected");
                sendRestaurantOrder(restName);
                new RestaurantHandleThread(this, socketWrapper, restName);
                break;
            }

            case "customer":
                String customerName = connectionMessage.getName();

                SocketWrapper custSocketWrapper = customerSockets.get(customerName);
                if (custSocketWrapper != null) {
                    socketWrapper.write("Customer Already Connected");
                    return;
                }

                customerSockets.put(customerName, socketWrapper);
                socketWrapper.write("connected");
                System.out.println(customerName + " Connected");
                new CustomerHandleThread(this, socketWrapper, customerName);

                break;

            default:
                break;
        }

    }

    void loadUserPassword() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(new File("restaurantPassword.txt")));

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] userPass = line.split(",");

            restaurantPasswords.put(userPass[0], userPass[1]);

        }

        System.out.println(restaurantPasswords);
    }

    boolean checkPassword(ConnectionMessage connectionMessage) {

        if (!restaurantPasswords.containsKey(connectionMessage.getName())) {
            return false;
        }

        String hashed = Integer.toString(Objects.hash(connectionMessage.getPassword()));

        return restaurantPasswords.get(connectionMessage.getName()).equals(hashed);
        
    }

    void sendRestaurantOrder(String restName) {
        SocketWrapper restSocketWrapper = restaurantSockets.get(restName);
        if (restSocketWrapper == null || restSocketWrapper.isClosed()) {
            return;
        }

        HashMap<Food, Integer> foodNames = allRestaurantOrders.get(restName);

        try {
            restSocketWrapper.write(new OrderRequest(restName, foodNames));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new ServerApp();
    }
}
