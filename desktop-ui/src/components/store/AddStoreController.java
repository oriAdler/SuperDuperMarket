package components.store;

import common.Input;
import components.main.MainController;
import engine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.net.URL;
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

    private SimpleBooleanProperty idTextFieldIsValid;
    private SimpleBooleanProperty nameTextFieldIsValid;
    private SimpleBooleanProperty ppkTextFieldIsValid;
    private SimpleBooleanProperty xComboBoxIsChosen;
    private SimpleBooleanProperty yComboBoxIsChosen;
    private SimpleBooleanProperty isLocationIsValid;

    private MainController mainController;
    private Engine engine;

    private int storeId;
    private String storeName;
    private int ppk;
    Point location;

    public AddStoreController(){
        idTextFieldIsValid = new SimpleBooleanProperty(false);
        nameTextFieldIsValid = new SimpleBooleanProperty(false);
        ppkTextFieldIsValid= new SimpleBooleanProperty(false);
        xComboBoxIsChosen = new SimpleBooleanProperty(false);
        yComboBoxIsChosen = new SimpleBooleanProperty(false);
        isLocationIsValid = new SimpleBooleanProperty(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.disableProperty().bind(idTextFieldIsValid.not()
                .or(nameTextFieldIsValid).not()
                .or(ppkTextFieldIsValid).not()
                .or(isLocationIsValid.not()));
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
        xComboBox.getItems().setAll(IntStream.range(1,50).boxed().collect(Collectors.toList()));
        yComboBox.getItems().setAll(IntStream.range(1,50).boxed().collect(Collectors.toList()));

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
    }
}
