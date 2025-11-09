package com.example.project12;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;


public class ShowOrderedFoodsController {

    private volatile boolean stopThread = false;
    public Thread t;
    /*
    private class ReadThread implements Runnable {

        private SocketWrapper tempSocket;

        ReadThread() throws IOException {
        this.tempSocket = new SocketWrapper(main.serverAddress,44444);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (!stopThread) {
                        System.out.println("Helloll");
                        tempSocket.write("7," + main.ClientR.getId());
                        Object o = tempSocket.read();
                        if (o instanceof ArrayList<?>) {
                            ArrayList<Customer> obj = (ArrayList<Customer>) o;
                            System.out.println("Weeee");
                            if (obj.size() > main.ClientR.getCustomersInWaiting().size()) {
                                main.ClientR.setCustomersInWaiting(obj);
                                dataC = FXCollections.observableArrayList();
                                dataC.clear();
                                dataC.addAll(obj);
                                tableViewForShowingCustomers.setEditable(true);
                                tableViewForShowingCustomers.setItems(dataC);
                                tableViewForShowingCustomers.setVisible(true);
                                tableViewForShowingCustomers.refresh();
                            }

                        }
                    }
                    else
                    {
                        tempSocket.closeConnection();
                        break;
                    }
                }

            } catch (IOException e) {
                // Handle IOException, e.g., log the error or take appropriate action
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // Handle ClassNotFoundException, e.g., log the error or take appropriate action
                e.printStackTrace();
            }
        }
    }

     */

    private class ReadThread implements Runnable {
        ShowOrderedFoodsController control;
        private SocketWrapper tempSocket;

        ReadThread(SocketWrapper tempSocket, ShowOrderedFoodsController control) throws IOException {
            this.tempSocket = tempSocket;
            this.control = control;
        }

        @Override
        public void run() {
            try {
                while (!stopThread && !Thread.currentThread().isInterrupted()) {
                    System.out.println("Hello");
                    Object o = tempSocket.read();
                    if (o instanceof ArrayList<?>) {
                        ArrayList<Customer> obj = (ArrayList<Customer>) o;
                        if (obj.size() > main.ClientR.getCustomersInWaiting().size()) {
                            main.ClientR.setCustomersInWaiting(obj);
                            for (Customer c : main.ClientR.getCustomersInWaiting()) {
                                c.setMain(main);
                                c.setControl(control);
                            }
                            dataC = FXCollections.observableArrayList();
                            dataC.clear();
                            dataC.addAll(obj);
                            tableViewForShowingCustomers.setEditable(true);
                            tableViewForShowingCustomers.setItems(dataC);
                            tableViewForShowingCustomers.setVisible(true);
                            tableViewForShowingCustomers.refresh();
                        }
                    }


                }
            }catch (IOException e) {
                // Handle IOException, e.g., log the error or take appropriate action
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // Handle ClassNotFoundException, e.g., log the error or take appropriate action
                e.printStackTrace();
            }
        }
    }




    @FXML
    public TableView tableViewForShowingFoods;
    @FXML
    public TableView tableViewForShowingCustomers;


    @FXML
    private Text nameOfRestaurant;
    @FXML
    private Text score;
    @FXML
    private Text price;
    @FXML
    private Text zipcode;
    @FXML
    private Text categories;
    ObservableList<Food> dataF;
    ObservableList<Customer> dataC;
    private Main main;

    private boolean init1 = true;
    private boolean init2 = true;


