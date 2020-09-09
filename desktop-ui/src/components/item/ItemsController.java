package components.item;

import DTO.ItemDTO;
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

    @FXML
    void amountColumnOnEditCommit(TableColumn.CellEditEvent<ItemDTO, String> event) {
        ItemDTO currentItem = event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow());

        String category = currentItem.getCategory();
        String price = currentItem.getPrice();
        String newValue = event.getNewValue();

        if (price.toUpperCase().equals("NOT SOLD")) {
            newValue = "Not Sold";
        }
        else{
            if(category.toUpperCase().equals("WEIGHT")){
                try{
                    double amount = Double.parseDouble(newValue);
                    if(amount < 0){
                        newValue = "Enter Positive Number";
                    }
                }
                catch (Exception ignored){
                    newValue = "Enter Number";
                }
            }
            else{
                try{
                    int amount = Integer.parseInt(newValue);
                    if(amount < 0){
                        newValue = "Enter Positive Number";
                    }
                }
                catch (Exception ignored){
                    newValue = "Enter Whole Number";
                }
            }
        }

        currentItem.setAmount(newValue);

        // Check if at least one item was chosen:
        proceedToCheckout.set(false);
        tableView.getItems().forEach(item -> {
            try{
                if(Double.parseDouble(item.getAmount()) > 0){
                    proceedToCheckout.set(true);
                }
            }
            catch (Exception ignore){

            }
        });

        tableView.refresh();
    }
//    private void userEditAmountCell(TableColumn.CellEditEvent<ItemDTO, String> event) {
//        ItemDTO currentItem = event.getTableView()
//                .getItems()
//                .get(event.getTablePosition().getRow());
//
//        String category = currentItem.getCategory();
//        String price = currentItem.getPrice();
//        String newValue = event.getNewValue();
//
//        if (price.toUpperCase().equals("NOT SOLD")) {
//            newValue = "Not Sold";
//        }
//        else{
//            if(category.toUpperCase().equals("WEIGHT")){
//                try{
//                    double amount = Double.parseDouble(newValue);
//                    if(amount < 0){
//                        newValue = "Enter Positive Number";
//                    }
//                }
//                catch (Exception ignored){
//                    newValue = "Enter Number";
//                }
//            }
//            else{
//                try{
//                    int amount = Integer.parseInt(newValue);
//                    if(amount < 0){
//                        newValue = "Enter Positive Number";
//                    }
//                }
//                catch (Exception ignored){
//                    newValue = "Enter Whole Number";
//                }
//            }
//        }
//
//        currentItem.setAmount(newValue);
//
//        // Check if at least one item was chosen:
//        proceedToCheckout.set(false);
//        tableView.getItems().forEach(item -> {
//            try{
//                if(Double.parseDouble(item.getAmount()) > 0){
//                    proceedToCheckout.set(true);
//                }
//            }
//            catch (Exception ignore){
//
//            }
//        });
//
//        tableView.refresh();
//    }
}
