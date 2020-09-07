package components.store;

import DTO.StoreDTO;
import common.SDMResourcesConstants;
import components.item.ItemsController;
import components.order.OrdersController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StoresController implements Initializable {

    // View:
    @FXML VBox vbox;
    @FXML private Pane bottomPane;

    @FXML private ComboBox<StoreDTO> storesComboBox;
    @FXML private Label idValueLabel;
    @FXML private Label PPKValueLabel;
    @FXML private Label deliveryIncomeValueLabel;

    private ToggleGroup toggleGroup;
    @FXML private RadioButton itemsRadioButton;
    @FXML private RadioButton ordersRadioButton;

    // Properties:
    private SimpleIntegerProperty storeId;
    private SimpleDoubleProperty PPK;
    private SimpleDoubleProperty DeliveryIncome;

    // Secondary Controllers:
    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;
    private OrdersController ordersController;
    private AnchorPane ordersAnchorPane;

    public StoresController(){
        storeId = new SimpleIntegerProperty();
        PPK = new SimpleDoubleProperty();
        DeliveryIncome = new SimpleDoubleProperty();

        itemsController = createItemsController();
        ordersController = createOrderSController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idValueLabel.textProperty().bind(Bindings.format("%d", storeId));
        PPKValueLabel.textProperty().bind(Bindings.format("%.0f", PPK));
        deliveryIncomeValueLabel.textProperty().bind(Bindings.format("%.2f", DeliveryIncome));

        toggleGroup = new ToggleGroup();
        itemsRadioButton.setToggleGroup(toggleGroup);
        itemsRadioButton.setDisable(true);
        ordersRadioButton.setToggleGroup(toggleGroup);
        ordersRadioButton.setDisable(true);
    }

    public void fillBorderPaneData(Engine engine){
        // Set controllers to default:
        storesComboBox.setValue(null);
        itemsRadioButton.setSelected(false);
        ordersRadioButton.setSelected(false);
        bottomPane.getChildren().clear();

        // Set stores names in combo box:
        ObservableList<StoreDTO> storesOL = FXCollections.observableArrayList();
        storesOL.addAll(engine.getAllStoreList());
        storesComboBox.setItems(storesOL);

        storesComboBox.setOnAction(e -> displayStoreDetails(storesComboBox.getValue()));
    }

    // TODO: without checking store is not null get exception
    private void displayStoreDetails(StoreDTO store){
        if(store != null){
            // Fill store's data:
            storeId.set(store.getId());
            PPK.set(store.getPPK());
            DeliveryIncome.set(store.getTotalDeliveryIncome());

            itemsRadioButton.setSelected(false);
            ordersRadioButton.setSelected(false);
            itemsRadioButton.setDisable(false);
            ordersRadioButton.setDisable(false);

            bottomPane.getChildren().clear();

            itemsRadioButton.setOnAction(e->{
                itemsController.fillTableViewData(store.getItems());
                bottomPane.getChildren().clear();
                bottomPane.getChildren().add(itemsController.getTableView());
            });

            ordersRadioButton.setOnAction(e->{
                ordersController.fillTableViewData(store.getOrders());
                bottomPane.getChildren().clear();
                bottomPane.getChildren().add(ordersController.getTableView());
            });
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
}
