package RDS.customerApp;

import RDS.classes.DoubleRange;
import RDS.utils.FoodSearchRequest;
import RDS.utils.Palette;
import RDS.utils.RestSearchRequest;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

@SuppressWarnings({ "unchecked" })
public class CustomerAppController {
    CustomerApp main;
    Scene scene;

    @FXML
    TextField custNameField;
    @FXML
    GridPane gridPane;
    @FXML
    Button logoutButton;

    AnchorPane restSearchPane;
    AnchorPane foodSearchPane;
    AnchorPane restOrderPane;

    GridPane[] sidebarButtons = new GridPane[3];

    public void init(CustomerApp main, Scene scene) {
        this.main = main;
        this.scene = scene;
    }

    public void loginButtonHandle() {
        String username = custNameField.getText();

        try {
            main.serverConnect(username);
        } catch (Exception e) {
            Alert failed = new Alert(AlertType.ERROR);
            failed.setContentText(e.getMessage());
            failed.show();
        }
    }

    public void loadMain(AnchorPane[] panes) {
        gridPane.getChildren().clear();
        gridPane.add(panes[0], 0, 0);
        gridPane.add(new AnchorPane(), 1, 0);

        restSearchPane = panes[1];
        loadRestSearchPane();

        foodSearchPane = panes[2];
        loadFoodSearchPane();

        restOrderPane = panes[3];
        loadRestOrderPane();

        sidebarButtons[0] = (GridPane) scene.lookup("#sidebarRestSearch");
        sidebarButtons[1] = (GridPane) scene.lookup("#sidebarFoodSearch");
        sidebarButtons[2] = (GridPane) scene.lookup("#sidebarRestOrder");
        showRestSearchPane();

        logoutButton.setVisible(true);
    }

    void loadRestSearchPane() {
        TableView<RestaurantCell> restSearchTable = (TableView<RestaurantCell>) restSearchPane.getChildren().get(0);

        ObservableList<TableColumn<RestaurantCell, ?>> columns = restSearchTable.getColumns();
        TableColumn<RestaurantCell, String> col;

        col = (TableColumn<RestaurantCell, String>) columns.get(0);
        col.setCellValueFactory(new PropertyValueFactory<>("no"));

        col = (TableColumn<RestaurantCell, String>) columns.get(1);
        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        col = (TableColumn<RestaurantCell, String>) columns.get(2);
        col.setCellValueFactory(new PropertyValueFactory<>("score"));

        col = (TableColumn<RestaurantCell, String>) columns.get(3);
        col.setCellValueFactory(new PropertyValueFactory<>("price"));

        col = (TableColumn<RestaurantCell, String>) columns.get(4);
        col.setCellValueFactory(new PropertyValueFactory<>("categories"));

        col = (TableColumn<RestaurantCell, String>) columns.get(5);
        col.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        col = (TableColumn<RestaurantCell, String>) columns.get(6);
        col.setCellValueFactory(new PropertyValueFactory<>("button"));

        restSearchTable.setItems(main.restSearchObsList);

    }

    void loadFoodSearchPane() {
        TableView<FoodCell> foodSearchTable = (TableView<FoodCell>) foodSearchPane.getChildren().get(0);

        ObservableList<TableColumn<FoodCell, ?>> columns = foodSearchTable.getColumns();
        TableColumn<FoodCell, String> col;

        col = (TableColumn<FoodCell, String>) columns.get(0);
        col.setCellValueFactory(new PropertyValueFactory<>("no"));

        col = (TableColumn<FoodCell, String>) columns.get(1);
        col.setCellValueFactory(new PropertyValueFactory<>("restName"));

        col = (TableColumn<FoodCell, String>) columns.get(2);
        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        col = (TableColumn<FoodCell, String>) columns.get(3);
        col.setCellValueFactory(new PropertyValueFactory<>("price"));

        col = (TableColumn<FoodCell, String>) columns.get(4);
        col.setCellValueFactory(new PropertyValueFactory<>("category"));

        col = (TableColumn<FoodCell, String>) columns.get(5);
        col.setCellValueFactory(new PropertyValueFactory<>("button1"));

        foodSearchTable.setItems(main.foodSearchObsList);
    }

    void loadRestOrderPane() {

    }

    public void showRestSearchPane() {
        resetSidebarButtons();
        sidebarButtons[0].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[0].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(restSearchPane, 1, 0);

        try {
            handleRestSearch();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showFoodSearchPane() {
        resetSidebarButtons();
        sidebarButtons[1].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[1].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(foodSearchPane, 1, 0);

        try {
            handleFoodSearch();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showRestOrderPane() {
        resetSidebarButtons();
        sidebarButtons[2].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[2].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(restOrderPane, 1, 0);
    }

    void resetSidebarButtons() {
        for (GridPane button : sidebarButtons) {
            button.setStyle("-fx-background-color: " + Palette.__secondary_dark);
            button.getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_light);
        }
    }

    public void handleRestSearch() throws Exception {
        var paneNodes = restSearchPane.getChildren();
        String restName = ((TextField) paneNodes.get(1)).getText();

        DoubleRange scoreRange = new DoubleRange();
        String doubleString;
        doubleString = ((TextField) paneNodes.get(2)).getText();
        if (!doubleString.isBlank()) {
            scoreRange.lower = Double.parseDouble(doubleString);
        }
        doubleString = ((TextField) paneNodes.get(3)).getText();
        if (!doubleString.isBlank()) {
            scoreRange.upper = Double.parseDouble(doubleString);
        }

        String price = ((TextField) paneNodes.get(4)).getText();

        String category = ((TextField) paneNodes.get(5)).getText();

        String zipCode = ((TextField) paneNodes.get(6)).getText();

        main.socketWrapper.write(new RestSearchRequest(restName, scoreRange, price, category, zipCode));
    }

    public void handleFoodSearch() throws Exception {
        System.out.println("Searching");

        var paneNodes = foodSearchPane.getChildren();
        String restName = ((TextField) paneNodes.get(1)).getText();

        String foodName = ((TextField) paneNodes.get(2)).getText();

        DoubleRange priceRange = new DoubleRange();
        String doubleString;
        doubleString = ((TextField) paneNodes.get(3)).getText();
        if (!doubleString.isBlank()) {
            priceRange.lower = Double.parseDouble(doubleString);
        }
        doubleString = ((TextField) paneNodes.get(4)).getText();
        if (!doubleString.isBlank()) {
            priceRange.upper = Double.parseDouble(doubleString);
        }

        String category = ((TextField) paneNodes.get(5)).getText();

        System.out.println("Search sent");
        main.socketWrapper.write(new FoodSearchRequest(restName, foodName, priceRange, category));
    }

    public void logout() throws Exception {
        main.reinit();
    }

    public void close() {
        System.exit(0);
    }
}
