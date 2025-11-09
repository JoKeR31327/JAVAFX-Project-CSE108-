package RDS.customerApp;

import java.util.HashMap;
import java.util.List;

import RDS.classes.Food;
import RDS.classes.Restaurant;
import RDS.utils.ConnectionMessage;
import RDS.utils.SocketWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerApp extends javafx.application.Application {
    SocketWrapper socketWrapper;
    CustomerReadThread readThread;

    CustomerAppController appController;
    Stage primaryStage;

    ObservableList<RestaurantCell> restSearchObsList = FXCollections.observableArrayList();
    ObservableList<FoodCell> foodSearchObsList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("customerLogin.fxml"));
        Parent root = loader.load();
        appController = loader.getController();

        primaryStage.setScene(new Scene(root));

        appController.init(this, primaryStage.getScene());
        primaryStage.show();
    }

    public void serverConnect(String userName) throws Exception {
        socketWrapper = new SocketWrapper("127.0.0.1", 4000);
        socketWrapper.write(new ConnectionMessage("customer", userName, ""));

        Object response = socketWrapper.read();
        if (response instanceof String) {
            String responseString = (String) response;
            if (!responseString.equals("connected")) {
                throw new Exception(responseString);
            }
        }

        readThread = new CustomerReadThread(this, socketWrapper);
        loadMain();
    }

    public void reinit() throws Exception {
        readThread.interrupt();
        socketWrapper.closeConnection();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("customerLogin.fxml"));
        Parent root = loader.load();
        appController = loader.getController();

        primaryStage.setScene(new Scene(root));
        appController.init(this, primaryStage.getScene());
        primaryStage.show();
    }

    void loadMain() throws Exception {
        AnchorPane[] panes = new AnchorPane[4];

        panes[0] = loadComponent("customerSidebar.fxml");
        panes[1] = loadComponent("customerRestSearch.fxml");
        panes[2] = loadComponent("customerFoodSearch.fxml");

        try {
            panes[3] = loadComponent("customerRestOrder.fxml");
        } catch (Exception e) {
            System.out.println(e);
        }

        appController.loadMain(panes);
    }

    AnchorPane loadComponent(String path) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(appController);
        return (AnchorPane) loader.load();
    }

    void updateRestSearch(List<Restaurant> result) {
        restSearchObsList.clear();

        int count = 0;
        for (Restaurant restaurant : result) {
            count++;
            RestaurantCell restCell = new RestaurantCell(count, restaurant);
            restCell.getButton().setOnAction(event -> {
                System.out.println(restCell.getName());
            });
            restSearchObsList.add(restCell);
        }
    }

    void updateFoodSearch(HashMap<String, List<Food>> result) {
        foodSearchObsList.clear();

        int count = 0;
        for (var entry : result.entrySet()) {

            String restName = entry.getKey();

            for (Food food : entry.getValue()) {
                count++;
                FoodCell foodCell = new FoodCell(count, restName, food);
                Button btn = new Button("Order");
                btn.setOnAction(event -> {
                    System.out.println(restName);
                });
                foodCell.setButton1(btn);

                foodSearchObsList.add(foodCell);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
