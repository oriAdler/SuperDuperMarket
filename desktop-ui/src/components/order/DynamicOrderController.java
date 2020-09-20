package components.order;

import DTO.CartDTO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DynamicOrderController implements Initializable {

    @FXML private AnchorPane anchorPane;
    @FXML private TableView<CartDTO> tableView;

    @FXML private TableColumn<CartDTO, Integer> storeIdColumn;
    @FXML private TableColumn<CartDTO, String> storeNameColumn;
    @FXML private TableColumn<CartDTO, Integer> xLocationColumn;
    @FXML private TableColumn<CartDTO, Integer> yLocationColumn;
    @FXML private TableColumn<CartDTO, Double> PPKColumn;
    @FXML private TableColumn<CartDTO, String> distanceColumn;
    @FXML private TableColumn<CartDTO, String> deliveryPriceColumn;
    @FXML private TableColumn<CartDTO, Integer> numberItemsTypeColumn;
    @FXML private TableColumn<CartDTO, String> itemsPriceColumn;

    //TODO: why cell->Bindings.. work?
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storeIdColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        xLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storeXLocation"));
        yLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storeYLocation"));
        PPKColumn.setCellValueFactory(new PropertyValueFactory<>("PPK"));
        //distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distanceFromStoreToCustomer"));
        distanceColumn.setCellValueFactory(cell->Bindings.format("%.2f", cell.getValue().getDistanceFromStoreToCustomer()));
        //deliveryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        deliveryPriceColumn.setCellValueFactory(cell->Bindings.format("%.2f", cell.getValue().getDeliveryPrice()));
        numberItemsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemsNumber"));
        //itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalItemsPrice"));
        itemsPriceColumn.setCellValueFactory(cell->Bindings.format("%.2f", cell.getValue().getTotalItemsPrice()));
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void fillDynamicOrderTableView(List<CartDTO> cartDTOList){
        ObservableList<CartDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(cartDTOList);
        tableView.setItems(itemsOL);
    }
}
