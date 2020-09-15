package components.cart;

import DTO.CartDTO;
import DTO.ItemDTO;
import DTO.ItemExtendedDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML private GridPane gridPane;
    @FXML private TableView<ItemExtendedDTO> itemsTableView;

    @FXML private TableColumn<ItemExtendedDTO, Integer> idColumn;
    @FXML private TableColumn<ItemExtendedDTO, String> nameColumn;
    @FXML private TableColumn<ItemExtendedDTO, String> categoryColumn;
    @FXML private TableColumn<ItemExtendedDTO, Double> amountColumn;
    @FXML private TableColumn<ItemExtendedDTO, Double> priceColumn;
    @FXML private TableColumn<ItemExtendedDTO, Double> totalPriceColumn;
    @FXML private TableColumn<ItemExtendedDTO, Boolean> onDiscountColumn;

    @FXML private Label storeName;
    @FXML private Label storeId;
    @FXML private Label PPK;
    @FXML private Label distance;
    @FXML private Label deliveryPrice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("numOfSales"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceSum"));
        onDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("onDiscount"));
    }

    public void fillGridPaneData(CartDTO cart, String storeName, Integer storeId, Double PPK,
                                 Double distance, Double deliveryPrice){
        this.storeName.setText(storeName);
        this.storeId.setText(String.format("%d", storeId));
        this.PPK.setText(String.format("%.2f", PPK));
        this.distance.setText(String.format("%.2f", distance));
        this.deliveryPrice.setText(String.format("%.2f", deliveryPrice));

        ObservableList<ItemExtendedDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(cart.getItems());
        itemsTableView.setItems(itemsOL);
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
