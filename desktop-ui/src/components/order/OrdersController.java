package components.order;

import DTO.CartDTO;
import DTO.OrderDTO;
import common.SDMResourcesConstants;
import components.cart.CartController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    // View:
    @FXML private AnchorPane anchorPane;
    @FXML private GridPane gridPane;
    @FXML private TableView<OrderDTO> tableView;

    // Table View:
    @FXML private TableColumn<OrderDTO, Integer> orderIdColumn;
    @FXML private TableColumn<OrderDTO, LocalDate> dateColumn;
    @FXML private TableColumn<OrderDTO, Integer> storeIdColumn;
    @FXML private TableColumn<OrderDTO, String> storeNameColumn;
    @FXML private TableColumn<OrderDTO, Integer> itemsNumberColumn;
    @FXML private TableColumn<OrderDTO, String> itemsPriceColumn;
    @FXML private TableColumn<OrderDTO, String> deliveryPriceColumn;
    @FXML private TableColumn<OrderDTO, String> totalPriceColumn;

    @FXML ComboBox<OrderDTO> chooseOrderComboBox;
    @FXML FlowPane flowPaneOrder;

    // Controllers:
    private CartController cartController;
    private GridPane cartGridPane;
    private Engine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        storeIdColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        itemsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numOfItems"));
        //itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemsPrice"));
        itemsPriceColumn.setCellValueFactory(cell -> Bindings.format("%.2f", cell.getValue().getItemsPrice()));
        //deliveryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        deliveryPriceColumn.setCellValueFactory(cell -> Bindings.format("%.2f", cell.getValue().getDeliveryPrice()));
        //totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalPriceColumn.setCellValueFactory(cell -> Bindings.format("%.2f", cell.getValue().getTotalPrice()));
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public TableView<OrderDTO> getTableView() {
        return tableView;
    }

    public void fillTableViewData(List<OrderDTO> ordersList){
        ObservableList<OrderDTO> ordersOL = FXCollections.observableArrayList();
        ordersOL.addAll(ordersList);
        tableView.setItems(ordersOL);

        if(ordersList.isEmpty()){
            chooseOrderComboBox.setDisable(true);
        }
        else{
            chooseOrderComboBox.setItems(ordersOL);
            chooseOrderComboBox.setOnAction(event -> displayOrderDetails(chooseOrderComboBox.getValue()));
        }
    }

    private void displayOrderDetails(OrderDTO order) {
        List<CartDTO> cartDTOList = engine.getSDM().getDetailedOrder(order.getId());
        flowPaneOrder.getChildren().clear();

        for(CartDTO cart : cartDTOList) {
            CartController currentCartController = createCartController();
            currentCartController.fillGridPaneData(cart, cart.getStoreName(), cart.getStoreId(),
                    cart.getPPK(), cart.getDistanceFromStoreToCustomer(), cart.getDeliveryPrice());

            flowPaneOrder.getChildren().add(currentCartController.getGridPane());
        }
    }

    public CartController createCartController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.CART_GRID_PANE);
            cartGridPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
