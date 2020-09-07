package components.customer;

import DTO.CustomerDTO;
import DTO.ItemDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sdm.customer.Customer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    // View:
    @FXML private GridPane gridPane;
    @FXML private TableView<CustomerDTO> tableView;

    // Table View:
    @FXML private TableColumn<CustomerDTO, Integer> idColumn;
    @FXML private TableColumn<CustomerDTO, String> nameColumn;
    @FXML private TableColumn<CustomerDTO, Integer> xColumn;
    @FXML private TableColumn<CustomerDTO, Integer> yColumn;
    @FXML private TableColumn<CustomerDTO, Integer> ordersNumberColumn;
    @FXML private TableColumn<CustomerDTO, Integer> itemsAveragePriceColumn;
    @FXML private TableColumn<CustomerDTO, Integer> deliverAveragePriceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("xLocation"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("yLocation"));
        ordersNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfOrders"));
        itemsAveragePriceColumn.setCellValueFactory(new PropertyValueFactory<>("averageOrdersPrice"));
        deliverAveragePriceColumn.setCellValueFactory(new PropertyValueFactory<>("averageDeliveryPrice"));
    }

    public void fillTableViewData(List<CustomerDTO> customersList){
        ObservableList<CustomerDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(customersList);
        tableView.setItems(itemsOL);
    }
}
