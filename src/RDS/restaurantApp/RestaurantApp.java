package RDS.restaurantApp;

import RDS.classes.Restaurant;
import RDS.utils.ConnectionMessage;
import RDS.utils.OrderRequest;
import RDS.utils.SocketWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RestaurantApp extends javafx.application.Application {
    SocketWrapper socketWrapper;
    Restaurant restaurant;
    ObservableList<FoodCell> ordersObservable;
    RestaurantReadThread readThread;

    RestaurantAppController appController;
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurantLogin.fxml"));
        Parent root = loader.load();
        appController = loader.getController();

        primaryStage.setScene(new Scene(root));

        appController.init(this, primaryStage.getScene());
        primaryStage.show();

        ordersObservable = FXCollections.observableArrayList();
    }

    public void reinit() throws Exception {
        readThread.interrupt();
        socketWrapper.closeConnection();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurantLogin.fxml"));
        Parent root = loader.load();
        appController = loader.getController();

        primaryStage.setScene(new Scene(root));
        appController.init(this, primaryStage.getScene());
        primaryStage.show();
    }

    public void serverConnect(String userName, String password) throws Exception {
        socketWrapper = new SocketWrapper("127.0.0.1", 4000);
        socketWrapper.write(new ConnectionMessage("restaurant", userName, password));

        Object response = socketWrapper.read();
        if (response instanceof String) {
            throw new Exception(((String) response));
        }

        if (response instanceof Restaurant) {
            restaurant = (Restaurant) response;
        }

        readThread = new RestaurantReadThread(this, socketWrapper);
        loadMain();
    }

    public void loadMain() throws Exception {
        AnchorPane[] panes = new AnchorPane[4];

        panes[0] = loadComponent("restaurantSidebar.fxml");
        panes[1] = loadComponent("restaurantInfo.fxml");
        panes[2] = loadComponent("restaurantMenu.fxml");
        panes[3] = loadComponent("restaurantOrder.fxml");

        appController.loadMain(panes);

    }

    AnchorPane loadComponent(String path) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(appController);
        return (AnchorPane) loader.load();
    }

    void updateOrders(OrderRequest orders) {
        ordersObservable.clear();

        if (orders.getFoodNames() == null) {
            return;
        }

        int count = 0;
        for (var order : orders.getFoodNames().entrySet()) {
            count++;
            FoodCell f = new FoodCell(count, order.getKey());
            f.setCount(order.getValue());
            ordersObservable.add(f);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
