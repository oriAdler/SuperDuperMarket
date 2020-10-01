package components.store;

import DTO.ItemDTO;
import common.Input;
import common.SDMResourcesConstants;
import components.item.ItemsController;
import components.main.MainController;
import components.message.MessageGeneratorController;
import engine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddStoreController implements Initializable {

    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<Integer> xComboBox;
    @FXML private ComboBox<Integer> yComboBox;
    @FXML private TextField ppkTextField;
    @FXML private Button cancelButton;
    @FXML private Button okButton;
    @FXML private AnchorPane anchorPaneItems;
    @FXML private Label idMessageLabel;
    @FXML private Label nameMessageLabel;
    @FXML private Label locationMessageLabel;
    @FXML private Label ppkMessageLabel;
    @FXML private ImageView questionImage;

    // Properties:
    private SimpleBooleanProperty idTextFieldIsValid;
    private SimpleBooleanProperty nameTextFieldIsValid;
    private SimpleBooleanProperty ppkTextFieldIsValid;
    private SimpleBooleanProperty xComboBoxIsChosen;
    private SimpleBooleanProperty yComboBoxIsChosen;
    private SimpleBooleanProperty isLocationIsValid;

    private MainController mainController;
    private Engine engine;

    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;

    private MessageGeneratorController messageGeneratorController;
    private AnchorPane messageAnchorPane;

    private int storeId;
    private String storeName;
    private int ppk;
    private Point location;
    private Map<Integer, Integer> itemIdToPrice;

    public AddStoreController(){
        idTextFieldIsValid = new SimpleBooleanProperty(false);
        nameTextFieldIsValid = new SimpleBooleanProperty(false);
        ppkTextFieldIsValid= new SimpleBooleanProperty(false);
        xComboBoxIsChosen = new SimpleBooleanProperty(false);
        yComboBoxIsChosen = new SimpleBooleanProperty(false);
        isLocationIsValid = new SimpleBooleanProperty(false);

        itemsController = createItemsController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.disableProperty().bind(idTextFieldIsValid.not()
                .or((nameTextFieldIsValid).not())
                .or((ppkTextFieldIsValid).not())
                .or((isLocationIsValid.not()))
                .or((itemsController.getIsItemSelectedProperty().not())));

        Tooltip.install(questionImage, new Tooltip("Fill all details & chose at least one item"));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
    }

    @FXML
    void okButtonAction(ActionEvent event) {
        // Collect items from table.
        itemIdToPrice = new HashMap<>();
        itemsController.getTableView().getItems().forEach(item -> {
            if(Input.isPositiveInteger(item.getPrice())){
                itemIdToPrice.put(item.getId(), Integer.parseInt(item.getPrice()));
            }
        });

        engine.getSDM().createNewStore(storeId, storeName, ppk, location, itemIdToPrice);
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();

        messageGeneratorController = createMessageGenerator();
        messageGeneratorController.setMessageLabelText(String.format("Store '%s' was added to Super Duper Market", storeName));

        mainController.getAnchorPaneRight().getChildren().add(messageAnchorPane);
        AnchorPane.setTopAnchor(messageAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(messageAnchorPane, 0.0);
        AnchorPane.setRightAnchor(messageAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(messageAnchorPane, 0.0);
    }

    public void fillAddStoreData(Engine engine){
        idTextField.setOnAction(event -> {
            String userInput = idTextField.getText();
            if(!Input.isPositiveInteger(userInput)){
                idMessageLabel.setText("Enter positive number");
                idTextFieldIsValid.set(false);
            }
            else{
                int userInputInteger = Integer.parseInt(userInput);
                if(engine.getSDM().isStoreExistById(userInputInteger)){
                    idMessageLabel.setText("Id already exist");
                    idTextFieldIsValid.set(false);
                }
                else{
                    this.storeId = userInputInteger;
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
                this.storeName = userInput;
                nameMessageLabel.setText("Valid name");
                nameTextFieldIsValid.set(true);
            }
        });

        ppkTextField.setOnAction(event -> {
            String userInput = ppkTextField.getText();
            if(!Input.isPositiveInteger(userInput)){
                ppkMessageLabel.setText("Enter positive number");
                ppkTextFieldIsValid.set(false);
            }
            else{
                int userInputInteger = Integer.parseInt(userInput);
                this.ppk = userInputInteger;
                ppkMessageLabel.setText("Valid ppk");
                ppkTextFieldIsValid.set(true);
            }
        });

        // Set combo box data:
        xComboBox.getItems().setAll(IntStream.range(1,51).boxed().collect(Collectors.toList()));
        yComboBox.getItems().setAll(IntStream.range(1,51).boxed().collect(Collectors.toList()));

        xComboBox.setOnAction(event -> {
            xComboBoxIsChosen.set(true);
            if(yComboBoxIsChosen.getValue()){
                Point newLocation = new Point(xComboBox.getValue(), yComboBox.getValue());
                if(engine.getSDM().isLocationOccupied(newLocation)){
                    locationMessageLabel.setText("Location is occupied");
                    isLocationIsValid.set(false);
                }
                else{
                    location = newLocation;
                    locationMessageLabel.setText("Valid location");
                    isLocationIsValid.set(true);
                }
            }
        });

        yComboBox.setOnAction(event -> {
            yComboBoxIsChosen.set(true);
            if(xComboBoxIsChosen.getValue()){
                Point newLocation = new Point(xComboBox.getValue(), yComboBox.getValue());
                if(engine.getSDM().isLocationOccupied(newLocation)){
                    locationMessageLabel.setText("Location is occupied");
                    isLocationIsValid.set(false);
                }
                else{
                    location = newLocation;
                    locationMessageLabel.setText("Valid location");
                    isLocationIsValid.set(true);
                }
            }
        });

        // Set the items view table:
        List<ItemDTO> itemDTOList = engine.getAllItemList();
        itemDTOList.forEach(itemDTO -> itemDTO.setPrice("0"));
        itemsController.fillTableViewData(itemDTOList);

        itemsController.getSellersColumn().setVisible(false);
        itemsController.getSalesColumn().setVisible(false);
        itemsController.getPriceColumn().setCellFactory(TextFieldTableCell.forTableColumn());
        itemsController.getTableView().setEditable(true);

        anchorPaneItems.getChildren().clear();
        anchorPaneItems.getChildren().add(itemsAnchorPane);
        AnchorPane.setTopAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(itemsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(itemsAnchorPane, 0.0);
    }

    public ItemsController createItemsController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ITEMS_ANCHOR_PANE);
            itemsAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
