package components.item;

import DTO.ItemDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sdm.item.PurchaseCategory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ItemsController implements Initializable{

    @FXML private TableView<ItemDTO> tableView;

    @FXML private TableColumn<ItemDTO, String> nameColumn;
    @FXML private TableColumn<ItemDTO, Integer> idColumn;
    @FXML private TableColumn<ItemDTO, PurchaseCategory> categoryColumn;
    @FXML private TableColumn<ItemDTO, Integer> sellersColumn;
    @FXML private TableColumn<ItemDTO, Double> priceColumn;
    @FXML private TableColumn<ItemDTO, Double> salesColumn;

    @FXML private TableColumn<?, ?> storeColumn;
    @FXML private TableColumn<ItemDTO, String> storeNameColumn;
    @FXML private TableColumn<ItemDTO, Integer> storeIdColumn;

    public TableView<ItemDTO> getTableView() {
        return tableView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set up the columns in the table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //TODO: why doesn't show enum in table view?
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        sellersColumn.setCellValueFactory(new PropertyValueFactory<>("numOfSellers"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfSales"));
        storeNameColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        storeIdColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));

        storeColumn.setVisible(false);
    }

    public void fillTableViewData(List<ItemDTO> itemsList){
        ObservableList<ItemDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(itemsList);
        tableView.setItems(itemsOL);
    }
}
