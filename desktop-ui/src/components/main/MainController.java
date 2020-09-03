package components.main;

import DTO.CustomerDTO;
import DTO.ItemExtendedDTO;
import common.SDMResourcesConstants;
import components.customer.CustomerController;
import components.item.ItemsController;
import components.store.StoresController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import engine.Engine;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML private Button openFileButton;
    @FXML private Button displayStoresButton;
    @FXML private Button displayItemsButton;
    @FXML private Button showCustomersButton;
    @FXML private Button makeOrderButton;
    @FXML private Button showOrdersHistoryButton;
    @FXML private Button updateStoreItemsButton;
    @FXML private Button showMapButton;
    @FXML private Pane currentCenterScenePane;

    private SimpleBooleanProperty isFileLoaded;

    private ItemsController itemsController;
    private TableView<ItemExtendedDTO> itemsTableView;
    private StoresController storesController;
    private BorderPane storesBorderPane;
    private CustomerController customerController;
    private TableView<CustomerDTO> customerTableView;

    private Stage primaryStage;
    private Engine engine;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        createItemsTableView();
        createStoresBorderPane();
        createCustomersTableView();
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

    //TODO: is it okay upon every click to fill table data from scratch?
    @FXML
    public void displayItemsButtonAction(ActionEvent actionEvent) {
        itemsController.fillTableViewData(engine.getAllItemList());
        currentCenterScenePane.getChildren().clear();
        currentCenterScenePane.getChildren().add(itemsTableView);
    }

    @FXML
    public void displayStoresButtonAction(ActionEvent event) {
        storesController.fillBorderPaneData(engine);
        currentCenterScenePane.getChildren().clear();
        currentCenterScenePane.getChildren().add(storesBorderPane);
    }

    @FXML
    void showCustomersButtonOnAction(ActionEvent event) {
        customerController.fillTableViewData(engine.getAllCustomersList());
        currentCenterScenePane.getChildren().clear();
        currentCenterScenePane.getChildren().add(customerTableView);
    }

    @FXML
    public void makeOrderButtonAction(ActionEvent event) {

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

    private void createItemsTableView()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ITEMS_TABLE_VIEW);
            itemsTableView = loader.load();
            itemsController = loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStoresBorderPane()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.STORES_BORDER_PANE);
            storesBorderPane = loader.load();
            storesController = loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCustomersTableView()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.CUSTOMERS_TABLE_VIEW);
            customerTableView = loader.load();
            customerController = loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
