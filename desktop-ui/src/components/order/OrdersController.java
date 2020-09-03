package components.order;

import DTO.ItemDTO;
import DTO.OrderDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML private TableView<OrderDTO> tableView;

    @FXML private TableColumn<OrderDTO, Integer> orderIdColumn;
    @FXML private TableColumn<OrderDTO, Date> dateColumn;
    @FXML private TableColumn<OrderDTO, Integer> storeIdColumn;
    @FXML private TableColumn<OrderDTO, String> storeNameColumn;
    @FXML private TableColumn<OrderDTO, Integer> itemsNumberColumn;
    @FXML private TableColumn<OrderDTO, Double> itemsPriceColumn;
    @FXML private TableColumn<OrderDTO, Double> deliveryPriceColumn;
    @FXML private TableColumn<OrderDTO, Double> totalPriceColumn;

    public TableView<OrderDTO> getTableView() {
        return tableView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        storeIdColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        itemsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numOfItems"));
        itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemsPrice"));
        deliveryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    public void fillTableViewData(List<OrderDTO> ordersList){
        ObservableList<OrderDTO> ordersOL = FXCollections.observableArrayList();
        ordersOL.addAll(ordersList);
        tableView.setItems(ordersOL);
    }
}
