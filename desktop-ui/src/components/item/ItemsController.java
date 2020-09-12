package components.item;

import DTO.ItemDTO;
import common.Input;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ItemsController implements Initializable{

    // View:
    @FXML private AnchorPane anchorPane;
    @FXML private TableView<ItemDTO> tableView;

    // Table View:
    @FXML private TableColumn<ItemDTO, String> nameColumn;
    @FXML private TableColumn<ItemDTO, Integer> idColumn;
    @FXML private TableColumn<ItemDTO, String> categoryColumn;
    @FXML private TableColumn<ItemDTO, Integer> sellersColumn;
    @FXML private TableColumn<ItemDTO, String> priceColumn;
    @FXML private TableColumn<ItemDTO, Double> salesColumn;
    @FXML private TableColumn<ItemDTO, String> amountColumn;

    private SimpleBooleanProperty proceedToCheckout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set generic columns:
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        sellersColumn.setCellValueFactory(new PropertyValueFactory<>("numOfSellers"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfSales"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Set columns for order procedure:
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //amountColumn.setOnEditCommit(this::userEditAmountCell);
        amountColumn.setVisible(false);
        proceedToCheckout = new SimpleBooleanProperty(false);
    }

    @FXML
    void amountColumnOnEditCommit(TableColumn.CellEditEvent<ItemDTO, String> event) {
        // Get the selected item:
        ItemDTO currentItem = event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow());
        String category = currentItem.getCategory();
        String price = currentItem.getPrice();

        // Check input validation:
        String newValue = event.getNewValue();
        if (price.toUpperCase().equals("NOT SOLD")) {
            newValue = "Not Sold";
        }
        else{
            if(category.toUpperCase().equals("WEIGHT")){
                if(!Input.isPositiveDouble(newValue)){
                    newValue = "Enter positive number";
                }
            }
            else{   //category.toUpperCase().equals("QUANTITY")
                if(!Input.isPositiveInteger(newValue)){
                    newValue = "Enter whole positive number";
                }
            }
        }
        currentItem.setAmount(newValue);

        // Check if at least one item was chosen:
        proceedToCheckout.set(false);
        tableView.getItems().forEach(item -> {
            if(Input.isPositiveDouble(item.getAmount())) {
                // User now can proceed to checkout
                proceedToCheckout.set(true);
            }
        });

        tableView.refresh();
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public TableView<ItemDTO> getTableView() {
        return tableView;
    }

    public TableColumn<ItemDTO, String> getPriceColumn() {
        return priceColumn;
    }

    public TableColumn<ItemDTO, String> getAmountColumn() {
        return amountColumn;
    }

    public boolean isProceedToCheckout() {
        return proceedToCheckout.get();
    }

    public SimpleBooleanProperty proceedToCheckoutProperty() {
        return proceedToCheckout;
    }

    public void setProceedToCheckout(boolean proceedToCheckout) {
        this.proceedToCheckout.set(proceedToCheckout);
    }

    public void fillTableViewData(List<ItemDTO> itemsList){
        ObservableList<ItemDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(itemsList);
        tableView.setItems(itemsOL);
    }
}
