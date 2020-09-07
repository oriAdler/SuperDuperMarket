package components.main;

import common.SDMResourcesConstants;
import components.customer.CustomerController;
import components.item.ItemsController;
import components.order.MakeOrderController;
import components.order.OrdersController;
import components.store.StoresController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import engine.Engine;

import java.io.File;
import java.io.IOException;

public class MainController {

    // View;
    @FXML private AnchorPane anchorPaneRight;

    // Buttons:
    @FXML private Button openFileButton;
    @FXML private Button displayStoresButton;
    @FXML private Button displayItemsButton;
    @FXML private Button showCustomersButton;
    @FXML private Button makeOrderButton;
    @FXML private Button showOrdersHistoryButton;
    @FXML private Button updateStoreItemsButton;
    @FXML private Button showMapButton;

    @FXML private CheckBox animationLabel;
    @FXML private ComboBox<?> styleComboBox;

    // Miscellaneous:
    private Stage primaryStage;
    private Engine engine;

    // Properties:
    private SimpleBooleanProperty isFileLoaded;

    // Secondary Controllers:
    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;
    private StoresController storesController;
    private VBox storesVBox;
    private CustomerController customerController;
    private AnchorPane customerAnchorPane;
    private MakeOrderController makeOrderController;
    private BorderPane makeOrderBorderPane;
    private OrdersController ordersController;
    private AnchorPane ordersAnchorPane;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);

        itemsController = createItemsController();
        customerController = createCustomersController();
        storesController = createStoresController();
        ordersController = createOrderSController();
        makeOrderController = createMakeOrderController();
        makeOrderController.setMainController(this);
    }

    @FXML
    private void initialize(){
        displayStoresButton.disableProperty().bind(isFileLoaded.not());
        displayItemsButton.disableProperty().bind(isFileLoaded.not());
        showCustomersButton.disableProperty().bind(isFileLoaded.not());
        makeOrderButton.disableProperty().bind(isFileLoaded.not());
        showOrdersHistoryButton.disableProperty().bind(isFileLoaded.not());
        updateStoreItemsButton.disableProperty().bind(isFileLoaded.not());
        showMapButton.disableProperty().bind(isFileLoaded.not());
    }

    @FXML
    public void displayItemsButtonAction(ActionEvent actionEvent) {
        itemsController.fillTableViewData(engine.getAllItemList());
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(itemsAnchorPane);
    }

    @FXML
    public void displayStoresButtonAction(ActionEvent event) {
        storesController.fillBorderPaneData(engine);
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(storesVBox);
    }

    @FXML
    void showCustomersButtonOnAction(ActionEvent event) {
        customerController.fillTableViewData(engine.getAllCustomersList());
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(customerAnchorPane);
    }

    @FXML
    public void makeOrderButtonAction(ActionEvent event) {
        makeOrderController.setEngine(engine);
        makeOrderController.fillMakeOrderData(engine);
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(makeOrderBorderPane);
    }

    @FXML
    void openFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select SDM file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try{
            engine.loadDataFromFile(absolutePath);
            isFileLoaded.set(true);
        }
        catch (Exception exception){

        }
    }

    @FXML
    public void showMapButtonAction(ActionEvent event) {

    }

    @FXML
    public void showOrdersHistoryButtonAction(ActionEvent event) {
        ordersController.fillTableViewData(engine.getOrdersHistory());
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(ordersAnchorPane);
    }

    @FXML
    public void updateStoreItemsButtonAction(ActionEvent event) {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public ItemsController createItemsController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ITEMS_ANCHOR_PANE);
            itemsAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public StoresController createStoresController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.STORES_VBOX);
            storesVBox = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CustomerController createCustomersController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.CUSTOMERS_ANCHOR_PANE);
            customerAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MakeOrderController createMakeOrderController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.MAKE_ORDER_BORDER_PANE);
            makeOrderBorderPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrdersController createOrderSController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ORDERS_ANCHOR_PANE);
            ordersAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