    private void setVisibilityFalse() {
        tableViewForShowingFoods.setVisible(false);
        tableViewForShowingCustomers.setVisible(false);

    }
    private void setDetails(){
        nameOfRestaurant.setText(main.ClientR.getName());
        score.setText(main.ClientR.getScore() + "");
        price.setText(main.ClientR.getPrice());
        zipcode.setText(main.ClientR.getZipCode());
        if (main.ClientR.getCategories().size() == 3){

            categories.setText(main.ClientR.getCategories().get(0) + ", " + main.ClientR.getCategories().get(1) + ", " + main.ClientR.getCategories().get(2));
        }
        else if (main.ClientR.getCategories().size() == 2){
            categories.setText(main.ClientR.getCategories().get(0) + ", " + main.ClientR.getCategories().get(1));
        }
        else{
            categories.setText(main.ClientR.getCategories().get(0));
        }

    }
    private void initializeColumnsForOrderedFood() {

        TableColumn<Food, String> foodName = new TableColumn<>("Food Name");
        foodName.setMinWidth(200);
        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodName.setCellFactory(column -> {
            return new TableCell<Food, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item);
                        setAlignment(Pos.CENTER); // Center alignment for text
                    } else {
                        setText(null);
                    }
                }
            };
        });

        TableColumn<Food, String> category = new TableColumn<>("Category");
        category.setMinWidth(200);
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        category.setCellFactory(column -> {
            return new TableCell<Food, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item);
                        setAlignment(Pos.CENTER); // Center alignment for text
                    } else {
                        setText(null);
                    }
                }
            };
        });

        TableColumn<Food, Double> price = new TableColumn<>("Price");
        price.setMinWidth(100);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setCellFactory(column -> {
            return new TableCell<Food, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(String.valueOf(item));
                        setAlignment(Pos.CENTER); // Center alignment for text
                    } else {
                        setText(null);
                    }
                }
            };
        });


        TableColumn<Food, Integer> orderedAmount = new TableColumn<>("Ordered Amount");
        orderedAmount.setMinWidth(100);
        orderedAmount.setCellValueFactory(new PropertyValueFactory<>("orderedAmount"));
        orderedAmount.setCellFactory(column -> {
            return new TableCell<Food, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(String.valueOf(item));
                        setAlignment(Pos.CENTER); // Center alignment for text
                    } else {
                        setText(null);
                    }
                }
            };
        });

        tableViewForShowingFoods.getColumns().addAll(foodName, category, price,orderedAmount);
    }

    private void initializeColumnsForCustomers() {

        TableColumn<Customer, String> custName = new TableColumn<>("Customer Name");
        custName.setMinWidth(300);
        custName.setCellValueFactory(new PropertyValueFactory<>("username"));
        custName.setCellFactory(column -> {
            return new TableCell<Customer, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item);
                        setAlignment(Pos.CENTER); // Center alignment for text
                    } else {
                        setText(null);
                    }
                }
            };
        });

        TableColumn<Customer, Button> clickHere = new TableColumn<>("");
        clickHere.setMinWidth(300);
        clickHere.setCellValueFactory(new PropertyValueFactory<>("ClickHere"));
        clickHere.setCellFactory(column -> {
            return new TableCell<Customer, Button>() {
                @Override
                protected void updateItem(Button item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setGraphic(item); // Use setGraphic to display buttons
                        setAlignment(Pos.CENTER); // Center alignment for buttons
                    } else {
                        setGraphic(null);
                    }
                }
            };
        });


        tableViewForShowingCustomers.getColumns().addAll(custName,clickHere);
    }

    public void loadForFoodView() {
        init1 = true;
        setDetails();
        setVisibilityFalse();
        if (init1) {
            initializeColumnsForOrderedFood();
            //init1 = false;
        }
        dataF = FXCollections.observableArrayList();
        ArrayList<Food> foodTemp = main.restaurantViewingCustomer.getOrderedFoods();
        dataF.addAll(foodTemp);
        tableViewForShowingFoods.setEditable(true);
        tableViewForShowingFoods.setItems(dataF);
        tableViewForShowingFoods.setVisible(true);

    }

    public void loadForCustView() throws IOException, ClassNotFoundException {

            main.restWrapper.write("18," + main.ClientR.getId());
            System.out.println("Rest name :" + main.ClientR.getName());
            ArrayList<Customer> custTemp1 = null;
            custTemp1 = new ArrayList<>((ArrayList<Customer>) main.restWrapper.read());
            main.ClientR.setCustomersInWaiting(custTemp1);
            System.out.println("JJJK " + main.ClientR.getCustomersInWaiting().size());
            for (Customer c : main.ClientR.getCustomersInWaiting()) {
                c.setMain(main);
                c.setControl(this);
            }
            setDetails();
            setVisibilityFalse();
            if (init2) {
                initializeColumnsForCustomers();
            }
            dataC = FXCollections.observableArrayList();
            ArrayList<Customer> custTemp2 = custTemp1;
            dataC.clear();
            dataC.addAll(custTemp2);
            tableViewForShowingCustomers.setEditable(true);
            tableViewForShowingCustomers.setItems(dataC);
            tableViewForShowingCustomers.setVisible(true);
            t = new Thread(new ReadThread(main.restWrapper, this));
            t.start();
        }
        // Create and start a new thread

    public void setMain(Main main) throws IOException, ClassNotFoundException {
        this.main = main;


    }

    public void onBackActionInShowOrder(ActionEvent actionEvent) throws Exception {
        stopThread = true;
        if (t.isAlive()){
            t.interrupt();
            System.out.println(t.isInterrupted());
        }
        main.showRestaurantTimeline();

    }

}