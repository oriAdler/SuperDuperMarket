package components.itemUpdate;

import DTO.ItemDTO;
import DTO.StoreDTO;
import common.Input;
import components.main.MainController;
import engine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemUpdateController implements Initializable {

    @FXML private AnchorPane anchorPane;

    @FXML private ComboBox<StoreDTO> storeComboBox;
    @FXML private RadioButton deleteItemRadioButton;
    @FXML private RadioButton addItemRadioButton;
    @FXML private RadioButton updatePriceRadioButton;
    @FXML private ComboBox<ItemDTO> itemComboBox;
    @FXML private TextField updatePriceTextField;
    @FXML private Button approveButton;
    @FXML private Button cancelButton;
    @FXML private Label itemPriceLabel;
    @FXML private TextArea textArea;

    private SimpleBooleanProperty deleteItemRadioButtonClicked;
    private SimpleBooleanProperty addItemRadioButtonClicked;
    private SimpleBooleanProperty updatePriceRadioButtonClicked;
    private SimpleBooleanProperty itemComboBoxClicked;
    private SimpleStringProperty messageLabelProperty;

    private SimpleBooleanProperty priceTextFieldIsValid;

    private MainController mainController;
    private Engine engine;

    public ItemUpdateController(){
        deleteItemRadioButtonClicked = new SimpleBooleanProperty(false);
        addItemRadioButtonClicked = new SimpleBooleanProperty(false);
        updatePriceRadioButtonClicked = new SimpleBooleanProperty(false);
        itemComboBoxClicked = new SimpleBooleanProperty(false);
        messageLabelProperty = new SimpleStringProperty("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteItemRadioButton.setDisable(true);
        addItemRadioButton.setDisable(true);
        updatePriceRadioButton.setDisable(true);

        itemComboBox.setVisible(false);

        itemPriceLabel.visibleProperty().bind(addItemRadioButtonClicked.or(updatePriceRadioButtonClicked));
        updatePriceTextField.visibleProperty().bind(addItemRadioButtonClicked.or(updatePriceRadioButtonClicked));

        approveButton.setDisable(true);
        textArea.textProperty().bind(messageLabelProperty);
        textArea.setVisible(false);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void fillItemUpdateDetails(Engine engine){
        ObservableList<StoreDTO> storesOL = FXCollections.observableArrayList();
        storesOL.addAll(engine.getAllStoreList());
        storeComboBox.setItems(storesOL);

        storeComboBox.setOnAction(e->{
            deleteItemRadioButton.setDisable(false);
            addItemRadioButton.setDisable(false);
            updatePriceRadioButton.setDisable(false);
        });

        deleteItemRadioButton.setOnAction(e->{
            storeComboBox.setDisable(true);
            addItemRadioButton.setDisable(true);
            updatePriceRadioButton.setDisable(true);
            deleteItemRadioButtonClicked.set(true);

            ObservableList<ItemDTO> itemOL = FXCollections.observableArrayList();
            itemOL.addAll(engine.getSDM().getStoreItems(storeComboBox.getValue().getId()));
            itemComboBox.setItems(itemOL);
            itemComboBox.setVisible(true);
        });

        addItemRadioButton.setOnAction(e->{
            storeComboBox.setDisable(true);
            deleteItemRadioButton.setDisable(true);
            updatePriceRadioButton.setDisable(true);
            addItemRadioButtonClicked.setValue(true);

            ObservableList<ItemDTO> itemOL = FXCollections.observableArrayList();
            itemOL.addAll(engine.getSDM().getItemsNotSoldByStore(storeComboBox.getValue().getId()));
            itemComboBox.setItems(itemOL);
            itemComboBox.setVisible(true);
        });

        updatePriceRadioButton.setOnAction(e->{
            storeComboBox.setDisable(true);
            deleteItemRadioButton.setDisable(true);
            addItemRadioButton.setDisable(true);
            updatePriceRadioButtonClicked.setValue(true);

            ObservableList<ItemDTO> itemOL = FXCollections.observableArrayList();
            itemOL.addAll(engine.getSDM().getStoreItems(storeComboBox.getValue().getId()));
            itemComboBox.setItems(itemOL);
            itemComboBox.setVisible(true);
        });

        itemComboBox.setOnAction(e->{
            itemComboBoxClicked.set(true);
            updatePriceTextField.setText("");
            if(deleteItemRadioButtonClicked.getValue()){
                approveButton.setDisable(false);
            }
        });

        updatePriceTextField.setOnAction(e->{
            if(Input.isPositiveInteger(updatePriceTextField.getText()) &&
                    itemComboBoxClicked.getValue()){
                approveButton.setDisable(false);
            }
            else{
                approveButton.setDisable(true);
            }
        });
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
    }

    @FXML
    void approveButtonAction(ActionEvent event){
        StoreDTO store = storeComboBox.getValue();
        ItemDTO item = itemComboBox.getValue();
        textArea.setVisible(true);

        //TODO: Deleted item is included in discount - QA.
        if(deleteItemRadioButtonClicked.getValue()){
            try{
                engine.getSDM().removeItemFromStore(store.getId(), item.getId());
                messageLabelProperty.setValue(String.format("'%s' removed from store '%s' successfully.",
                        item.getName(), store.getName()));
            }
            catch (Exception exception){
                messageLabelProperty.setValue(exception.getMessage());
            }
        }
        else{
            int newPrice = Integer.parseInt(updatePriceTextField.getText());

            if(addItemRadioButtonClicked.getValue()){
                engine.getSDM().addItemToStore(store.getId(), item.getId(), newPrice);
                messageLabelProperty.setValue(String.format("'%s' with price'%d' was added to store '%s' successfully.",
                        item.getName(), newPrice, store.getName()));
            }
            else if(updatePriceRadioButtonClicked.getValue()){
                engine.getSDM().updateItemPriceInStore(store.getId(), item.getId(), newPrice);
                messageLabelProperty.setValue(String.format("'%s' in store '%s' price was updated to '%d' successfully.",
                        item.getName(), store.getName(), newPrice));
            }
        }

        mainController.setInDynamicProcedure(false);
        approveButton.setDisable(true);
        cancelButton.setDisable(true);
    }
}
