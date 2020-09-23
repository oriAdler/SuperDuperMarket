package components.main;

import common.SDMResourcesConstants;
import components.customer.CustomerController;
import components.discount.AddDiscountController;
import components.file.FileLoaderController;
import components.item.AddItemController;
import components.item.ItemsController;
import components.itemUpdate.ItemUpdateController;
import components.map.MapGenerator;
import components.order.MakeOrderController;
import components.order.OrdersController;
import components.store.StoresController;
import components.store.AddStoreController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import engine.Engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @FXML private Button addStoreButton;
    @FXML private Button addItemButton;
    @FXML private Button addDiscountButton;

    @FXML private CheckBox animationCheckBox;
    @FXML private ComboBox<String> styleComboBox;

    // Miscellaneous:
    private Stage primaryStage;
    private Scene scene;
    private Engine engine;

    // Properties:
    private SimpleBooleanProperty isFileLoaded;
    private SimpleBooleanProperty inDynamicProcedure;
    private SimpleBooleanProperty isAnimationCheckBoxChosen;

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

    private AddStoreController addStoreController;
    private AnchorPane addStoreAnchorPane;

    private AddItemController addItemController;
    private AnchorPane addItemAnchorPane;

    private AddDiscountController addDiscountController;
    private AnchorPane addDiscountAnchorPane;

    private String themeDefaultUrl;
    private String themeFlatBeeUrl;
    private String themeModenaDarkUrl;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        inDynamicProcedure = new SimpleBooleanProperty(false);
        isAnimationCheckBoxChosen = new SimpleBooleanProperty(false);
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
        animationCheckBox.selectedProperty().bindBidirectional(isAnimationCheckBoxChosen);
        addStoreButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        addItemButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));
        addDiscountButton.disableProperty().bind(isFileLoaded.not().or(inDynamicProcedure));

        List<String> styleList = new ArrayList<>();
        styleList.add("Default");
        styleList.add("Dark Modena");
        styleList.add("Flat Bee");
        styleComboBox.getItems().setAll(styleList);

        themeDefaultUrl = getClass().getResource("/css/default.css").toExternalForm();
        themeFlatBeeUrl = getClass().getResource("/css/flatbee.css").toExternalForm();
        themeModenaDarkUrl = getClass().getResource("/css/modenaDark.css").toExternalForm();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
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
        makeOrderController.setIsAnimationCheckBoxChosen(isAnimationCheckBoxChosen);

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

        ScrollPane scrollPaneMap = MapGenerator.draw(engine);

        anchorPaneRight.getChildren().addAll(scrollPaneMap);
        AnchorPane.setTopAnchor(scrollPaneMap, 0.0);
        AnchorPane.setBottomAnchor(scrollPaneMap, 0.0);
        AnchorPane.setRightAnchor(scrollPaneMap, 0.0);
        AnchorPane.setLeftAnchor(scrollPaneMap, 0.0);
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

    @FXML
    void addStoreButtonAction(ActionEvent event) {
        inDynamicProcedure.set(true);

        // Load add store FXML file:
        addStoreController = createAddStoreController();

        // Wire up controller:
        addStoreController.setMainController(this);
        addStoreController.setEngine(engine);
        addStoreController.fillAddStoreData(engine);

        // Place add store scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(addStoreAnchorPane);
        AnchorPane.setTopAnchor(addStoreAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(addStoreAnchorPane, 0.0);
        AnchorPane.setRightAnchor(addStoreAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(addStoreAnchorPane, 0.0);
    }

    @FXML
    void addItemButtonAction(ActionEvent event){
        inDynamicProcedure.set(true);

        // Load add store FXML file:
        addItemController = createAddItemController();

        // Wire up controller:
        addItemController.setMainController(this);
        addItemController.setEngine(engine);
        addItemController.fillAddItemData(engine);

        // Place add item scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(addItemAnchorPane);
        AnchorPane.setTopAnchor(addItemAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(addItemAnchorPane, 0.0);
        AnchorPane.setRightAnchor(addItemAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(addItemAnchorPane, 0.0);
    }

    @FXML
    void addDiscountButtonAction(ActionEvent event){
        inDynamicProcedure.set(true);

        // Load add store FXML file:
        addDiscountController = createAddDiscountController();

        // Wire up controller:
        addDiscountController.setMainController(this);
        addDiscountController.setEngine(engine);
        addDiscountController.fillAddDiscountData(engine);

        // Place add item scene:
        anchorPaneRight.getChildren().clear();
        anchorPaneRight.getChildren().add(addDiscountAnchorPane);
        AnchorPane.setTopAnchor(addDiscountAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(addDiscountAnchorPane, 0.0);
        AnchorPane.setRightAnchor(addDiscountAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(addDiscountAnchorPane, 0.0);
    }

    @FXML
    void styleComboBoxAction(ActionEvent event) {
        scene.getStylesheets().remove(themeDefaultUrl);
        scene.getStylesheets().remove(themeFlatBeeUrl);
        scene.getStylesheets().remove(themeModenaDarkUrl);

        switch (styleComboBox.getValue()) {
            case "Default":
                scene.getStylesheets().add(themeDefaultUrl);
                break;
            case "Dark Modena":
                scene.getStylesheets().add(themeModenaDarkUrl);
                break;
            case "Flat Bee":
                scene.getStylesheets().add(themeFlatBeeUrl);
                break;
        }
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

    public AddStoreController createAddStoreController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ADD_STORE_ANCHOR_PANE);
            addStoreAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AddItemController createAddItemController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ADD_ITEM_ANCHOR_PANE);
            addItemAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AddDiscountController createAddDiscountController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ADD_DISCOUNT_ANCHOR_PANE);
            addDiscountAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
