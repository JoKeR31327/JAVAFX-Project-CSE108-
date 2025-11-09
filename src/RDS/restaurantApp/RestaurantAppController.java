package RDS.restaurantApp;

import RDS.classes.Food;
import RDS.utils.Palette;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * RestaurantAppController
 */

@SuppressWarnings({ "unchecked" })
public class RestaurantAppController {
    RestaurantApp main;
    Scene scene;

    @FXML
    TextField restNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    GridPane gridPane;
    @FXML
    Button logoutButton;

    AnchorPane infoPane;
    AnchorPane menuPane;
    AnchorPane orderPane;

    GridPane[] sidebarButtons = new GridPane[3];

    public void init(RestaurantApp main, Scene scene) {
        this.main = main;
        this.scene = scene;
        gridPane.requestFocus();
    }

    public void loginButtonHandle() {
        String username = restNameField.getText();
        String password = passwordField.getText();

        try {
            main.serverConnect(username, password);
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

        infoPane = panes[1];
        loadInfoPane();

        menuPane = panes[2];
        loadMenuPane();

        orderPane = panes[3];
        loadOrderPane();

        sidebarButtons[0] = (GridPane) scene.lookup("#sidebarInfo");
        sidebarButtons[1] = (GridPane) scene.lookup("#sidebarMenu");
        sidebarButtons[2] = (GridPane) scene.lookup("#sidebarOrder");
        showInfoPane();

        logoutButton.setVisible(true);
    }

    void loadInfoPane() {
        for (var item : infoPane.getChildren()) {
            if (item.getId() == null) {
                continue;
            }
            switch (item.getId()) {
                case "restNameText":
                    ((Text) item).setText(main.restaurant.getName());
                    break;
                case "categoryListView":
                    ((ListView<String>) item).getItems().clear();
                    ((ListView<String>) item).getItems().addAll(main.restaurant.getCategories());
                    break;
                case "scoreText":
                    ((Text) item).setText(Double.toString(main.restaurant.getScore()));
                    break;
                case "priceText":
                    ((Text) item).setText(main.restaurant.getPrice());
                    break;
                case "itemCountText":
                    ((Text) item).setText(Integer.toString(main.restaurant.foodCount()));
                    break;
                case "zipCodeText":
                    ((Text) item).setText(main.restaurant.getZipCode());
                    break;

                default:
                    break;
            }
        }
    }

    void loadMenuPane() {
        TableView<FoodCell> menuTable = (TableView<FoodCell>) menuPane.getChildren().get(0);

        ObservableList<TableColumn<FoodCell, ?>> columns = menuTable.getColumns();
        TableColumn<FoodCell, String> col;

        col = (TableColumn<FoodCell, String>) columns.get(0);
        col.setCellValueFactory(new PropertyValueFactory<>("no"));

        col = (TableColumn<FoodCell, String>) columns.get(1);
        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        col = (TableColumn<FoodCell, String>) columns.get(2);
        col.setCellValueFactory(new PropertyValueFactory<>("category"));

        col = (TableColumn<FoodCell, String>) columns.get(3);
        col.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<FoodCell> menu = FXCollections.observableArrayList();

        int count = 0;
        for (Food food : main.restaurant.getMenu()) {
            count++;
            menu.add(new FoodCell(count, food));
        }

        menuTable.setItems(menu);
    }

    void loadOrderPane() {

        TableView<FoodCell> orderTable = (TableView<FoodCell>) orderPane.getChildren().get(0);

        ObservableList<TableColumn<FoodCell, ?>> columns = orderTable.getColumns();
        TableColumn<FoodCell, String> col;

        col = (TableColumn<FoodCell, String>) columns.get(0);
        col.setCellValueFactory(new PropertyValueFactory<>("no"));

        col = (TableColumn<FoodCell, String>) columns.get(1);
        col.setCellValueFactory(new PropertyValueFactory<>("name"));

        col = (TableColumn<FoodCell, String>) columns.get(2);
        col.setCellValueFactory(new PropertyValueFactory<>("category"));

        col = (TableColumn<FoodCell, String>) columns.get(3);
        col.setCellValueFactory(new PropertyValueFactory<>("price"));

        col = (TableColumn<FoodCell, String>) columns.get(4);
        col.setCellValueFactory(new PropertyValueFactory<>("count"));

        orderTable.setItems(main.ordersObservable);
    }

    public void showInfoPane() {
        resetSidebarButtons();
        sidebarButtons[0].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[0].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(infoPane, 1, 0);
    }

    public void showMenuPane() {
        resetSidebarButtons();
        sidebarButtons[1].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[1].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(menuPane, 1, 0);
    }

    public void showOrderPane() {
        resetSidebarButtons();
        sidebarButtons[2].setStyle("-fx-background-color: " + Palette.__primary_light);
        sidebarButtons[2].getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_dark);

        gridPane.getChildren().remove(1);
        gridPane.add(orderPane, 1, 0);
    }

    public void showAddPane() {
        // gridPane.getChildren().remove(1);
        // gridPane.add(infoPane, 1, 0);
        System.out.println("Add");
    }

    void resetSidebarButtons() {
        for (GridPane button : sidebarButtons) {
            button.setStyle("-fx-background-color: " + Palette.__secondary_dark);
            button.getChildren().get(0).setStyle("-fx-fill: " + Palette.__primary_light);
        }
    }

    public void logout() throws Exception {
        main.reinit();
    }

    public void close() {
        System.exit(0);
    }
}