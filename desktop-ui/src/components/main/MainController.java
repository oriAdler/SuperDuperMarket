package components.main;

import common.SDMResourcesConstants;
import components.customer.CustomerController;
import components.file.FileLoaderController;
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
    private SimpleBooleanProperty inOrderProcedure;

    // Secondary Controllers:
    private FileLoaderController fileLoaderController;
    private AnchorPane fileLoaderAnchorPane;

    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;

    private StoresController storesController;
    private AnchorPane storesAnchorPane;

    private CustomerController customerController;
    private AnchorPane customerAnchorPane;

    private MakeOrderController makeOrderController;
    private AnchorPane makeOrderAnchorPane;

    private OrdersController ordersController;
    private AnchorPane ordersAnchorPane;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        inOrderProcedure = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize(){
        openFileButton.disableProperty().bind(inOrderProcedure);
        displayStoresButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        displayItemsButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        showCustomersButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        makeOrderButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        showOrdersHistoryButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        updateStoreItemsButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
        showMapButton.disableProperty().bind(isFileLoaded.not().or(inOrderProcedure));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setInOrderProcedure(boolean inOrderProcedure) {
        this.inOrderProcedure.set(inOrderProcedure);
    }

    public AnchorPane getAnchorPaneRight() {
        return anchorPaneRight;
    }

    public SimpleBooleanProperty isFileLoadedProperty() {
        return isFileLoaded;
    }

    @FXML
    public void displayItemsButtonAction(ActionEvent actionEvent) {
        // Load items FXML file
        itemsController = createItemsController();
        itemsController.fillTableViewData(engine.getAllItemList());

        // Place items scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(itemsAnchorPane);
        AnchorPane.setTopAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(itemsAnchorPane, 0.0);
    }

    @FXML
    public void displayStoresButtonAction(ActionEvent event) {
        // Load stores FXML file & wire up:
        storesController = createStoresController();
        storesController.fillBorderPaneData(engine);
        storesController.setEngine(engine);

        // Set stores scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(storesAnchorPane);
        AnchorPane.setTopAnchor(storesAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(storesAnchorPane, 0.0);
        AnchorPane.setRightAnchor(storesAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(storesAnchorPane, 0.0);
    }

    @FXML
    void showCustomersButtonOnAction(ActionEvent event) {
        // Load customers FXML file:
        customerController = createCustomersController();
        customerController.fillTableViewData(engine.getAllCustomersList());

        // Set customers scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(customerAnchorPane);
        AnchorPane.setTopAnchor(customerAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(customerAnchorPane, 0.0);
        AnchorPane.setRightAnchor(customerAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(customerAnchorPane, 0.0);
    }

    @FXML
    public void makeOrderButtonAction(ActionEvent event) {
        inOrderProcedure.set(true);

        // Load make order FXML file:
        ordersController = createOrderSController();
        makeOrderController = createMakeOrderController();

        // Wire up controller:
        makeOrderController.setMainController(this);
        makeOrderController.setEngine(engine);
        makeOrderController.fillMakeOrderData(engine);

        // Set make order scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(makeOrderAnchorPane);
        AnchorPane.setTopAnchor(makeOrderAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(makeOrderAnchorPane, 0.0);
        AnchorPane.setRightAnchor(makeOrderAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(makeOrderAnchorPane, 0.0);
    }

    @FXML
    void openFileButtonAction(ActionEvent event) {
        // Get file path via 'fileChooser':
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select SDM file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Load FXML & Wire up file loader controller:
        fileLoaderController = createFileLoaderController();
        fileLoaderController.setEngine(engine);
        fileLoaderController.setSelectedFile(selectedFile);
        fileLoaderController.setMainController(this);

        if (selectedFile == null) {
            fileLoaderController.setFilePath("No selection has been made");
            fileLoaderController.getLoadFileButton().setDisable(true);
        }
        else{
            fileLoaderController.setFilePath(selectedFile.getAbsolutePath());
            fileLoaderController.getLoadFileButton().setDisable(false);
        }

        // Place file loader scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(fileLoaderAnchorPane);
        AnchorPane.setTopAnchor(fileLoaderAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(fileLoaderAnchorPane, 0.0);
        AnchorPane.setRightAnchor(fileLoaderAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(fileLoaderAnchorPane, 0.0);
    }

    @FXML
    public void showMapButtonAction(ActionEvent event) {

    }

    @FXML
    public void showOrdersHistoryButtonAction(ActionEvent event) {
        // Load make order FXML file:
        ordersController = createOrderSController();

        // Wire up controller:
        ordersController.setEngine(engine);
        ordersController.fillTableViewData(engine.getOrdersHistory());

        // Place show orders history scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(ordersAnchorPane);
        AnchorPane.setTopAnchor(ordersAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(ordersAnchorPane, 0.0);
        AnchorPane.setRightAnchor(ordersAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(ordersAnchorPane, 0.0);
    }

    @FXML
    public void updateStoreItemsButtonAction(ActionEvent event) {

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
            storesAnchorPane = loader.load();
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
            makeOrderAnchorPane = loader.load();
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

    public FileLoaderController createFileLoaderController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.FILE_LOADER_ANCHOR_PANE);
            fileLoaderAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
