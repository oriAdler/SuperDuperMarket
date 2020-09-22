package components.item;

import DTO.ItemDTO;
import common.Input;
import javafx.animation.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
    private SimpleBooleanProperty isItemSelected;

    private SimpleBooleanProperty isAnimationCheckBoxChosen;

    private Button dummyButton;

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

        isItemSelected = new SimpleBooleanProperty(false);

        dummyButton = new Button();
        dummyButton.setOnAction(new dummyButtonListener());
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
                else if(isAnimationCheckBoxChosen.getValue()){
                    dummyButton.fire();
                }
            }
            else{   //category.toUpperCase().equals("QUANTITY")
                if(!Input.isPositiveInteger(newValue)){
                    newValue = "Enter whole positive number";
                }
                else if(isAnimationCheckBoxChosen.getValue()){
                    dummyButton.fire();
                }
            }
        }
        currentItem.setAmount(newValue);

        // Check if at least one item was chosen:
        proceedToCheckout.set(false);
        tableView.getItems().forEach(item -> {
            if(Input.isPositiveInteger(item.getAmount())) {
                // User now can proceed to checkout
                proceedToCheckout.set(true);
            }
        });

        tableView.refresh();
    }

    @FXML
    void priceColumnOnEditCommit(TableColumn.CellEditEvent<ItemDTO, String> event) {
        // Get the selected item:
        ItemDTO currentItem = event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow());

        // Check input validation:
        String newValue = event.getNewValue();
        if(!Input.isPositiveInteger(newValue)){
            newValue = "Enter whole positive number";
        }
        currentItem.setPrice(newValue);

        // Check if at least one item was chosen:
        isItemSelected.set(false);
        tableView.getItems().forEach(item -> {
            if(Input.isPositiveDouble(item.getPrice())) {
                // User now can proceed to checkout
                isItemSelected.set(true);
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

    public TableColumn<ItemDTO, Integer> getSellersColumn() {
        return sellersColumn;
    }

    public TableColumn<ItemDTO, Double> getSalesColumn() {
        return salesColumn;
    }

    public TableColumn<ItemDTO, String> getAmountColumn() {
        return amountColumn;
    }

    public void setIsAnimationCheckBoxChosen(SimpleBooleanProperty isAnimationCheckBoxChosen) {
        this.isAnimationCheckBoxChosen = isAnimationCheckBoxChosen;
    }

    public boolean isProceedToCheckout() {
        return proceedToCheckout.get();
    }

    public SimpleBooleanProperty proceedToCheckoutProperty() {
        return proceedToCheckout;
    }

    public SimpleBooleanProperty getIsItemSelectedProperty(){
        return isItemSelected;
    }

    public void setProceedToCheckout(boolean proceedToCheckout) {
        this.proceedToCheckout.set(proceedToCheckout);
    }

    public void fillTableViewData(List<ItemDTO> itemsList){
        ObservableList<ItemDTO> itemsOL = FXCollections.observableArrayList();
        itemsOL.addAll(itemsList);
        tableView.setItems(itemsOL);
    }

    private class dummyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            Group root = new Group();

            Rectangle rectangleItem = new Rectangle(50, 50, 75, 75);
            rectangleItem.setFill(new ImagePattern(new Image("/image/box.png")));
            root.getChildren().add(rectangleItem);

            Rectangle rectangleCart = new Rectangle(400, 50, 125, 125);
            rectangleCart.setFill(new ImagePattern(new Image("/image/cart.png")));
            root.getChildren().add(rectangleCart);

            FadeTransition ft = new FadeTransition(Duration.millis(3000));
            ft.setFromValue(1);
            ft.setToValue(0.75);

            RotateTransition rt = new RotateTransition(Duration.seconds(5));
            rt.setByAngle(180);

            Path path = new Path();
            path.getElements().addAll(new MoveTo(50, 50), new HLineTo(525));
            path.setFill(null);
            path.setStroke(Color.TRANSPARENT);
            path.setStrokeWidth(2);
            root.getChildren().add(path);

            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);

            stage.show();

            ParallelTransition pt = new ParallelTransition(rectangleItem, ft, rt);
            pt.play();

            PathTransition pt2 = new PathTransition(Duration.millis(2500), path, rectangleItem);
            pt2.setCycleCount(Animation.INDEFINITE);
            pt2.setAutoReverse(true);
            pt2.play();

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished( event -> stage.close() );
            delay.play();
        }
    }
}
