package components.store;

import DTO.ItemDTO;
import DTO.OrderDTO;
import DTO.StoreDTO;
import common.SDMResourcesConstants;
import components.item.ItemsController;
import components.order.OrdersController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.naming.Binding;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StoresController implements Initializable {

    @FXML private ComboBox<String> storesComboBox;
    @FXML private Label IdLabel;
    @FXML private Label PPKLabel;
    @FXML private Label DeliveryIncomeLabel;
    @FXML private Pane itemsPane;
    @FXML private Pane ordersPane;

    private SimpleIntegerProperty storeId;
    private SimpleDoubleProperty PPK;
    private SimpleDoubleProperty DeliveryIncome;

    private TableView<ItemDTO> itemsTableView;
    private ItemsController itemsController;
    private TableView<OrderDTO> ordersTableView;
    private OrdersController ordersController;

    public StoresController(){
        storeId = new SimpleIntegerProperty();
        PPK = new SimpleDoubleProperty();
        DeliveryIncome = new SimpleDoubleProperty();
    }

    @FXML
    void storesComboBoxAction(ActionEvent event) {

    }

    public void fillBorderPaneData(Engine engine){
        List<StoreDTO> storesList = engine.getAllStoreList();

        // Set stores names in combo box:
        ObservableList<String> storesOL = FXCollections.observableArrayList();
        storesOL.addAll(engine.getAllStoreList()
                .stream()
                .map(StoreDTO::getName)
                .collect(Collectors.toList()));
        storesComboBox.setItems(storesOL);

        storesComboBox.setOnAction(e->displayStoreDetails(storesComboBox.getValue(), engine));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storesComboBox.setPromptText("Choose store");

        IdLabel.textProperty().bind(Bindings.format("%d", storeId));
        PPKLabel.textProperty().bind(Bindings.format("%.0f", PPK));
        DeliveryIncomeLabel.textProperty().bind(Bindings.format("%.2f", DeliveryIncome));
    }

    private void displayStoreDetails(String storeName, Engine engine){
        // Find store
        Optional<StoreDTO> storeOptional = engine.getAllStoreList()
                .stream()
                .filter(storeDTO -> storeDTO.getName().equals(storeName))
                .findAny();
        // Fill store's data:
        if(storeOptional.isPresent()){
            StoreDTO store = storeOptional.get();
            storeId.set(store.getId());
            PPK.set(store.getPPK());
            DeliveryIncome.set(store.getTotalDeliveryIncome());
            // Fill items:
            createItemsTableView();
            itemsController.fillTableViewData(store.getItems());
            itemsPane.getChildren().add(itemsController.getTableView());
            // Fill orders:
//            createOrdersTableView();
//            ordersController.fillTableViewData(store.getOrders());
//            ordersPane.getChildren().add(ordersController.getTableView());
        }
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

    private void createOrdersTableView()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ORDERS_TABLE_VIEW);
            ordersTableView = loader.load();
            ordersController = loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
