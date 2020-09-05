package components.order;

import DTO.CustomerDTO;
import DTO.StoreDTO;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

//TODO: handle exceptions when choosing again this option
public class MakeOrderController implements Initializable {

    @FXML private ComboBox<CustomerDTO> chooseCustomerComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<StoreDTO> chooseStoreComboBox;
    @FXML private RadioButton staticOrderRadioButton;
    @FXML private RadioButton dynamicOrderRadioButton;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    @FXML private Label customerIdLabel;
    @FXML private Label storeIdLabel;
    @FXML private Label locationLabel;
    @FXML private Label deliveryPriceLabel;

    private SimpleIntegerProperty customerId;
    private SimpleIntegerProperty storeId;
    private SimpleIntegerProperty xLocation;
    private SimpleIntegerProperty yLocation;
    private SimpleDoubleProperty deliveryPrice;

    private SimpleBooleanProperty customerComboBoxClicked;
    private SimpleBooleanProperty storeComboBoxClicked;
    private SimpleBooleanProperty staticOrderRadioButtonClicked;
    private SimpleBooleanProperty dynamicOrderRadioButtonClicked;
    private SimpleBooleanProperty datePickerClicked;

    private ToggleGroup orderTypeToggleGroup;

    public MakeOrderController(){
        customerId = new SimpleIntegerProperty();
        storeId = new SimpleIntegerProperty();
        xLocation = new SimpleIntegerProperty();
        yLocation = new SimpleIntegerProperty();
        deliveryPrice = new SimpleDoubleProperty();

        customerComboBoxClicked = new SimpleBooleanProperty();
        storeComboBoxClicked = new SimpleBooleanProperty();
        staticOrderRadioButtonClicked = new SimpleBooleanProperty();
        dynamicOrderRadioButtonClicked = new SimpleBooleanProperty();
        datePickerClicked = new SimpleBooleanProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderTypeToggleGroup = new ToggleGroup();
        staticOrderRadioButton.setToggleGroup(orderTypeToggleGroup);
        dynamicOrderRadioButton.setToggleGroup(orderTypeToggleGroup);

        customerIdLabel.textProperty().bind(Bindings.format("%d", customerId));
        storeIdLabel.textProperty().bind(Bindings.format("%d", storeId));
        locationLabel.textProperty().bind(Bindings.format("[%d,%d]", xLocation, yLocation));
        deliveryPriceLabel.textProperty().bind(Bindings.format("%.2f", deliveryPrice));

        customerComboBoxClicked.set(false);
        datePickerClicked.set(false);
        storeComboBoxClicked.set(false);
        staticOrderRadioButtonClicked.set(false);

        staticOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not());
        dynamicOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not());
        chooseStoreComboBox.disableProperty().bind(staticOrderRadioButtonClicked.not());

        okButton.disableProperty().bind(datePickerClicked.not().or(storeComboBoxClicked.not().and(dynamicOrderRadioButtonClicked.not())));
    }

    public void fillMakeOrderData(Engine engine){
        //TODO: this check is necessary?
        if(engine != null){
            ObservableList<CustomerDTO> customerOL = FXCollections.observableArrayList();
            customerOL.addAll(engine.getAllCustomersList());
            chooseCustomerComboBox.setItems(customerOL);
            chooseCustomerComboBox.setOnAction(e -> {
                customerComboBoxClicked.set(true);
                customerId.set(chooseCustomerComboBox.getValue().getId());
            });

            datePicker.setOnAction(e -> datePickerClicked.set(true));

            staticOrderRadioButton.setOnAction(e-> {
                staticOrderRadioButtonClicked.set(true);
                dynamicOrderRadioButtonClicked.set(false);
            });

            dynamicOrderRadioButton.setOnAction(e->{
                dynamicOrderRadioButtonClicked.set(true);
                staticOrderRadioButtonClicked.set(false);
            });

            ObservableList<StoreDTO> storeOL = FXCollections.observableArrayList();
            storeOL.addAll(engine.getAllStoreList());
            chooseStoreComboBox.setItems(storeOL);
            chooseStoreComboBox.setOnAction(e ->{
                storeComboBoxClicked.set(true);
                storeId.set(chooseStoreComboBox.getValue().getId());
                xLocation.set(chooseStoreComboBox.getValue().getxLocation());
                yLocation.set(chooseStoreComboBox.getValue().getyLocation());
                engine.getSDM().calculateDeliveryPrice(storeId.getValue(), customerId.getValue(), deliveryPrice::set);
            });
        }
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }

    @FXML
    void okButtonAction(ActionEvent event) {

    }
}
