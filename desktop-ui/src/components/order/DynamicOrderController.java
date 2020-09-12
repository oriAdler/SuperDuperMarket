package components.order;

import DTO.CartDTO;
import DTO.ItemDTO;
import DTO.ItemExtendedDTO;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DynamicOrderController implements Initializable {

    @FXML private TableView<CartDTO> tableView;

    @FXML private TableColumn<CartDTO, Integer> storeIdColumn;
    @FXML private TableColumn<CartDTO, String> storeNameColumn;
    @FXML private TableColumn<CartDTO, Integer> xLocationColumn;
    @FXML private TableColumn<CartDTO, Integer> yLocationColumn;
    @FXML private TableColumn<CartDTO, Double> PPKColumn;
    @FXML private TableColumn<CartDTO, Double> distanceColumn;
    @FXML private TableColumn<CartDTO, Double> deliveryPriceColumn;
    @FXML private TableColumn<CartDTO, Integer> numberItemsTypeColumn;
    @FXML private TableColumn<CartDTO, Double> itemsPriceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storeIdColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        xLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storeXLocation"));
        yLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storeYLocation"));
        PPKColumn.setCellValueFactory(new PropertyValueFactory<>("PPK"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distanceFromStoreToCustomer"));
        deliveryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        numberItemsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemsNumber"));
        itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalItemsPrice"));
    }

    public void fillDynamicOrderTableView(List<CartDTO> cartDTOList){
        ObservableList<CartDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(cartDTOList);
        tableView.setItems(itemsOL);
    }
}
