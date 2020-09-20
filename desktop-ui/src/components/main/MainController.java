package components.main;

import DTO.CustomerDTO;
import DTO.StoreDTO;
import common.SDMResourcesConstants;
import components.customer.CustomerController;
import components.file.FileLoaderController;
import components.item.ItemsController;
import components.itemUpdate.ItemUpdateController;
import components.map.CustomerButton;
import components.map.StoreButton;
import components.order.MakeOrderController;
import components.order.OrdersController;
import components.store.StoresController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import engine.Engine;

import java.io.File;
import java.io.IOException;

public class MainController {

    // View;
    @FXML private AnchorPane anchorPaneRight;
    @FXML private SplitPane splitPane;

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
    private SimpleBooleanProperty inDynamicProcedure;

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

    private ItemUpdateController itemUpdateController;
    private AnchorPane itemUpdateAnchorPane;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        inDynamicProcedure = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize(){
        openFileButton.disableProperty().bind(inDynamicProcedure);
        displayStoresButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        displayItemsButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        showCustomersButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        makeOrderButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        showOrdersHistoryButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        updateStoreItemsButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        showMapButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setInDynamicProcedure(boolean inDynamicProcedure) {
        this.inDynamicProcedure.set(inDynamicProcedure);
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
        inDynamicProcedure.set(true);

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
        anchorPaneRight.getChildren().clear();

        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int x = 0; x <= engine.getSDM().findMaxXCoordinate()+1; x++) {
            Pane paneDummy = new Pane();
            paneDummy.setMinHeight(20);
            paneDummy.setMinWidth(20);
            gridPane.addColumn(x, paneDummy);
        }
        for (int y = 0; y <= engine.getSDM().findMaxYCoordinate()+1; y++) {
            Pane paneDummy = new Pane();
            paneDummy.setMinHeight(20);
            paneDummy.setMinWidth(20);
            gridPane.addRow(y, paneDummy);
        }

        for(CustomerDTO customer : engine.getAllCustomersList()){
            CustomerButton customerButton = new CustomerButton(customer.getId(),
                    customer.getName(), customer.getNumberOfOrders());
            gridPane.add(customerButton,customer.getXLocation()-1, customer.getYLocation()-1);
        }

        for(StoreDTO store : engine.getAllStoreList()){
            StoreButton storeButton = new StoreButton(store.getId(),
                    store.getName(), store.getPPK(), store.getOrders().size());
            gridPane.add(storeButton, store.getxLocation()-1, store.getyLocation()-1);
        }

        scrollPane.setContent(gridPane);
        anchorPaneRight.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
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
        inDynamicProcedure.set(true);

        // Load make order FXML file:
        itemUpdateController = createItemUpdateController();

        // Wire up controller:
        itemUpdateController.setMainController(this);
        itemUpdateController.setEngine(engine);
        itemUpdateController.fillItemUpdateDetails(engine);

        // Place show orders history scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(itemUpdateAnchorPane);
        AnchorPane.setTopAnchor(itemUpdateAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(itemUpdateAnchorPane, 0.0);
        AnchorPane.setRightAnchor(itemUpdateAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(itemUpdateAnchorPane, 0.0);
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

    public ItemUpdateController createItemUpdateController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ITEM_UPDATE_ANCHOR_PANE);
            itemUpdateAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
