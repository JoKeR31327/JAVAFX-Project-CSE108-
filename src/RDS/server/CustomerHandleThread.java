package RDS.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import RDS.classes.Food;
import RDS.classes.Restaurant;
import RDS.utils.FoodSearchRequest;
import RDS.utils.FoodSearchResponse;
import RDS.utils.OrderRequest;
import RDS.utils.RestSearchRequest;
import RDS.utils.RestSearchResponse;
import RDS.utils.SocketWrapper;

public class CustomerHandleThread implements Runnable {
    ServerApp server;
    Thread thr;
    SocketWrapper socketWrapper;
    String customerName;

    CustomerHandleThread(ServerApp server, SocketWrapper socketWrapper, String customerName) {
        this.server = server;
        this.socketWrapper = socketWrapper;
        this.customerName = customerName;

        this.thr = new Thread(this);
        thr.start();
    }

    void handleResponse(Object response) {

        if (response instanceof RestSearchRequest) {
            System.out.println("Rest Search Request Received");
            handleRestSearch((RestSearchRequest) response);
            return;
        }

        if (response instanceof FoodSearchRequest) {
            System.out.println("Food Search Request Received");
            handleFoodSearch((FoodSearchRequest) response);
            return;
        }

        if (response instanceof OrderRequest) {
            System.out.println("Order Placed");
            handleOrder((OrderRequest) response);
            return;
        }

        if (response instanceof String) {
            if (((String) response).equals("disconnect")) {
                thr.interrupt();
                return;
            }
        }
    }

    void handleOrder(OrderRequest orderRequest) {

        String restName = orderRequest.getRestaurantName();
        HashMap<Food, Integer> foodNames = orderRequest.getFoodNames();

        HashMap<Food, Integer> serverOrders = server.allRestaurantOrders.get(restName);

        for (var order : foodNames.entrySet()) {
            serverOrders.merge(order.getKey(), order.getValue(), Integer::sum);
        }

        server.sendRestaurantOrder(restName);
    }

    void handleRestSearch(RestSearchRequest request) {
        List<Restaurant> result = server.restaurantManager.searchRestaurant.byAll(request.getName(),
                request.getScoreRange(), request.getPrice(), request.getCategory(), request.getZipCode());

        try {
            socketWrapper.write(new RestSearchResponse(result));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void handleFoodSearch(FoodSearchRequest request) {
        HashMap<String, List<Food>> result = server.restaurantManager.searchMenu.byAllInRestaurant(
                request.getFoodName(), request.getPriceRange(), request.getCategory(), request.getRestName());

        try {
            socketWrapper.write(new FoodSearchResponse(result));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        System.out.println(customerName + " Thread Started");

        try {
            while (!Thread.interrupted()) {
                Object messageObject = socketWrapper.read();
                handleResponse(messageObject);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                server.customerSockets.remove(customerName);
                socketWrapper.closeConnection();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        System.out.println(customerName + " Thread Stopped");
    }
}
