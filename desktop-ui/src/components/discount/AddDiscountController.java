package components.discount;

import DTO.ItemDTO;
import DTO.OfferDTO;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddDiscountController implements Initializable {

    @FXML private TextField nameTextField;
    @FXML private ComboBox<ItemDTO> itemComboBox;
    @FXML private Button cancelButton;
    @FXML private Button okButton;
    @FXML private Label nameMessageLabel;
    @FXML private Label amountMessageLabel;
    @FXML private TableView<ItemDTO> storeTableView;
    @FXML private TableColumn<ItemDTO, String> itemColumn;
    @FXML private TableColumn<ItemDTO, String> categoryColumn;
    @FXML private TableColumn<ItemDTO, String> amountColumn;
    @FXML private TableColumn<ItemDTO, String> priceColumn;
    @FXML private ComboBox<StoreDTO> storeComboBox;
    @FXML private TextField amountTextField;
    @FXML private ComboBox<String> discountComboBox;
    @FXML private Label addItemLabel;
    @FXML private Label addItemLabel2;
    @FXML private ImageView questionImage;

    private MainController mainController;
    private Engine engine;

    private MessageGeneratorController messageGeneratorController;
    private AnchorPane messageAnchorPane;

    private SimpleBooleanProperty isOfferItemsChosen;
    private SimpleBooleanProperty discountTextFieldIsValid;
    private SimpleBooleanProperty amountTextFieldIsValid;

    private String discountName;
    private Double amount;

    public AddDiscountController(){
        isOfferItemsChosen = new SimpleBooleanProperty(false);
        discountTextFieldIsValid = new SimpleBooleanProperty(false);
        amountTextFieldIsValid = new SimpleBooleanProperty(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.disableProperty().bind(isOfferItemsChosen.not()
                .or((discountTextFieldIsValid.not()))
                .or((amountTextFieldIsValid.not())));
        itemComboBox.setDisable(true);
        amountTextField.setDisable(true);
        discountComboBox.setDisable(true);

        itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountColumn.setVisible(false);
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setVisible(false);

        addItemLabel.setVisible(false);
        addItemLabel2.setVisible(false);

        Tooltip.install(questionImage, new Tooltip("Fill all details & chose at least one item"));
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
    }

    @FXML
    void okButtonAction(ActionEvent event) {
        List<OfferDTO> offerDTOList = new ArrayList<>();

        storeTableView.getItems().forEach(item -> {
            if(Input.isNonNegativeInteger(item.getPrice())){
                if((item.getCategory().equals("Quantity") && Input.isPositiveDouble(item.getAmount()))
                || (item.getCategory().equals("Weight") && Input.isPositiveInteger(item.getAmount()))){
                    offerDTOList.add(new OfferDTO(item.getId(),
                            item.getName(),
                            Double.parseDouble(item.getAmount()),
                            Double.parseDouble(item.getPrice()),
                            storeComboBox.getValue().getId()));
                }
            }
        });

        engine.getSDM().addDiscountToStore(storeComboBox.getValue().getId(), discountName,
                itemComboBox.getValue().getId(), amount,
                discountComboBox.getValue(), offerDTOList);

        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();

        messageGeneratorController = createMessageGenerator();
        messageGeneratorController.setMessageLabelText(String.format("Discount '%s' was added to Super Duper Market", discountName));

        mainController.getAnchorPaneRight().getChildren().add(messageAnchorPane);
        AnchorPane.setTopAnchor(messageAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(messageAnchorPane, 0.0);
        AnchorPane.setRightAnchor(messageAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(messageAnchorPane, 0.0);
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
        checkOneItemIsChosenAndSetItemChosenProperty();

        storeTableView.refresh();
    }

    @FXML
    void amountColumnOnEditCommit(TableColumn.CellEditEvent<ItemDTO, String> event){
        // Get the selected item:
        ItemDTO currentItem = event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow());

        // Check input validation:
        String newValue = event.getNewValue();
        if(currentItem.getCategory().equals("Quantity") && !Input.isPositiveInteger(newValue)){
            newValue = "Enter whole positive number";
        }
        else if(currentItem.getCategory().equals("Weight") && !Input.isPositiveDouble(newValue)){
            newValue = "Enter positive number";
        }
        currentItem.setAmount(newValue);

        // Check if at least one item was chosen:
        checkOneItemIsChosenAndSetItemChosenProperty();

        storeTableView.refresh();
    }

    private void checkOneItemIsChosenAndSetItemChosenProperty(){
        isOfferItemsChosen.set(false);
        storeTableView.getItems().forEach(item -> {
            if(Input.isNonNegativeInteger(item.getPrice())) {
                if(item.getCategory().equals("Quantity") && Input.isPositiveInteger(item.getAmount())){
                    // User now can proceed to checkout
                    isOfferItemsChosen.set(true);
                }
                else if(item.getCategory().equals("Weight") && Input.isPositiveDouble(item.getAmount())){
                    // User now can proceed to checkout
                    isOfferItemsChosen.set(true);
                }
            }
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void fillAddDiscountData(Engine engine){
        ObservableList<StoreDTO> storesOL = FXCollections.observableArrayList();
        storesOL.addAll(engine.getAllStoreList());
        storeComboBox.setItems(storesOL);

        storeComboBox.setOnAction(event -> {
            itemComboBox.setDisable(false);

            ObservableList<ItemDTO> itemsOL = FXCollections.observableArrayList();
            List<ItemDTO> itemDTOList = engine.getSDM().getStoreItems(storeComboBox.getValue().getId());
            itemDTOList.forEach(item->item.setPrice("0"));
            itemsOL.addAll(itemDTOList);
            storeTableView.setItems(itemsOL);

            ObservableList<ItemDTO> itemDTOsOL = FXCollections.observableArrayList();
            itemDTOsOL.addAll(engine.getSDM().getStoreItems(storeComboBox.getValue().getId()));
            itemComboBox.setItems(itemDTOsOL);
        });

        itemComboBox.setOnAction(event -> {
            storeComboBox.setDisable(true);
            amountTextField.setDisable(false);
            discountComboBox.setDisable(false);
        });

        nameTextField.setOnAction(event -> {
            String userInput = nameTextField.getText();
            if(userInput.isEmpty()){
                nameMessageLabel.setText("Enter not empty string");
                discountTextFieldIsValid.set(false);
            }
            else if(engine.getSDM().isDiscountNameExist(userInput)){
                nameMessageLabel.setText("Discount name is taken");
                discountTextFieldIsValid.set(false);
            }
            else{
                nameMessageLabel.setText("Valid name");
                this.discountName = userInput;
                discountTextFieldIsValid.set(true);
            }
        });

        amountTextField.setOnAction(event -> {
            String userInput = amountTextField.getText();
            if(itemComboBox.getValue().getCategory().equals("Weight")) {
                if(!Input.isPositiveDouble(userInput)) {
                    amountMessageLabel.setText("Enter positive number");
                    amountTextFieldIsValid.set(false);
                }
                else {
                    amountMessageLabel.setText("Valid amount");
                    this.amount = Double.parseDouble(userInput);
                    amountTextFieldIsValid.set(true);
                }
            }
            else {  //itemComboBox.getValue().getCategory().equals("Quantity")
                if(!Input.isPositiveInteger(userInput)) {
                    amountMessageLabel.setText("Enter whole positive number");
                    amountTextFieldIsValid.set(false);
                }
                else {
                    amountMessageLabel.setText("Valid amount");
                    this.amount = Double.parseDouble(userInput);
                    amountTextFieldIsValid.set(true);
                }
            }
        });

        // Set combo box data:
        List<String> stringList = new ArrayList<>();
        stringList.add("ONE-OF");
        stringList.add("ALL-OR-NOTHING");
        discountComboBox.getItems().setAll(stringList);

        discountComboBox.setOnAction(event -> {
            addItemLabel.setVisible(true);
            addItemLabel2.setVisible(true);
            amountColumn.setVisible(true);
            priceColumn.setVisible(true);
            storeTableView.setEditable(true);
        });
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
