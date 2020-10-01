package components.item;

import DTO.StoreDTO;
import common.Input;
import common.SDMResourcesConstants;
import components.main.MainController;
import components.message.MessageGeneratorController;
import engine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AddItemController implements Initializable {

    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<String> purchaseComboBox;
    @FXML private Button cancelButton;
    @FXML private Button okButton;
    @FXML private Label locationMessageLabel;
    @FXML private Label idMessageLabel;
    @FXML private Label nameMessageLabel;
    @FXML private Label ppkMessageLabel;

    @FXML private TableView<StoreDTO> storeTableView;
    @FXML private TableColumn<StoreDTO, String> storeColumn;
    @FXML private TableColumn<StoreDTO, String> priceColumn;

    @FXML private ImageView questionImage;

    private MainController mainController;
    private Engine engine;

    private MessageGeneratorController messageGeneratorController;
    private AnchorPane messageAnchorPane;

    private SimpleBooleanProperty idTextFieldIsValid;
    private SimpleBooleanProperty nameTextFieldIsValid;
    private SimpleBooleanProperty purchaseComboBoxIsChosen;
    private SimpleBooleanProperty isStoreSelected;

    private int itemId;
    private String itemName;
    private String purchaseMethod;
    private Map<Integer, Integer> storeIdToItemPrice;

    public AddItemController(){
        idTextFieldIsValid = new SimpleBooleanProperty(false);
        nameTextFieldIsValid = new SimpleBooleanProperty(false);
        purchaseComboBoxIsChosen = new SimpleBooleanProperty(false);
        isStoreSelected = new SimpleBooleanProperty(false);

        storeIdToItemPrice = new HashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.disableProperty().bind(idTextFieldIsValid.not()
                .or((nameTextFieldIsValid).not())
                .or((purchaseComboBoxIsChosen).not())
                .or((isStoreSelected.not())));
        storeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("newItemPrice"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        Tooltip.install(questionImage, new Tooltip("Fill all details & chose at least one store"));
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
    }

    @FXML
    void okButtonAction(ActionEvent event) {
        storeTableView.getItems().forEach(store-> {
            if(Input.isPositiveInteger(store.getNewItemPrice())){
                storeIdToItemPrice.put(store.getId(), Integer.parseInt(store.getNewItemPrice()));
            }
        });

        engine.getSDM().addItemToSDM(itemId, itemName, purchaseMethod, storeIdToItemPrice);

        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();

        messageGeneratorController = createMessageGenerator();
        messageGeneratorController.setMessageLabelText(String.format("Item '%s' was added to Super Duper Market", itemName));

        mainController.getAnchorPaneRight().getChildren().add(messageAnchorPane);
        AnchorPane.setTopAnchor(messageAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(messageAnchorPane, 0.0);
        AnchorPane.setRightAnchor(messageAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(messageAnchorPane, 0.0);
    }

    @FXML
    void priceColumnOnEditCommit(TableColumn.CellEditEvent<StoreDTO, String> event) {
        // Get the selected item:
        StoreDTO currentStore = event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow());

        // Check input validation:
        String newValue = event.getNewValue();
        if(!Input.isPositiveInteger(newValue)){
            newValue = "Enter whole positive number";
        }
        currentStore.setNewItemPrice(newValue);

        // Check if at least one item was chosen:
        isStoreSelected.set(false);
        storeTableView.getItems().forEach(store -> {
            if(Input.isPositiveDouble(store.getNewItemPrice())) {
                // User now can proceed to checkout
                isStoreSelected.set(true);
            }
        });

        storeTableView.refresh();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void fillAddItemData(Engine engine){
        idTextField.setOnAction(event -> {
            String userInput = idTextField.getText();
            if(!Input.isPositiveInteger(userInput)){
                idMessageLabel.setText("Enter positive number");
                idTextFieldIsValid.set(false);
            }
            else{
                int userInputInteger = Integer.parseInt(userInput);
                if(engine.getSDM().isItemExistById(userInputInteger)){
                    idMessageLabel.setText("Id already exist");
                    idTextFieldIsValid.set(false);
                }
                else{
                    this.itemId = userInputInteger;
                    idMessageLabel.setText("Valid id");
                    idTextFieldIsValid.set(true);
                }
            }
        });

        nameTextField.setOnAction(event -> {
            String userInput = nameTextField.getText();
            if(userInput.isEmpty()){
                nameMessageLabel.setText("Enter not empty string");
                nameTextFieldIsValid.set(false);
            }
            else{
                this.itemName = userInput;
                nameMessageLabel.setText("Valid name");
                nameTextFieldIsValid.set(true);
            }
        });

        // Set combo box data:
        List<String> stringList = new ArrayList<>();
        stringList.add("Quantity");
        stringList.add("Weight");
        purchaseComboBox.getItems().setAll(stringList);

        purchaseComboBox.setOnAction(event -> {
            purchaseComboBoxIsChosen.set(true);
            purchaseMethod = purchaseComboBox.getValue();
        });

        // Set the items view table:
        List<StoreDTO> storeDTOList = engine.getAllStoreList();
        ObservableList<StoreDTO> storesOL = FXCollections.observableArrayList();
        storesOL.addAll(storeDTOList);
        storeTableView.setEditable(true);
        storeTableView.setItems(storesOL);
    }

    public MessageGeneratorController createMessageGenerator()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.MESSAGE_GENERATOR_ANCHOR_PANE);
            messageAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
